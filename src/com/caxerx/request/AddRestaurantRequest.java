package com.caxerx.request;

import java.util.ArrayList;

public class AddRestaurantRequest {
    private int background;
    private int logo;
    private String restaurantName;
    private ArrayList<Integer> tags;

    public AddRestaurantRequest() {
    }

    public AddRestaurantRequest(int background, int logo, String restaurantName, ArrayList<Integer> tags) {
        this.background = background;
        this.logo = logo;
        this.restaurantName = restaurantName;
        this.tags = tags;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public ArrayList<Integer> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Integer> tags) {
        this.tags = tags;
    }
}
