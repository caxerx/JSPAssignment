package com.caxerx.servlet;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.*;
import com.caxerx.request.AddRestaurantRequest;
import com.caxerx.request.LoginRequest;
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
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "RestaurantController", urlPatterns = {"/api/restaurant"})
public class RestaurantController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;
    private Gson gson;
    private LogDb logDb;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        restaurantDb = RestaurantDb.getInstance(pool);
        logDb = new LogDb(pool);
        gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
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
        AddRestaurantRequest addRestaurantRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addRestaurantRequest = gson.fromJson(body, AddRestaurantRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addRestaurantRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (restaurantDb.insert(owner.getId(), addRestaurantRequest)) {
            out.print(new SuccessResponse("Restaurant Created"));
            return;
        }

        response.setStatus(500);
        out.print(new FailResponse("Fail to create restaurant"));

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        int rId = 0;
        if (id == null) {
            List<Restaurant> restaurants = restaurantDb.findAll();
            out.print(gson.toJson(new SuccessResponse(restaurants)));
            return;
        }
        try {
            rId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            out.print(new FailResponse("Invalid restaurant id"));
            response.setStatus(400);
            return;
        }

        Restaurant restaurant = restaurantDb.findById(rId);
        long nowDate = new Date(System.currentTimeMillis()).getTime();
        List<Menu> m = restaurant.getMenus().stream().filter(menu -> menu.isShowMenu() && (menu.getStartTime().getTime() <= 0 || nowDate >= menu.getStartTime().getTime()) && (menu.getEndTime().getTime() <= 0 || nowDate <= menu.getEndTime().getTime())).collect(Collectors.toList());
        restaurant.setMenus(m);

        if (restaurant == null) {
            out.print(new FailResponse("Restaurant Not Found"));
            response.setStatus(404);
            return;
        }

        logDb.logVisit(restaurant.getId());
        out.print(gson.toJson(restaurant));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
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
        AddRestaurantRequest addRestaurantRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addRestaurantRequest = gson.fromJson(body, AddRestaurantRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }
        int id = -1;
        try {
            id = Integer.parseInt(request.getParameter("rid"));
        } catch (Exception e) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addRestaurantRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (restaurantDb.update(owner.getId(), id, addRestaurantRequest)) {
            out.print(new SuccessResponse("Restaurant Edited"));
            return;
        }

        response.setStatus(500);
        out.print(new FailResponse("Fail to update restaurant"));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            resp.sendRedirect("/login.jsp");
            return;
        }
        if (!user.getPermission().contains(2)) {
            resp.setStatus(401);
        }
        String uid = req.getParameter("restaurantId");
        int uId = -1;
        try {
            uId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(400);
        }
        restaurantDb.deleteRestaurant(uId);
    }
}
