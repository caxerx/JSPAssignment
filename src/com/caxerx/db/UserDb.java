package com.caxerx.db;


import com.caxerx.bean.Restaurant;
import com.caxerx.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserDb {

    private DatabaseConnectionPool pool;

    public UserDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public User findById(int id) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryById("User"))) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    Date dateOfBirth = resultSet.getDate("dateOfBirth");
                    int type = resultSet.getInt("type");

                    User user = new User(id, username, password, firstName, lastName, email, dateOfBirth, type);

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User getValidUser(String username, String password) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryByColumn("User", "username", "password"))) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int obj_id = resultSet.getInt("id");
                    String obj_username = resultSet.getString("username");
                    String obj_password = resultSet.getString("password");
                    String obj_firstName = resultSet.getString("firstName");
                    String obj_lastName = resultSet.getString("lastName");
                    String obj_email = resultSet.getString("email");
                    Date obj_dateOfBirth = resultSet.getDate("dateOfBirth");
                    int obj_type = resultSet.getInt("type");
                    return new User(obj_id, obj_username, obj_password, obj_firstName, obj_lastName, obj_email, obj_dateOfBirth, obj_type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
