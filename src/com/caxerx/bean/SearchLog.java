package com.caxerx.bean;

import java.io.Serializable;

public class SearchLog implements Serializable {
    private int id;
    private int userId;
    private String keyword;

    private transient User user;

    public SearchLog() {
    }

    public SearchLog(int id, int userId, String keyword) {
        this.id = id;
        this.userId = userId;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
