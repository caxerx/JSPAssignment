package com.caxerx.request;

import java.io.Serializable;
import java.util.ArrayList;

public class AddBranchRequest implements Serializable {
    private int restaurantId;
    private int branchId;
    private int district;
    private String branchName;
    private String phoneNumber;
    private String openTime;
    private String closeTime;
    private String address;
    private ArrayList<Integer> deliveryDistrict;

    public AddBranchRequest() {
    }

    public AddBranchRequest(int restaurantId, int district, String branchName, String phoneNumber, String openTime, String closeTime, String address, ArrayList<Integer> deliveryDistrict) {
        this.restaurantId = restaurantId;
        this.district = district;
        this.branchName = branchName;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.address = address;
        this.deliveryDistrict = deliveryDistrict;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<Integer> getDeliveryDistrict() {
        return deliveryDistrict;
    }

    public void setDeliveryDistrict(ArrayList<Integer> deliveryDistrict) {
        this.deliveryDistrict = deliveryDistrict;
    }
}
