package com.caxerx.servlet;

import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.DistrictDb;
import com.caxerx.db.RestaurantDb;
import com.caxerx.db.TagDb;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RestaurantDashboardServlet", urlPatterns = "/restaurant/dashboard")
public class RestaurantDashboardServlet extends HttpServlet {
    private TagDb tagDb;
    private RestaurantDb restaurantDb;
    private Gson gson;
    private DistrictDb districtDb;

    @Override
    public void init() throws ServletException {
        tagDb = new TagDb(DatabaseConnectionPool.contextInit(getServletContext()));
        restaurantDb = new RestaurantDb(DatabaseConnectionPool.contextInit(getServletContext()));
        districtDb = new DistrictDb(DatabaseConnectionPool.contextInit(getServletContext()));
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            response.sendRedirect("/login.jsp");
            return;
        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "add":
                request.setAttribute("nav", "dash");
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("actionName", "Add Restaurant");
                request.setAttribute("action", "dashboard/restaurant-add.jsp");
                break;
            case "list":
                request.setAttribute("nav", "dash");
                request.setAttribute("actionName", "Restaurant List");
                request.setAttribute("action", "dashboard/restaurant-list.jsp");
                break;
            case "addmenu":
                if (!checkOwner(user, request, response)) {
                    return;
                }

                request.setAttribute("action", "dashboard/restaurant/menu-add.jsp");
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("actionName", "Add Menu");
                break;
            case "addbranch":
                if (!checkOwner(user, request, response)) {
                    return;
                }
                request.setAttribute("districts", gson.toJson(districtDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/branch-add.jsp");
                request.setAttribute("actionName", "Add Branch");
                break;
            default:
                response.sendRedirect("/error/404.jsp");
                return;
        }
        request.setAttribute("restaurant", gson.toJson(restaurantDb.findOwnedRestaurant(user.getId())));
        request.getRequestDispatcher("/restaurant/dashboard.jsp").forward(request, response);
    }

    private boolean checkOwner(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute("nav", "restaurant");
        String rid = request.getParameter("rid");
        int id;
        try {
            id = Integer.parseInt(rid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("/error/500.jsp");
            return false;
        }
        if (id <= 0) {
            response.sendRedirect("/error/404.jsp");
            return false;
        }
        Restaurant restaurant = restaurantDb.findById(id);
        if (restaurant == null || restaurant.getOwner() != user.getId()) {
            response.sendRedirect("/error/404.jsp");
            return false;
        }
        return true;
    }
}
