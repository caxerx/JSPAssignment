package com.caxerx.request;

import java.io.Serializable;
import java.util.ArrayList;

public class AddMenuRequest implements Serializable {
    private int restaurantId;
    private int menuId;
    private String menuName;
    private long startDate;
    private long endDate;
    private boolean showMenu;
    private ArrayList<Integer> tags;
    private ArrayList<Integer> images;

    public AddMenuRequest() {
    }

    public AddMenuRequest(int restaurantId, String menuName, long startDate, long endDate, boolean showMenu, ArrayList<Integer> tags, ArrayList<Integer> images) {
        this.restaurantId = restaurantId;
        this.menuName = menuName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.showMenu = showMenu;
        this.tags = tags;
        this.images = images;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public ArrayList<Integer> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Integer> tags) {
        this.tags = tags;
    }
}
