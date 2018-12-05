package com.caxerx.db;

import com.caxerx.bean.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDb {

    private DatabaseConnectionPool pool;

    public TagDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }

    public List<Tag> findAll() {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryAll("Tag"))) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Tag> tags = new ArrayList<>();
                while (rs.next()) {
                    Tag tag = new Tag(rs.getInt("id"), rs.getString("name"));
                    tags.add(tag);
                }
                return tags;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
