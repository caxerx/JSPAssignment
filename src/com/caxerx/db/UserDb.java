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

    public void replacePermission(int role, ArrayList<Integer> permissions) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("RolePermission", "roleId"))) {
            stmt.setInt(1, role);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i : permissions) {
            addPermission(role, i);
        }
    }


    public void addPermission(int role, int permission) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("RolePermission", 3))) {
            stmt.setInt(1, 0);
            stmt.setInt(2, role);
            stmt.setInt(3, permission);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getAllPermission(int roleId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("RolePermission", "roleId"))) {
            stmt.setInt(1, roleId);
            List<Integer> perms = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    perms.add(rs.getInt("permission"));
                }
                return perms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Role> getAllRole() {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(SqlBuilder.queryAll("Role"))) {
            try (ResultSet rs = statement.executeQuery()) {
                List<Role> roles = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Role role = new Role(id, name);
                    role.setPermission(getAllPermission(id));
                    roles.add(role);
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
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    Date dateOfBirth = resultSet.getDate("dateOfBirth");
                    int type = resultSet.getInt("type");

                    User user = new User(id, username, null, firstName, lastName, email, dateOfBirth, type);

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(AddUserRequest request) {
        boolean changePw = false;
        if (request.getPassword() != null && !request.getPassword().equalsIgnoreCase("")) {
            changePw = true;
        }
        String sql = "";
        if (changePw) {
            sql = SqlBuilder.updateByColumn("User", "username", "password", "firstName", "lastName", "email", "dateOfBirth", "type");
        } else {
            sql = SqlBuilder.updateByColumn("User", "username", "firstName", "lastName", "email", "dateOfBirth", "type");
        }
        sql += " WHERE `User`.`id` = ?";
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            if (changePw) {
                statement.setString(1, request.getUsername());
                statement.setString(2, request.getPassword());
                statement.setString(3, request.getFirstName());
                statement.setString(4, request.getLastName());
                statement.setString(5, request.getEmail());
                statement.setDate(6, new java.sql.Date(request.getDob()));
                statement.setInt(7, request.getRole());
                statement.setInt(8, request.getId());
            } else {
                statement.setString(1, request.getUsername());
                statement.setString(2, request.getFirstName());
                statement.setString(3, request.getLastName());
                statement.setString(4, request.getEmail());
                statement.setDate(5, new java.sql.Date(request.getDob()));
                statement.setInt(6, request.getRole());
                statement.setInt(7, request.getId());
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    User user = new User(obj_id, obj_username, obj_password, obj_firstName, obj_lastName, obj_email, obj_dateOfBirth, obj_type);
                    user.setPermission(getPermissions(user.getId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Integer> getPermissions(int userId) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM `User` LEFT JOIN `Role` ON User.type = Role.id LEFT JOIN rolepermission ON Role.id = rolepermission.roleId WHERE User.id = ?")) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Integer> prm = new ArrayList<>();
                while (rs.next()) {
                    prm.add(rs.getInt("permission"));
                }
                return prm;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasPermission(int userId, int permission) {
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM `User` LEFT JOIN `Role` ON User.type = Role.id LEFT JOIN `rolepermission` ON Role.id = rolepermission.roleId WHERE User.id = ? AND rolepermission.permission IS NOT NULL")) {
            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    if (rs.getInt("permission") == permission) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void delete(int userId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.deleteByColumn("User", "id"))) {
            stmt.setInt(1, userId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
