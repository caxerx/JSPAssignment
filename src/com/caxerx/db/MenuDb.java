package com.caxerx.db;

import com.caxerx.bean.Image;
import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;
import com.caxerx.bean.Tag;
import com.caxerx.request.AddMenuRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDb {
    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;
    private static MenuDb instance;

    private MenuDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public static MenuDb getInstance(DatabaseConnectionPool pool) {
        if (instance == null) {
            instance = new MenuDb(pool);
        }
        return instance;
    }

    public RestaurantDb getRestaurantDb() {
        if (restaurantDb == null) {
            restaurantDb = RestaurantDb.getInstance(pool);
        }
        return restaurantDb;
    }

    public void toggleVisibility(int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE `Menu` SET `showMenu` = !`showMenu` WHERE `Menu`.`id` = ?")) {
            stmt.setInt(1, menuId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insert(int userId, int restaurantId, AddMenuRequest request) {
        Restaurant restaurant = getRestaurantDb().findById(restaurantId);
        if (restaurant == null) {
            return -1;
        }
        if (restaurant.getOwner() != userId) {
            return -2;
        }
        try (Connection conn = pool.getConnection()) {
            int id = -1;
            try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Menu", 6), Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, 0);
                stmt.setInt(2, restaurantId);
                stmt.setString(3, request.getMenuName());
                stmt.setDate(4, new Date(request.getStartDate()));
                stmt.setDate(5, new Date(request.getEndDate()));
                stmt.setBoolean(6, request.isShowMenu());
                int rs = stmt.executeUpdate();
                if (rs > 0) {
                    ResultSet gK = stmt.getGeneratedKeys();
                    if (gK.next()) {
                        id = gK.getInt(1);
                    }
                }
            }

            for (int tag : request.getTags()) {
                insertTag(conn, id, tag);
            }

            for (int image : request.getImages()) {
                insertImage(conn, id, image);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }
        return 1;
    }


    public int update(int userId, int restaurantId, AddMenuRequest request) {
        Restaurant restaurant = getRestaurantDb().findById(restaurantId);
        if (restaurant == null) {
            return -1;
        }
        if (restaurant.getOwner() != userId) {
            return -2;
        }
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE Menu SET title = ?, startTime = ?, endTime = ?, showMenu = ? WHERE id = ?")) {
                stmt.setString(1, request.getMenuName());
                stmt.setDate(2, new Date(request.getStartDate()));
                stmt.setDate(3, new Date(request.getEndDate()));
                stmt.setBoolean(4, request.isShowMenu());
                stmt.setInt(5, request.getMenuId());
                int rs = stmt.executeUpdate();
            }

            int id = request.getMenuId();

            clearTag(conn, id);
            for (int tag : request.getTags()) {
                insertTag(conn, id, tag);
            }

            clearImage(conn, id);
            for (int image : request.getImages()) {
                insertImage(conn, id, image);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }
        return 1;
    }


    public void insertTag(Connection conn, int menuId, int tagId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("MenuTag", 2))) {
            stmt.setInt(1, menuId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();
        }
    }


    public void insertImage(Connection conn, int menuId, int imageId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("MenuImage", 2))) {
            stmt.setInt(1, menuId);
            stmt.setInt(2, imageId);
            stmt.executeUpdate();
        }
    }


    public void clearTag(Connection conn, int menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("MenuTag", "menuId"))) {
            stmt.setInt(1, menuId);
            stmt.executeUpdate();
        }
    }


    public void clearImage(Connection conn, int menuId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("MenuImage", "menuId"))) {
            stmt.setInt(1, menuId);
            stmt.executeUpdate();
        }
    }


    public void deleteMenu(int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("Menu", "id"))) {
            stmt.setInt(1, menuId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
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


    public List<Integer> getMenuImage(int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("MenuImage", "menuId"))) {
            stmt.setInt(1, menuId);
            List<Integer> images = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int imageId = rs.getInt("imageId");
                    images.add(imageId);
                }
                return images;
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
                    menu.setImage(getMenuImage(id));
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
                    menu.setImage(getMenuImage(id));
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
                    menu.setImage(getMenuImage(menuId));
                    return menu;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

