package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.LikeFavouriteCommentDb;
import com.caxerx.response.SuccessResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/liked", "/api/favourited"})
public class LikedController extends HttpServlet {
    private LikeFavouriteCommentDb likedDb;

    @Override
    public void init() throws ServletException {
        likedDb = LikeFavouriteCommentDb.getInstance(DatabaseConnectionPool.contextInit(getServletContext()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        User user = (User) req.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            resp.setStatus(401);
            return;
        }

        if (req.getServletPath().equalsIgnoreCase("/api/liked")) {
            String rid = req.getParameter("restaurantId");
            int rId = -1;
            try {
                rId = Integer.parseInt(rid);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (likedDb.isLikedRestaurant(user.getId(), rId)) {
                out.print(new SuccessResponse(true));
            } else {
                out.print(new SuccessResponse(false));
            }
            return;
        }

        if (req.getServletPath().equalsIgnoreCase("/api/favourited")) {

            String rid = req.getParameter("restaurantId");
            String mid = req.getParameter("menuId");


            if (rid != null) {
                int rId = -1;
                try {
                    rId = Integer.parseInt(rid);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (likedDb.isFavouriteRestaurant(user.getId(), rId)) {
                    out.print(new SuccessResponse(true));
                } else {
                    out.print(new SuccessResponse(false));
                }
                return;
            }

            if (mid != null) {
                int rId = -1;
                try {
                    rId = Integer.parseInt(mid);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (likedDb.isFavouriteMenu(user.getId(), rId)) {
                    out.print(new SuccessResponse(true));
                } else {
                    out.print(new SuccessResponse(false));
                }
            }
        }

    }
}
