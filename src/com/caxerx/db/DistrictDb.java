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

    public DistrictDb(DatabaseConnectionPool pool) {
        this.pool = pool;
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
