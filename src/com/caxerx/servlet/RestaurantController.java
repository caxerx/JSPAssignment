package com.caxerx.servlet;

import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.RestaurantDb;
import com.caxerx.db.TagDb;
import com.caxerx.db.UserDb;
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
import java.util.List;

@WebServlet(name = "RestaurantController", urlPatterns = {"/api/restaurant"})
public class RestaurantController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        restaurantDb = new RestaurantDb(pool);
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

        if (restaurant == null) {
            out.print(new FailResponse("Restaurant Not Found"));
            response.setStatus(404);
            return;
        }

        out.print(gson.toJson(restaurant));
    }
}
