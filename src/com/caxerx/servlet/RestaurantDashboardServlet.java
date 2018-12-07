package com.caxerx.servlet;

import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.*;
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
    private MenuDb menuDb;
    private BranchDb branchDb;

    @Override
    public void init() throws ServletException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.contextInit(getServletContext());
        tagDb = new TagDb(pool);
        restaurantDb = RestaurantDb.getInstance(pool);
        branchDb = BranchDb.getInstance(pool);
        districtDb = DistrictDb.getInstance(pool);
        menuDb = MenuDb.getInstance(pool);
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            response.sendRedirect("/login.jsp");
            return;
        }
        if (!user.getPermission().contains(2)) {
            response.sendRedirect("/index.jsp");
            return;
        }
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        int rid = -1;
        switch (action) {
            case "dashboard":
                request.setAttribute("nav", "dash");
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("actionName", "Dashboard");
                request.setAttribute("action", "dashboard/restaurant-dash.jsp");
                break;
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
            case "info":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("districts", gson.toJson(districtDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/restaurant-info.jsp");
                request.setAttribute("actionName", "Restaurant Information");
                break;
            case "editinfo":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/restaurant-edit-info.jsp");
                request.setAttribute("actionName", "Edit Information");
                request.setAttribute("editInfo", "");
                break;
            case "addmenu":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("action", "dashboard/restaurant/menu-add.jsp");
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("actionName", "Add Menu");
                break;
            case "addbranch":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("districts", gson.toJson(districtDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/branch-add.jsp");
                request.setAttribute("actionName", "Add Branch");
                break;
            case "editbranch":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("branch", gson.toJson(branchDb.findRestaurantBranch(rid)));
                request.setAttribute("districts", gson.toJson(districtDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/branch-edit.jsp");
                request.setAttribute("actionName", "Edit Branch");
                request.setAttribute("editBranch", request.getParameter("bid"));
                break;
            case "editmenu":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("menu", gson.toJson(menuDb.findRestaurantMenu(rid)));
                request.setAttribute("action", "dashboard/restaurant/menu-edit.jsp");
                request.setAttribute("actionName", "Edit Menu");
                request.setAttribute("editMenu", request.getParameter("mid"));
                break;
            case "listbranch":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("branch", gson.toJson(branchDb.findRestaurantBranch(rid)));
                request.setAttribute("districts", gson.toJson(districtDb.findAll()));
                request.setAttribute("action", "dashboard/restaurant/branch-list.jsp");
                request.setAttribute("actionName", "Branch List");
                break;
            case "listmenu":
                rid = checkOwner(user, request, response);
                if (rid < 0) {
                    return;
                }
                request.setAttribute("tags", gson.toJson(tagDb.findAll()));
                request.setAttribute("menu", gson.toJson(menuDb.findRestaurantMenu(rid)));
                request.setAttribute("action", "dashboard/restaurant/menu-list.jsp");
                request.setAttribute("actionName", "Menu List");
                break;
            default:
                response.sendRedirect("/error/404.jsp");
                return;
        }
        request.setAttribute("restaurant", gson.toJson(restaurantDb.findOwnedRestaurant(user.getId())));
        request.getRequestDispatcher("/restaurant/dashboard.jsp").forward(request, response);
    }

    private int checkOwner(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute("nav", "restaurant");
        String rid = request.getParameter("rid");
        int id;
        if (rid == null) {
            response.sendRedirect("/error/404.jsp");
            return -1;
        }
        try {
            id = Integer.parseInt(rid);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("/error/500.jsp");
            return -1;
        }
        if (id <= 0) {
            response.sendRedirect("/error/404.jsp");
            return -1;
        }
        Restaurant restaurant = restaurantDb.findById(id);
        if (restaurant == null || restaurant.getOwner() != user.getId()) {
            response.sendRedirect("/error/404.jsp");
            return -1;
        }
        return restaurant.getId();
    }
}
