package com.caxerx.db;

import java.util.List;

public interface DatabaseHelper<T> {
    T findById(int id);

    List<T> findByColumn(String column, String value);

    int update(T item);

    int create(T item);

    int delete(int id);
}
