package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.BranchDb;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.MenuDb;
import com.caxerx.request.AddBranchRequest;
import com.caxerx.request.AddMenuRequest;
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

@WebServlet(name = "BranchController", urlPatterns = {"/api/branch"})
public class BranchController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private BranchDb branchDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        branchDb = new BranchDb(pool);
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
        AddBranchRequest addBranchRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addBranchRequest = gson.fromJson(body, AddBranchRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }

        if (addBranchRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request"));
            return;
        }


        int code = branchDb.insert(owner.getId(), addBranchRequest.getRestaurantId(), addBranchRequest);
        if (code > 0) {
            out.print(new SuccessResponse("Branch Created"));
            return;
        }

        response.setStatus(500);
        out.print(new FailIdResponse("Fail to create branch", code));

    }



    /*

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();
        int rId = 0;
        if (id == null) {
            List<Menu> restaurants = menuDb.findAll();
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

        Restaurant restaurant = menuDb.findById(rId);

        if (restaurant == null) {
            out.print(new FailResponse("Restaurant Not Found"));
            response.setStatus(404);
            return;
        }

        out.print(gson.toJson(restaurant));
    }

    */
}
