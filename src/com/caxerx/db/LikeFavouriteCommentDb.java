package com.caxerx.db;

import com.caxerx.bean.Comment;
import com.caxerx.bean.Menu;
import com.caxerx.bean.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeFavouriteCommentDb {
    private final DatabaseConnectionPool pool;
    private final UserDb userDb;
    private RestaurantDb restaurantDb;
    private MenuDb menuDb;
    private static LikeFavouriteCommentDb instance;

    private LikeFavouriteCommentDb(DatabaseConnectionPool pool) {
        this.pool = pool;
        userDb = new UserDb(pool);
    }

    public static LikeFavouriteCommentDb getInstance(DatabaseConnectionPool pool) {
        if (instance == null) {
            instance = new LikeFavouriteCommentDb(pool);
        }
        return instance;
    }

    public RestaurantDb getRestaurantDb() {
        if (restaurantDb == null) {
            restaurantDb = RestaurantDb.getInstance(pool);
        }
        return restaurantDb;
    }


    public MenuDb getMenuDb() {
        if (menuDb == null) {
            menuDb = MenuDb.getInstance(pool);
        }
        return menuDb;
    }


    public void favouriteRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("FavouriteRestaurant", 2))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFavouriteRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("FavouriteRestaurant", "userId", "restaurantId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isFavouriteRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("FavouriteRestaurant", "userId", "restaurantId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void likeRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Like", 2))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isLikedRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("Like", "userId", "restaurantId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeLikedRestaurant(int user, int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("Like", "userId", "restaurantId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, restaurantId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void favouriteMenu(int user, int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("FavouriteMenu", 2))) {
            stmt.setInt(1, user);
            stmt.setInt(2, menuId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isFavouriteMenu(int user, int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("FavouriteMenu", "userId", "menuId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, menuId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void removeFavouriteMenu(int user, int menuId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("FavouriteMenu", "userId", "menuId"))) {
            stmt.setInt(1, user);
            stmt.setInt(2, menuId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commentRestaurant(int user, int restaurant, String comment, int rating) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Comment", 6))) {
            stmt.setInt(1, 0);
            stmt.setInt(2, user);
            stmt.setInt(3, restaurant);
            stmt.setString(4, comment);
            stmt.setInt(5, rating);
            stmt.setNull(6, Types.TIMESTAMP);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comment> getRestaurantComment(int restaurant) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("Comment", "restaurantId"))) {
            stmt.setInt(1, restaurant);
            try (ResultSet rs = stmt.executeQuery()) {
                ArrayList<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    Comment comment = new Comment();
                    //id userId restaurantId comment rating
                    String cmt = rs.getString("comment");
                    int userId = rs.getInt("userId");
                    int rating = rs.getInt("rating");
                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    comment.setComment(cmt);
                    comment.setUserId(userId);
                    comment.setRating(rating);
                    comment.setTimestamp(timestamp.getTime());
                    comment.setUser(userDb.findById(userId));
                    comments.add(comment);
                }
                return comments;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Restaurant> getFavouriteRestaurant(int userId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("FavouriteRestaurant", "userId"))) {
            stmt.setInt(1, userId);
            List<Restaurant> restaurants = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(getRestaurantDb().findById(rs.getInt("restaurantId")));
                }
            }
            return restaurants;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Menu> getFavouriteMenu(int userId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("FavouriteMenu", "userId"))) {
            stmt.setInt(1, userId);
            List<Menu> menus = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Menu menu = getMenuDb().findById(rs.getInt("menuId"));
                    Restaurant r = getRestaurantDb().findById(menu.getRestaurantId());
                    menu.setRestaurant(r);
                    menus.add(menu);
                }
            }
            return menus;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
