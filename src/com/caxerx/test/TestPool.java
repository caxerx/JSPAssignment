package com.caxerx.test;

import com.caxerx.db.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestPool {
    public static void main(String[] args) {
        DatabaseConnectionPool pool = DatabaseConnectionPool.createSimpleInstance("localhost", "3306", "caxerx_lab", "ABZy1DV1AkAK0yY9", "iamsofat");
        try (Connection connection = pool.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM test")) {
            System.out.println(statement.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
