package com.caxerx.servlet;

import com.caxerx.bean.Branch;
import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.BranchDb;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.MenuDb;
import com.caxerx.db.RestaurantDb;
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
import java.util.List;

@WebServlet(name = "BranchController", urlPatterns = {"/api/branch"})
public class BranchController extends HttpServlet {

    private DatabaseConnectionPool pool;
    private BranchDb branchDb;
    private RestaurantDb restaurantDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        restaurantDb = RestaurantDb.getInstance(pool);
        branchDb = BranchDb.getInstance(pool);
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
        AddBranchRequest addBranchRequest;
        try {
            String body = CharStreams.toString(request.getReader());
            addBranchRequest = gson.fromJson(body, AddBranchRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            out.print(new FailResponse("Invalid request -3"));
            return;
        }

        if (addBranchRequest == null) {
            response.setStatus(400);
            out.print(new FailResponse("Invalid request -2"));
            return;
        }


        int code = branchDb.update(owner.getId(), addBranchRequest.getRestaurantId(), addBranchRequest.getBranchId(), addBranchRequest);
        if (code > 0) {
            out.print(new SuccessResponse("Branch edited"));
            return;
        }

        response.setStatus(500);
        out.print(new FailIdResponse("Fail to edit branch", code));

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String restaurantId = request.getParameter("restaurantId");
        String id = request.getParameter("id");

        if (id == null && restaurantId == null) {
            out.print(new FailResponse("Please specify restaurantId or branch(id)"));
            return;
        }

        if (id == null) {
            id = "-1";
        }

        if (restaurantId == null) {
            restaurantId = "-1";
        }


        int parseId = 0;
        int parseRestaurantId = 0;

        try {
            parseId = Integer.parseInt(id);
            parseRestaurantId = Integer.parseInt(restaurantId);
        } catch (NumberFormatException e) {
            out.print(new FailResponse("Invalid branch or restaurant id"));
            response.setStatus(400);
            return;
        }


        if (parseId > 0) {
            Branch branch = branchDb.findById(parseId);
            if (branch == null) {
                response.setStatus(404);
                out.print(new FailResponse("branch not found"));
                return;
            }
            out.print(new SuccessResponse(branch));
            return;
        }

        if (parseRestaurantId > 0) {
            Restaurant restaurant = restaurantDb.findById(parseRestaurantId);

            if (restaurant == null) {
                out.print(new FailResponse("Restaurant Not Found"));
                response.setStatus(404);
                return;
            }

            List<Branch> restaurants = branchDb.findRestaurantBranch(parseRestaurantId);
            if (restaurants == null) {
                out.print(new FailResponse("Failed to get branch"));
                response.setStatus(500);
                return;
            }

            out.print(new SuccessResponse(restaurants));
            return;

        }

        out.print(new FailResponse("Failed to get branch"));
        response.setStatus(500);
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
        String uid = req.getParameter("branchId");
        int uId = -1;
        try {
            uId = Integer.parseInt(uid);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(400);
        }
        branchDb.deleteBranch(uId);
    }

}
