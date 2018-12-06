package com.caxerx.servlet;

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
        request.setAttribute("nav", "dash");
        String action = request.getParameter("action");
        switch (action) {
            case "userlist":
                request.setAttribute("user", gson.toJson(userDb.getAllUser()));
                request.setAttribute("action", "/admin/admin/user-list.jsp");
                break;
            case "adduser":
                request.setAttribute("role", gson.toJson(userDb.getAllRole()));
                request.setAttribute("action", "/admin/admin/user-add.jsp");
                break;
        }
        RequestDispatcher rd = request.getRequestDispatcher("/admin/dashboard.jsp");
        rd.forward(request, response);
    }
}
