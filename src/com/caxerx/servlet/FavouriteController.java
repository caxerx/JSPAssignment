package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.LikeFavouriteCommentDb;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "FavouriteController", urlPatterns = {"/api/fav"})
public class FavouriteController extends HttpServlet {
    private LikeFavouriteCommentDb likeDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        likeDb = LikeFavouriteCommentDb.getInstance(DatabaseConnectionPool.contextInit(getServletContext()));
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        User owner = (User) req.getSession().getAttribute("loggedInAs");
        if (owner == null || owner.getId() <= 0) {
            resp.setStatus(401);
            return;
        }
        HashMap<String, Object> test = new HashMap<>();
        test.put("menu", likeDb.getFavouriteMenu(owner.getId()));
        test.put("restaurant", likeDb.getFavouriteRestaurant(owner.getId()));
        out.print(gson.toJson(test));
    }
}
