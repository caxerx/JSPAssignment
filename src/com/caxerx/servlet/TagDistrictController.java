package com.caxerx.servlet;

import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.DistrictDb;
import com.caxerx.db.TagDb;
import com.caxerx.response.FailResponse;
import com.caxerx.response.SuccessResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TagDistrictController", urlPatterns = {"/api/district", "/api/tag"})
public class TagDistrictController extends HttpServlet {
    private DatabaseConnectionPool pool;
    private TagDb tagDb;
    private DistrictDb districtDb;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        tagDb = new TagDb(pool);
        districtDb = DistrictDb.getInstance(pool);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        if (req.getServletPath().startsWith("/api/district")) {
            out.print(new SuccessResponse(districtDb.findAll()));
        } else if (req.getServletPath().startsWith("/api/tag")) {
            out.print(new SuccessResponse(tagDb.findAll()));
        } else {
            resp.setStatus(400);
            out.print(new FailResponse("Invalid action"));
        }

    }
}
