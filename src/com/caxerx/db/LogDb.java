package com.caxerx.db;

import java.sql.*;
import java.util.HashMap;

public class LogDb {
    private final DatabaseConnectionPool pool;

    public LogDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    //	id	type	keyword	tag	district	timestamp
    public void logSearch(String type, String keyword, String tag, String district) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("SearchLog", 6))) {
            stmt.setInt(1, 0);
            stmt.setString(2, type);
            stmt.setString(3, keyword == null ? "" : keyword);
            stmt.setString(4, tag == null ? "-1" : tag);
            stmt.setString(5, district == null ? "-1" : district);
            stmt.setNull(6, Types.TIMESTAMP);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logVisit(int resturantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("VisitLog", 3))) {
            stmt.setInt(1, 0);
            stmt.setInt(2, resturantId);
            stmt.setNull(3, Types.TIMESTAMP);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getVisitor(int restaurantId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM `visitlog` WHERE restaurantId = ?")) {
            stmt.setInt(1, restaurantId);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public HashMap<Long, String> getHistory() {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT keyword, timestamp from `searchlog` WHERE keyword !='' ")) {
            HashMap<Long, String> hist = new HashMap<>();
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hist.put(rs.getTimestamp("timestamp").getTime(), rs.getString("keyword"));
                }
                return hist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
