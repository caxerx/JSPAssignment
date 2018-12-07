package com.caxerx.db;

import java.util.Collection;
import java.util.List;

public class SqlBuilder {
    public static String queryAll(String table) {
        return String.format("SELECT * FROM `%s`", table);
    }

    public static String queryAllWithJoin(String table, String joinText) {
        return String.format("SELECT * FROM `%s` %s", table, joinText);
    }

    public static String queryById(String table) {
        return String.format("SELECT * FROM `%s` WHERE id = ?", table);
    }

    public static String queryByColumn(String table, String column) {
        return String.format("SELECT * FROM `%s` WHERE " + column + " = ?", table);
    }

    public static String queryByColumn(String table, String... columns) {
        StringBuilder sb = new StringBuilder("SELECT * FROM `").append(table).append("` WHERE ");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(columns[i]).append(" = ? ");
        }

        return sb.toString();
    }

    public static String updateByColumn(String table, String... columns) {
        StringBuilder sb = new StringBuilder("UPDATE `").append(table).append("` SET ");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("`").append(columns[i]).append("`").append(" = ? ");
        }

        return sb.toString();
    }

    public static String queryByColumnWithJoin(String table, String joinText, String... columns) {
        StringBuilder sb = new StringBuilder("SELECT * FROM `").append(table).append("` ").append(joinText).append(" WHERE ");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(columns[i]).append(" = ? ");
        }

        return sb.toString();
    }

    public static String insert(String table, int column) {
        StringBuilder sb = new StringBuilder("INSERT INTO `").append(table).append("` VALUES(");
        for (int i = 0; i < column; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");

        return sb.toString();
    }

    public static String deleteByColumn(String table, String column) {
        return String.format("DELETE FROM `%s` WHERE " + column + " = ?", table);
    }

    public static String deleteByColumn(String table, String... columns) {
        StringBuilder sb = new StringBuilder("DELETE FROM `").append(table).append("` WHERE ");
        for (int i = 0; i < columns.length; i++) {
            if (i != 0) {
                sb.append(" AND ");
            }
            sb.append(columns[i]).append(" = ? ");
        }

        return sb.toString();
    }

}
