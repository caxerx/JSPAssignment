package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.UserDb;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    UserDb userDb;
    Gson gson;

    @Override
    public void init() throws ServletException {
        userDb = new UserDb(DatabaseConnectionPool.contextInit(getServletContext()));
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            response.sendRedirect("/login.jsp");
            return;
        }
        if (!user.getPermission().contains(1)) {
            response.sendRedirect("/index.jsp");
            return;
        }
        request.setAttribute("nav", "dash");
        String action = request.getParameter("action");
        switch (action) {
            case "userlist":
                request.setAttribute("actionName", "User List");
                request.setAttribute("user", gson.toJson(userDb.getAllUser()));
                request.setAttribute("action", "/admin/admin/user-list.jsp");
                break;
            case "adduser":
                request.setAttribute("actionName", "Add User");
                request.setAttribute("role", gson.toJson(userDb.getAllRole()));
                request.setAttribute("action", "/admin/admin/user-add.jsp");
                break;
            case "edituser":
                String uid = request.getParameter("uid");
                int uId = -1;
                try {
                    uId = Integer.parseInt(uid);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("/admin/admin/user-list.jsp");
                    return;
                }

                request.setAttribute("editUser", uId);
                request.setAttribute("user", gson.toJson(userDb.getAllUser()));
                request.setAttribute("actionName", "Edit User");
                request.setAttribute("role", gson.toJson(userDb.getAllRole()));
                request.setAttribute("action", "/admin/admin/user-edit.jsp");
                break;
            case "dashboard":
                request.setAttribute("actionName", "Dashboard");
                request.setAttribute("user", gson.toJson(userDb.getAllUser()));
                request.setAttribute("action", "/admin/admin/stat.jsp");
                break;
            case "history":
                request.setAttribute("actionName", "Search History");
                request.setAttribute("action", "/admin/admin/search-history.jsp");
                request.setAttribute("","");
                break;
            default:
                response.sendRedirect("/error/404.jsp");
                return;
        }
        RequestDispatcher rd = request.getRequestDispatcher("/admin/dashboard.jsp");
        rd.forward(request, response);
    }
}
