package com.caxerx.servlet;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.MenuDb;
import com.caxerx.db.RestaurantDb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/menuvisibility"})
public class VisibilityController extends HttpServlet {
    private RestaurantDb restaurantDb;
    private MenuDb menuDb;

    @Override
    public void init() throws ServletException {
        menuDb = MenuDb.getInstance(DatabaseConnectionPool.contextInit(getServletContext()));
        restaurantDb = RestaurantDb.getInstance(DatabaseConnectionPool.contextInit(getServletContext()));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        User owner = (User) request.getSession().getAttribute("loggedInAs");
        if (owner == null || owner.getId() <= 0) {
            response.setStatus(401);
            return;
        }

        String mid = request.getParameter("menuId");
        int mId = -1;
        try {
            mId = Integer.parseInt(mid);
        } catch (Exception e) {
            response.setStatus(400);
            return;
        }
        Menu menu = menuDb.findById(mId);
        Restaurant rest = restaurantDb.findById(menu.getRestaurantId());
        if (rest.getOwner() == owner.getId()) {
            menuDb.toggleVisibility(mId);
        }


    }
}
