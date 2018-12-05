package com.caxerx.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Menu implements Serializable {
    private int id;
    private int restaurantId;
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean showMenu;

    private List<Integer> image;
    private Restaurant restaurant;
    private List<Tag> tags;

    public Menu() {
    }

    public Menu(int id, int restaurantId, String title, Date startTime, Date endTime, boolean showMenu) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.showMenu = showMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Integer> getImage() {
        return image;
    }

    public void setImage(List<Integer> image) {
        this.image = image;
    }
}
