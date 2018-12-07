package com.caxerx.servlet;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.LogDb;
import com.caxerx.db.MenuDb;
import com.caxerx.db.RestaurantDb;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/api/popular/keyword", "/api/popular/restaurant"})
public class PopularController extends HttpServlet {


    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        restaurantDb = RestaurantDb.getInstance(pool);
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        if (request.getServletPath().equalsIgnoreCase("/api/popular/restaurant")) {
            try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT restaurantId, COUNT(*) AS count FROM `visitlog` GROUP BY `restaurantId` ORDER BY count DESC LIMIT 3")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Restaurant> rst = new ArrayList<>();
                    while (rs.next()) {
                        rst.add(restaurantDb.findById(rs.getInt("restaurantId")));
                    }
                    out.print(gson.toJson(rst));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (request.getServletPath().equalsIgnoreCase("/api/popular/keyword")) {
            try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT keyword, COUNT(*) AS count FROM `searchlog`  WHERE keyword != \"\" GROUP BY `keyword` ORDER BY count DESC LIMIT 5")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    List<String> rst = new ArrayList<>();
                    while (rs.next()) {
                        rst.add(rs.getString("keyword"));
                    }
                    out.print(gson.toJson(rst));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(400);
        }
    }
}
