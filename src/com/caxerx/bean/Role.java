package com.caxerx.bean;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {
    private int id;
    private String name;
    private List<Integer> permission;

    public Role() {
    }

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPermission() {
        return permission;
    }

    public void setPermission(List<Integer> permission) {
        this.permission = permission;
    }
}
