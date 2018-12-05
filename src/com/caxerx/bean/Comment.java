package com.caxerx.bean;

import java.io.Serializable;

public class Comment implements Serializable {
    private int id;
    private int userId;
    private int restaurantId;
    private int foodQuality;
    private int serviceQuality;
    private String comment;

    private transient User user;

    public Comment() {
    }

    public Comment(int id, int userId, int restaurantId, int foodQuality, int serviceQuality, String comment) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.foodQuality = foodQuality;
        this.serviceQuality = serviceQuality;
        this.comment = comment;
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

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getFoodQuality() {
        return foodQuality;
    }

    public void setFoodQuality(int foodQuality) {
        this.foodQuality = foodQuality;
    }

    public int getServiceQuality() {
        return serviceQuality;
    }

    public void setServiceQuality(int serviceQuality) {
        this.serviceQuality = serviceQuality;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
