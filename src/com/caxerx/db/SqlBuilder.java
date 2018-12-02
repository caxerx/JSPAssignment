package com.caxerx.db;

import java.util.Collection;
import java.util.List;

public class SqlBuilder {
    public static String queryById(String table) {
        return String.format("SELECT * FROM %s WHERE id = ?", table);
    }

    public static String queryByColumn(String table, String column) {
        return String.format("SELECT * FROM %s WHERE " + column + " = ?", table);
    }

    public static String queryByColumn(String table, List<String> columns) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE ");
        for (int i = 0; i < columns.size(); i++) {
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(columns.get(i)).append(" = ? ");
        }

        return sb.toString();
    }
}
