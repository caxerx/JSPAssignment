package com.caxerx.bean;

import java.io.Serializable;
import java.util.List;

public class Branch implements Serializable {
    private int id;
    private int restaurantId;
    private int districtId;
    private String address;
    private String telephone;
    private String openTime;


    private transient District district;
    private transient Restaurant restaurant;
    private transient List<District> deliveryDistrict;

    public Branch() {
    }

    public Branch(int id, int restaurantId, int districtId, String address, String telephone, String openTime) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.districtId = districtId;
        this.address = address;
        this.telephone = telephone;
        this.openTime = openTime;
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

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<District> getDeliveryDistrict() {
        return deliveryDistrict;
    }

    public void setDeliveryDistrict(List<District> deliveryDistrict) {
        this.deliveryDistrict = deliveryDistrict;
    }
}
