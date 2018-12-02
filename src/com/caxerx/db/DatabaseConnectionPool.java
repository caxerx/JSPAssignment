package com.caxerx.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    private static DatabaseConnectionPool instance;

    private HikariDataSource dataSource;

    private DatabaseConnectionPool(HikariConfig config) {
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public static DatabaseConnectionPool contextInit(ServletContext context) {
        if (instance == null) {
            String host = context.getInitParameter("database-host");
            String port = context.getInitParameter("database-port");
            String username = context.getInitParameter("database-username");
            String password = context.getInitParameter("database-password");
            String database = context.getInitParameter("database-db");
            instance = createSimpleInstance(host, port, username, password, database);
        }
        context.setAttribute("sqlPool", instance);
        return instance;
    }

    public static DatabaseConnectionPool createSimpleInstance(String host, String port, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        return new DatabaseConnectionPool(config);
    }
}