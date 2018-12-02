package com.caxerx.db;


import com.caxerx.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDb {
    private static final String TABLE_NAME = "User";

    private DatabaseConnectionPool pool;

    public UserDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public User findById(int id) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryById(TABLE_NAME))) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String type = resultSet.getString("type");
                    return new User(id, username, password, type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getValidUser(String username, String password) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryByColumn(TABLE_NAME, Arrays.asList("username", "password")))) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int obj_id = resultSet.getInt("id");
                    String obj_username = resultSet.getString("username");
                    String obj_password = resultSet.getString("password");
                    String obj_type = resultSet.getString("type");
                    return new User(obj_id, obj_username, obj_password, obj_type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
