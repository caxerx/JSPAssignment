package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.UserDb;
import com.caxerx.request.LoginRequest;
import com.caxerx.response.FailResponse;
import com.caxerx.response.LoginSuccessResponseContent;
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

@WebServlet(name = "LoginController", urlPatterns = {"/api/login", "/api/logout"})
public class LoginController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private UserDb userDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        userDb = new UserDb(pool);
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/api/logout".equalsIgnoreCase(path)) {
            doLogout(request, response);
            response.sendRedirect("../index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getServletPath();
        if ("/api/login".equalsIgnoreCase(path)) {
            doLogin(request, response);
        }
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        if (request.getContentType() == null || !request.getContentType().toLowerCase().contains("application/json")) {
            response.setStatus(400);
            out.print(new FailResponse("Unknown content type"));
            return;
        }
        LoginRequest loginRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            loginRequest = gson.fromJson(body, LoginRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        User user = userDb.getValidUser(username, password);
        if (user == null) {
            out.print(new FailResponse("Invalid username or password"));
            return;
        }
        request.getSession().setAttribute("loggedInAs", user);
        out.print(new SuccessResponse(new LoginSuccessResponseContent(user)));
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
    }
}
