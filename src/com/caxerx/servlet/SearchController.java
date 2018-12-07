package com.caxerx.servlet;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.Tag;
import com.caxerx.db.*;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchController", urlPatterns = {"/api/search"})
public class SearchController extends HttpServlet {
    private DatabaseConnectionPool pool;
    private MenuDb menuDb;
    private RestaurantDb restaurantDb;
    private Gson gson;
    private LogDb logDb;

    @Override
    public void init() throws ServletException {
        pool = DatabaseConnectionPool.contextInit(getServletContext());
        menuDb = MenuDb.getInstance(pool);
        restaurantDb = RestaurantDb.getInstance(pool);
        logDb = new LogDb(pool);
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String type = request.getParameter("type");
        String keyword = request.getParameter("keyword");
        String tags = request.getParameter("tag");
        String district = request.getParameter("district");

        if (type == null) {
            type = "restaurant";
        }
        List<String> cond = new ArrayList<>();
        switch (type) {
            case "menu":
                String sqlM = "SELECT DISTINCT `Menu`.`id`, `Menu`.`restaurantId`, `Menu`.`title` FROM `Menu`" +
                        "LEFT JOIN `Restaurant` ON `Menu`.`restaurantId` = `Restaurant`.`id` " +
                        "LEFT JOIN `Branch` ON `branch`.`restaurantId` = `restaurant`.`id` " +
                        "LEFT JOIN `MenuTag` ON `Menu`.`id` = `MenuTag`.`menuId` " +
                        " WHERE `Menu`.`showMenu` = TRUE AND (`Menu`.`startTime` = '1970-1-1' OR ? >= `Menu`.`startTime`) AND (`Menu`.`endTime` = '1970-1-1' OR ? <= `Menu`.`startTime`)";
                if (keyword != null) {
                    cond.add("`Menu`.`title` LIKE ?");
                }
                if (tags != null) {
                    cond.add("`MenuTag`.`tagId` = ?");
                }
                if (district != null) {
                    cond.add("`Branch`.`districtId` = ?");
                }
                if (cond.size() > 0) {
                    String s = String.join(" AND ", cond);
                    sqlM = sqlM + " AND " + s;
                }

                try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlM)) {
                    List<Menu> menus = new ArrayList<>();
                    stmt.setDate(1, new Date(System.currentTimeMillis()));
                    stmt.setDate(2, new Date(System.currentTimeMillis()));
                    if (cond.size() > 0) {
                        int n = 3;
                        if (keyword != null) {
                            stmt.setString(n, "%" + keyword + "%");
                            n++;
                        }
                        if (tags != null) {
                            stmt.setString(n, tags);
                            n++;
                        }
                        if (district != null) {
                            stmt.setString(n, district);
                        }
                    }
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            int restaurantId = rs.getInt("restaurantId");
                            String title = rs.getString("title");
                            Menu menu = new Menu();
                            menu.setId(id);
                            menu.setShowMenu(true);
                            menu.setRestaurantId(restaurantId);
                            menu.setTitle(title);
                            List<Tag> tag = menuDb.getMenuTag(menu.getId());
                            menu.setTags(tag);
                            List<Integer> imgs = menuDb.getMenuImage(menu.getId());
                            menu.setImage(imgs);
                            Restaurant rest = restaurantDb.findById(menu.getRestaurantId());
                            menu.setRestaurant(rest);
                            menus.add(menu);
                        }
                        logDb.logSearch(type,keyword,tags,district);
                        out.print(gson.toJson(menus));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case "restaurant":
                List<Restaurant> restaurants = new ArrayList<>();
                String sqlR = "SELECT DISTINCT `Restaurant`.`id`, `Restaurant`.`name`, `Restaurant`.`logo` FROM `Restaurant` " +
                        "LEFT JOIN `Branch` ON `Branch`.`restaurantId` = `Restaurant`.`id` " +
                        "LEFT JOIN `RestaurantTag` ON `Restaurant`.`id` = `RestaurantTag`.`restaurantId`";
                if (keyword != null) {
                    cond.add("`Restaurant`.`name` LIKE ?");
                }
                if (tags != null) {
                    cond.add("`RestaurantTag`.`tagId` = ?");
                }
                if (district != null) {
                    cond.add("`Branch`.`districtId` = ?");
                }
                if (cond.size() > 0) {
                    String s = String.join(" AND ", cond);
                    sqlR = sqlR + " WHERE " + s;
                }
                try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlR)) {
                    if (cond.size() > 0) {
                        int i = 1;
                        if (keyword != null) {
                            stmt.setString(i, "%" + keyword + "%");
                            i++;
                        }
                        if (tags != null) {
                            stmt.setString(i, tags);
                            i++;
                        }
                        if (district != null) {
                            stmt.setString(i, district);
                        }
                    }
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            int logo = rs.getInt("logo");
                            Restaurant restaurant = new Restaurant();
                            restaurant.setId(id);
                            restaurant.setName(name);
                            restaurant.setLogo(logo);
                            List<Menu> rm = menuDb.findRestaurantMenu(restaurant.getId());
                            restaurant.setMenus(rm);
                            List<Tag> tag = restaurantDb.getRestaurantTag(restaurant.getId());
                            restaurant.setTags(tag);
                            restaurants.add(restaurant);
                        }
                        logDb.logSearch(type,keyword,tags,district);
                        out.print(gson.toJson(restaurants));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }


    }
}

