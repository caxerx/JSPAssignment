package com.caxerx.servlet;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.MenuDb;
import com.caxerx.db.RestaurantDb;
import com.caxerx.request.AddMenuRequest;
import com.caxerx.request.AddRestaurantRequest;
import com.caxerx.response.FailIdResponse;
import com.caxerx.response.FailResponse;
import com.caxerx.response.SuccessResponse;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MenuController", urlPatterns = {"/api/menu"})
public class MenuController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private MenuDb menuDb;
    private RestaurantDb restaurantDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        menuDb = new MenuDb(pool);
        restaurantDb = RestaurantDb.getInstance(pool);
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        User owner = (User) request.getSession().getAttribute("loggedInAs");
        if (owner == null || owner.getId() <= 0) {
            response.setStatus(401);
            return;
        }

        if (!request.getContentType().toLowerCase().contains("application/json")) {
            response.setStatus(400);
            out.print(new FailResponse("Unknown content type"));
            return;
        }
        AddMenuRequest addMenuRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addMenuRequest = gson.fromJson(body, AddMenuRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addMenuRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }


        int code = menuDb.insert(owner.getId(), addMenuRequest.getRestaurantId(), addMenuRequest);
        if (code > 0) {
            out.print(new SuccessResponse("Menu Created"));
            return;
        }

        response.setStatus(500);
        out.print(new FailIdResponse("Fail to create menu", code));

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String restaurantId = request.getParameter("restaurantId");
        String id = request.getParameter("id");

        if (id == null && restaurantId == null) {
            out.print(new FailResponse("Please specify restaurantId or menuId(id)"));
            return;
        }

        if (id == null) {
            id = "-1";
        }

        if (restaurantId == null) {
            restaurantId = "-1";
        }


        int parseId = 0;
        int parseRestaurantId = 0;

        try {
            parseId = Integer.parseInt(id);
            parseRestaurantId = Integer.parseInt(restaurantId);
        } catch (NumberFormatException e) {
            out.print(new FailResponse("Invalid menu or restaurant id"));
            response.setStatus(400);
            return;
        }


        if (parseId > 0) {
            Menu menu = menuDb.findById(parseId);
            if (menu == null) {
                response.setStatus(404);
                out.print(new FailResponse("menu not found"));
                return;
            }
            out.print(new SuccessResponse(menu));
            return;
        }

        if (parseRestaurantId > 0) {
            Restaurant restaurant = restaurantDb.findById(parseRestaurantId);

            if (restaurant == null) {
                out.print(new FailResponse("Restaurant Not Found"));
                response.setStatus(404);
                return;
            }

            List<Menu> restaurants = menuDb.findRestaurantMenu(parseRestaurantId);
            if (restaurants == null) {
                out.print(new FailResponse("Failed to get menu"));
                response.setStatus(500);
                return;
            }

            out.print(new SuccessResponse(restaurants));
            return;

        }

        out.print(new FailResponse("Failed to get menu"));
        response.setStatus(500);
    }

}