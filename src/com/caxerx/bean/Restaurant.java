package com.caxerx.bean;

import java.util.List;

public class Restaurant {
    private int id;
    private int owner;
    private String name;
    private int logo;
    private int background;

    //association
    private List<Tag> tags;
    private List<Menu> menus;
    private List<Comment> comments;
    private List<Branch> branchs;
    private List<User> likedUser;

    public Restaurant() {
    }

    public Restaurant(int id, int owner, String name, int logo, int background) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.logo = logo;
        this.background = background;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Branch> getBranchs() {
        return branchs;
    }

    public void setBranchs(List<Branch> branchs) {
        this.branchs = branchs;
    }

    public List<User> getLikedUser() {
        return likedUser;
    }

    public void setLikedUser(List<User> likedUser) {
        this.likedUser = likedUser;
    }
}
