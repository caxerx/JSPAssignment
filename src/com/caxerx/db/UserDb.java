package com.caxerx.db;


import com.caxerx.bean.Restaurant;
import com.caxerx.bean.Role;
import com.caxerx.bean.User;
import com.caxerx.request.AddUserRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserDb {

    private DatabaseConnectionPool pool;

    public UserDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public List<Role> getAllRole() {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryAll("Role"))) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Role> roles = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    roles.add(new Role(id, name));
                }
                return roles;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUser() {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryAllWithJoin("User", " INNER JOIN Role ON User.type = Role.id"))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<User> users = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    Date dateOfBirth = resultSet.getDate("dateOfBirth");
                    int type = resultSet.getInt("type");
                    String roleName = resultSet.getString("name");
                    User user = new User(id, username, null, firstName, lastName, email, dateOfBirth, type);
                    user.setRole(new Role(type, roleName));
                    users.add(user);
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public void insert(AddUserRequest user) {
        try (Connection connection = pool.getConnection(); PreparedStatement stmt = connection.prepareStatement(SqlBuilder.insert("User", 8))) {
            stmt.setInt(1, 0);
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getEmail());
            stmt.setDate(7, new java.sql.Date(user.getDob()));
            stmt.setInt(8, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
