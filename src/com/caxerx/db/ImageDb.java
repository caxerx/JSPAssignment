package com.caxerx.db;


import com.caxerx.bean.Image;
import com.caxerx.bean.User;

import java.io.InputStream;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class ImageDb {

    private DatabaseConnectionPool pool;

    public ImageDb(DatabaseConnectionPool pool) {
        this.pool = pool;
    }


    public Image getImage(int imageId) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.queryById("Image"))) {
            stmt.setInt(1, imageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                Blob img = rs.getBlob("image");
                return new Image(imageId, img, type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int uploadImage(InputStream img, String type) {
        try (Connection conn = pool.getConnection(); PreparedStatement stmt = conn.prepareStatement(SqlBuilder.insert("Image", 3), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, 0);
            stmt.setBlob(2, img);
            stmt.setString(3, type);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -2;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -3;
        }
    }
}
