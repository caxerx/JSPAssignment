package com.caxerx.db;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SqlBuilder {
    public static String queryById(String table) {
        return String.format("SELECT * FROM %s WHERE id = ?", table);
    }

    public static String queryByColumn(String table, String column) {
        return String.format("SELECT * FROM %s WHERE " + column + " = ?", table);
    }

    public static String queryByColumn(String table, Collection<String> columns) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(table).append(" WHERE ");
        String query = columns.stream().map(column -> column + " = ?").collect(Collectors.joining(" AND "));
        sb.append(query);
        return sb.toString();
    }
}
