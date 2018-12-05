package com.caxerx.db;

import com.caxerx.bean.Restaurant;
import com.caxerx.bean.Tag;
import com.caxerx.request.AddRestaurantRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantDb {
    private DatabaseConnectionPool pool;

    public RestaurantDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public List<Restaurant> findAll() {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryAll("Restaurant"))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Restaurant> restaurants = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int owner = resultSet.getInt("owner");
                    int logo = resultSet.getInt("logo");
                    int background = resultSet.getInt("background");
                    Restaurant restaurant = new Restaurant(id, owner, name, logo, background);
                    restaurant.setTags(getRestaurantTag(restaurant.getId()));
                    restaurants.add(restaurant);
                }
                return restaurants;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Restaurant> findOwnedRestaurant(int ownerId) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryByColumn("Restaurant", "owner"))) {
            statement.setInt(1, ownerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Restaurant> restaurants = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int owner = resultSet.getInt("owner");
                    int logo = resultSet.getInt("logo");
                    int background = resultSet.getInt("background");
                    Restaurant restaurant = new Restaurant(id, owner, name, logo, background);
                    restaurant.setTags(getRestaurantTag(restaurant.getId()));
                    restaurants.add(restaurant);
                }
                return restaurants;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Restaurant findById(int id) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryById("Restaurant"))) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int owner = resultSet.getInt("owner");
                    int logo = resultSet.getInt("logo");
                    int background = resultSet.getInt("background");
                    Restaurant restaurant = new Restaurant(id, owner, name, logo, background);
                    restaurant.setTags(getRestaurantTag(restaurant.getId()));
                    return restaurant;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(int owner, AddRestaurantRequest restaurantRequest) {
        restaurantRequest.getRestaurantName();
        int restaurantId;
        try (Connection conn = pool.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Restaurant", 5), Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, 0);
                stmt.setInt(2, owner);
                stmt.setString(3, restaurantRequest.getRestaurantName());
                stmt.setInt(4, restaurantRequest.getLogo());
                stmt.setInt(5, restaurantRequest.getBackground());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        restaurantId = generatedKeys.getInt(1);
                    } else {
                        return false;
                    }
                }
            }

            if (restaurantId > 0) {
                for (int i : restaurantRequest.getTags()) {
                    insertTag(conn, restaurantId, i);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void insertTag(Connection conn, int restaurantId, int tagId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("RestaurantTag", 2))) {
            stmt.setInt(1, restaurantId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();
        }
    }

    public List<Tag> getRestaurantTag(int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumnWithJoin("RestaurantTag", "INNER JOIN Tag ON RestaurantTag.tagId = Tag.id", "restaurantId"))) {
            stmt.setInt(1, restaurantId);
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


}
