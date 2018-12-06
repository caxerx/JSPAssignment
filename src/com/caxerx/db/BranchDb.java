package com.caxerx.db;

import com.caxerx.bean.*;
import com.caxerx.request.AddBranchRequest;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDb {
    private DatabaseConnectionPool pool;
    private RestaurantDb restaurantDb;
    private static BranchDb instance;

    public static BranchDb getInstance(DatabaseConnectionPool pool) {
        if (instance == null) {
            instance = new BranchDb(pool);
        }
        return instance;
    }


    private RestaurantDb getRestaurantDb() {
        if (restaurantDb == null) {
            restaurantDb = RestaurantDb.getInstance(pool);
        }
        return restaurantDb;
    }


    private BranchDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public int insert(int userId, int restaurantId, AddBranchRequest request) {
        Restaurant restaurant = getRestaurantDb().findById(restaurantId);
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


    public List<District> getDeliveryDistrict(int branchId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumnWithJoin("BranchDeliveryDistrict", "INNER JOIN District ON BranchDeliveryDistrict.districtId = District.id", "branchId"))) {
            stmt.setInt(1, branchId);
            List<District> districts = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int district = rs.getInt("districtId");
                    String tagName = rs.getString("name");
                    districts.add(new District(district, tagName));
                }
                return districts;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Branch> findAll() {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryAll("Branch"))) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Branch> branches = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int restaurantId = rs.getInt("restaurantId");
                    String name = rs.getString("name");
                    int districtId = rs.getInt("districtId");
                    String address = rs.getString("address");
                    String telephone = rs.getString("telephone");
                    String openTime = rs.getString("openTime");

                    Branch branch = new Branch(id, restaurantId, districtId, name, address, telephone, openTime);
                    branch.setDeliveryDistrict(getDeliveryDistrict(id));
                    branches.add(branch);
                }
                return branches;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Branch> findRestaurantBranch(int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryByColumn("Branch", "restaurantId"))) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Branch> branches = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int districtId = rs.getInt("districtId");
                    String address = rs.getString("address");
                    String telephone = rs.getString("telephone");
                    String openTime = rs.getString("openTime");

                    Branch branch = new Branch(id, restaurantId, districtId, name, address, telephone, openTime);
                    branch.setDeliveryDistrict(getDeliveryDistrict(id));
                    branches.add(branch);
                }
                return branches;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Branch findById(int id) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryById("Branch"))) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int restaurantId = rs.getInt("restaurantId");
                    String name = rs.getString("name");
                    int districtId = rs.getInt("districtId");
                    String address = rs.getString("address");
                    String telephone = rs.getString("telephone");
                    String openTime = rs.getString("openTime");

                    Branch branch = new Branch(id, restaurantId, districtId, name, address, telephone, openTime);
                    branch.setDeliveryDistrict(getDeliveryDistrict(id));
                    return branch;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
