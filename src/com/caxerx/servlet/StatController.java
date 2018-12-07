package com.caxerx.servlet;

import com.caxerx.bean.District;
import com.caxerx.bean.Tag;
import com.caxerx.bean.User;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.DistrictDb;
import com.caxerx.db.TagDb;
import com.caxerx.db.UserDb;
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
import java.util.HashMap;

@WebServlet(name = "StatController", urlPatterns = {"/api/stat/admin/month", "/api/stat/admin/kind", "/api/stat/admin/district", "/api/stat/owner/month", "/api/stat/owner/kind", "/api/stat/owner/district"})
public class StatController extends HttpServlet {
    private DatabaseConnectionPool pool;
    private Gson gson;
    private DistrictDb districtDb;
    private TagDb tagDb;
    private UserDb userDb;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        gson = new Gson();
        districtDb = DistrictDb.getInstance(pool);
        tagDb = new TagDb(pool);
        userDb = new UserDb(pool);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        User user = (User) req.getSession().getAttribute("loggedInAs");
        if (user == null || user.getId() <= 0) {
            resp.sendRedirect("/login.jsp");
            return;
        }

        if (req.getServletPath().startsWith("/api/stat/admin")) {
            if (!user.getPermission().contains(1)) {
                resp.sendRedirect("/index.jsp");
                return;
            }

            if (req.getServletPath().equalsIgnoreCase("/api/stat/admin/month")) {
                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("select month(timestamp) AS month ,count(*)/(SELECT count(DISTINCT restaurant.id) FROM restaurant) AS count from visitlog group by month(timestamp)")) {
                    HashMap<Integer, Double> m = getAllMonth();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int month = rs.getInt("month");
                            double val = rs.getDouble("count");
                            if (m.containsKey(month)) {
                                m.put(month, val);
                            }
                        }
                        out.print(gson.toJson(m));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (req.getServletPath().equalsIgnoreCase("/api/stat/admin/district")) {
                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("SELECT did, tv/t as avgv FROM (SELECT districtId, COUNT(*) as t FROM Restaurant LEFT JOIN Branch ON Restaurant.id = Branch.restaurantId WHERE districtId IS NOT NULL GROUP BY districtId) AS dbn INNER JOIN (SELECT did, count(*) as tv FROM (SELECT visitlog.id as id, visitlog.restaurantId as rid, branch.districtId as did FROM visitlog INNER JOIN restaurant ON restaurant.id = visitlog.restaurantId LEFT JOIN branch ON restaurant.id = branch.restaurantId WHERE districtId IS NOT NULL) AS tmp GROUP BY did) AS tt ON tt.did = dbn.districtId")) {
                    HashMap<District, Double> m = getAllDistrict();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int districtId = rs.getInt("did");
                            double averageVisitor = rs.getDouble("avgv");
                            for (District district : m.keySet()) {
                                if (district.getId() == districtId) {
                                    m.put(district, averageVisitor);
                                    break;
                                }
                            }
                        }
                        HashMap<String, Double> m2 = new HashMap<>();
                        for (District district : m.keySet()) {
                            m2.put(district.getName(), m.get(district));
                        }

                        out.print(gson.toJson(m2));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (req.getServletPath().equalsIgnoreCase("/api/stat/admin/kind")) {
                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("SELECT t1.tagId, total/tv as avga FROM (SELECT tagId, count(*) as total FROM (SELECT restaurant.id, restauranttag.tagId FROM visitlog INNER JOIN restaurant ON restaurant.id = visitlog.restaurantId LEFT JOIN restauranttag ON restaurant.id = restauranttag.restaurantId) AS mgr GROUP BY tagId) as t1 INNER JOIN (SELECT tagId, COUNT(*) as tv FROM Restaurant LEFT JOIN restauranttag ON Restaurant.id = restauranttag.restaurantId GROUP BY tagId) as t2 ON t1.tagId = t2.tagId")) {
                    HashMap<Tag, Double> m = getAllTags();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int tagId = rs.getInt("tagid");
                            double averageVisitor = rs.getDouble("avga");
                            for (Tag tag : m.keySet()) {
                                if (tag.getId() == tagId) {
                                    m.put(tag, averageVisitor);
                                    break;
                                }
                            }
                        }
                        HashMap<String, Double> m2 = new HashMap<>();
                        for (Tag tag : m.keySet()) {
                            m2.put(tag.getName(), m.get(tag));
                        }

                        out.print(gson.toJson(m2));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {

            if (!user.getPermission().contains(2)) {
                resp.sendRedirect("/index.jsp");
                return;
            }

            if (req.getServletPath().equalsIgnoreCase("/api/stat/owner/month")) {

                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("select month(timestamp) AS month ,count(*) AS count from visitlog inner join restaurant on visitlog.restaurantId = restaurant.id WHERE restaurant.owner = ? group by month(timestamp)")) {
                    stmt.setInt(1, user.getId());
                    HashMap<Integer, Double> m = getAllMonth();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int month = rs.getInt("month");
                            double val = rs.getDouble("count");
                            if (m.containsKey(month)) {
                                m.put(month, val);
                            }
                        }
                        out.print(gson.toJson(m));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (req.getServletPath().equalsIgnoreCase("/api/stat/owner/district")) {
                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("select branch.districtId, count(*) as count from visitlog inner join restaurant on restaurant.id = visitlog.restaurantId INNER JOIN branch on restaurant.id = branch.restaurantId WHERE restaurant.owner = ? GROUP by branch.districtId")) {
                    stmt.setInt(1, user.getId());
                    HashMap<District, Double> m = getAllDistrict();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int districtId = rs.getInt("districtId");
                            double averageVisitor = rs.getDouble("count");
                            for (District district : m.keySet()) {
                                if (district.getId() == districtId) {
                                    m.put(district, averageVisitor);
                                    break;
                                }
                            }
                        }
                        HashMap<String, Double> m2 = new HashMap<>();
                        for (District district : m.keySet()) {
                            m2.put(district.getName(), m.get(district));
                        }

                        out.print(gson.toJson(m2));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (req.getServletPath().equalsIgnoreCase("/api/stat/owner/kind")) {
                try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement("select restauranttag.tagid, count(*) as count from visitlog inner join restaurant on restaurant.id = visitlog.restaurantId INNER JOIN restauranttag on restaurant.id = restauranttag.restaurantId WHERE restaurant.owner = ? group by restauranttag.tagId")) {
                    stmt.setInt(1, user.getId());
                    HashMap<Tag, Double> m = getAllTags();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int tagId = rs.getInt("tagid");
                            double averageVisitor = rs.getDouble("count");
                            for (Tag tag : m.keySet()) {
                                if (tag.getId() == tagId) {
                                    m.put(tag, averageVisitor);
                                    break;
                                }
                            }
                        }
                        HashMap<String, Double> m2 = new HashMap<>();
                        for (Tag tag : m.keySet()) {
                            m2.put(tag.getName(), m.get(tag));
                        }

                        out.print(gson.toJson(m2));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private HashMap<Integer, Double> getAllMonth() {
        HashMap<Integer, Double> months = new HashMap<>();
        months.put(1, 0.0);
        months.put(2, 0.0);
        months.put(3, 0.0);
        months.put(4, 0.0);
        months.put(5, 0.0);
        months.put(6, 0.0);
        months.put(7, 0.0);
        months.put(8, 0.0);
        months.put(9, 0.0);
        months.put(10, 0.0);
        months.put(11, 0.0);
        months.put(12, 0.0);
        return months;
    }

    private HashMap<District, Double> getAllDistrict() {
        HashMap<District, Double> district = new HashMap<>();
        for (District d : districtDb.findAll()) {
            district.put(d, 0d);
        }
        return district;
    }


    private HashMap<Tag, Double> getAllTags() {
        HashMap<Tag, Double> tags = new HashMap<>();
        for (Tag t : tagDb.findAll()) {
            tags.put(t, 0d);
        }
        return tags;
    }
}
