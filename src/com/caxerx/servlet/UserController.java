package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.RestaurantDb;
import com.caxerx.db.UserDb;
import com.caxerx.request.AddRestaurantRequest;
import com.caxerx.request.AddUserRequest;
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


@WebServlet(name = "UserController", urlPatterns = {"/api/user"})
public class UserController extends HttpServlet {
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
        AddUserRequest addUserRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addUserRequest = gson.fromJson(body, AddUserRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addUserRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        try {
            userDb.insert(addUserRequest);
            out.print(new SuccessResponse("Create successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print(new FailResponse("Fail to create user"));
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        AddUserRequest addUserRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addUserRequest = gson.fromJson(body, AddUserRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addUserRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        try {
            userDb.update(addUserRequest);
            out.print(new SuccessResponse("Edited successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print(new FailResponse("Fail to edit user"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            resp.sendRedirect("/login.jsp");
            return;
        }
        if (!user.getPermission().contains(1)) {
            resp.setStatus(401);
        }
        String uid = req.getParameter("userId");
        int uId = -1;
        try {
            uId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(400);
        }
        userDb.delete(uId);
    }
}
