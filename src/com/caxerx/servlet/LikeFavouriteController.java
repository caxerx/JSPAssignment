package com.caxerx.servlet;

import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.LikeFavouriteCommentDb;
import com.caxerx.response.FailResponse;
import com.caxerx.response.SuccessResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/like", "/api/favourite", "/api/comment"}, name = "LikeFavouriteController")
public class LikeFavouriteController extends HttpServlet {
    private DatabaseConnectionPool pool;
    private LikeFavouriteCommentDb likeDb;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        likeDb = LikeFavouriteCommentDb.getInstance(pool);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        User user = (User) req.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            resp.setStatus(401);
            out.print(new FailResponse("not logged in"));
            return;
        }


        String rId = req.getParameter("restaurantId");
        String mId = req.getParameter("menuId");
        String nRating = req.getParameter("rating");
        int rid = -1;
        int mid = -1;
        int rating = -1;
        if (rId != null) {
            try {
                rid = Integer.parseInt(rId);
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid restaurantId"));
                return;
            }
        }

        if (mId != null) {
            try {
                mid = Integer.parseInt(mId);
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid menuId"));
                return;
            }
        }

        if (nRating != null) {
            try {
                rating = Integer.parseInt(nRating);
            } catch (NumberFormatException e) {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid rating"));
                return;
            }
        }
        int uid = user.getId();

        if (req.getServletPath().startsWith("/api/like")) {
            if (rid <= 0) {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid restaurant id"));
                return;
            }
            if (likeDb.isLikedRestaurant(uid, rid)) {
                likeDb.removeLikedRestaurant(uid, rid);
            } else {
                likeDb.likeRestaurant(uid, rid);
            }
        } else if (req.getServletPath().startsWith("/api/favourite")) {
            if (rid > 0) {
                if (likeDb.isFavouriteRestaurant(uid, rid)) {
                    likeDb.removeFavouriteRestaurant(uid, rid);
                } else {
                    likeDb.favouriteRestaurant(uid, rid);
                }
            } else if (mid > 0) {
                if (likeDb.isFavouriteMenu(uid, mid)) {
                    likeDb.removeFavouriteMenu(uid, mid);
                } else {
                    likeDb.favouriteMenu(uid, mid);
                }
            } else {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid id"));
                return;
            }
        } else if (req.getServletPath().startsWith("/api/comment")) {
            if (rid > 0 && (rating >= 1 && rating <= 5)) {
                String comment = req.getParameter("comment");
                if (comment != null) {
                    likeDb.commentRestaurant(uid, rid, comment, rating);
                }
            } else {
                resp.setStatus(400);
                out.print(new FailResponse("Invalid request"));
                return;
            }
        } else {
            resp.setStatus(400);
            out.print(new FailResponse("Invalid action"));
            return;
        }
        out.print(new SuccessResponse("Done"));
    }
}