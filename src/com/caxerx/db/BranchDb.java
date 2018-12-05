package com.caxerx.db;

import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.Tag;
import com.caxerx.request.AddBranchRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDb {
    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;

    public BranchDb(DatabaseConnectionPool pool) {
        this.pool = pool;
        restaurantDb = new RestaurantDb(pool);
    }

    public int insert(int userId, int restaurantId, AddBranchRequest request) {
        Restaurant restaurant = restaurantDb.findById(restaurantId);
        if (restaurant == null) {
            return -1;
        }
        if (restaurant.getOwner() != userId) {
            return -2;
        }
        try (Connection conn = pool.getConnection()) {
            int id = -1;
            try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Branch", 7), Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, 0);
                stmt.setInt(2, restaurantId);
                stmt.setString(3, request.getBranchName());
                stmt.setInt(4, request.getDistrict());
                stmt.setString(5, request.getAddress());
                stmt.setString(6, request.getPhoneNumber());
                stmt.setString(7, request.getOpenTime() + " - " + request.getCloseTime());
                int rs = stmt.executeUpdate();
                if (rs > 0) {
                    ResultSet gK = stmt.getGeneratedKeys();
                    if (gK.next()) {
                        id = gK.getInt(1);
                    }
                }
            }

            for (int tag : request.getDeliveryDistrict()) {
                insertDeliveryDistrict(conn, id, tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }
        return 1;
    }


    public void insertDeliveryDistrict(Connection conn, int branchId, int districtId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("BranchDeliveryDistrict", 2))) {
            stmt.setInt(1, branchId);
            stmt.setInt(2, districtId);
            stmt.executeUpdate();
        }
    }


    public List<Tag> getMenuTag(int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumnWithJoin("MenuTag", "INNER JOIN Tag ON MenuTag.tagId = Tag.id", "menuId"))) {
            stmt.setInt(1, menuId);
            List<Tag> tags = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int tagId = rs.getInt("tagId");
                    String tagName = rs.getString("name");
                    tags.add(new Tag(tagId, tagName));
                }
                return tags;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Menu> findAll() {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryAll("Menu"))) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Menu> menus = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int restaurantId = rs.getInt("restaurantId");
                    String title = rs.getString("title");
                    Date startDate = rs.getDate("startTime");
                    Date endDate = rs.getDate("endTime");
                    boolean showMenu = rs.getBoolean("showMenu");
                    Menu menu = new Menu(id, restaurantId, title, startDate, endDate, showMenu);
                    menu.setTags(getMenuTag(id));
                    menus.add(menu);
                }
                return menus;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Menu> findRestaurantMenu(int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("Menu", "restaurantId"))) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Menu> menus = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    Date startDate = rs.getDate("startTime");
                    Date endDate = rs.getDate("endTime");
                    boolean showMenu = rs.getBoolean("showMenu");
                    Menu menu = new Menu(id, restaurantId, title, startDate, endDate, showMenu);
                    menu.setTags(getMenuTag(id));
                    menus.add(menu);
                }
                return menus;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Menu findById(int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryById("Menu"))) {
            stmt.setInt(1, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int restaurantId = rs.getInt("restaurantId");
                    String title = rs.getString("title");
                    Date startDate = rs.getDate("startTime");
                    Date endDate = rs.getDate("endTime");
                    boolean showMenu = rs.getBoolean("showMenu");
                    Menu menu = new Menu(menuId, restaurantId, title, startDate, endDate, showMenu);
                    menu.setTags(getMenuTag(menuId));
                    return menu;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
