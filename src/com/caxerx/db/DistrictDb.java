package com.caxerx.db;

import com.caxerx.bean.District;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DistrictDb {

    private DatabaseConnectionPool pool;
    private static DistrictDb instance;


    private DistrictDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public static DistrictDb getInstance(DatabaseConnectionPool pool) {
        if (instance == null) {
            instance = new DistrictDb(pool);
        }
        return instance;
    }

    public District findById(int id) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryById("District"))) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    District district = new District(rs.getInt("id"), rs.getString("name"));
                    return district;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<District> findAll() {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryAll("District"))) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<District> districts = new ArrayList<>();
                while (rs.next()) {
                    District district = new District(rs.getInt("id"), rs.getString("name"));
                    districts.add(district);
                }
                return districts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
