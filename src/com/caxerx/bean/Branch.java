package com.caxerx.bean;

import java.util.List;

public class Branch {
    int id;
    int restaurant;
    String address;
    String telephone;
    String openTime;
    District district;
    List<District> deliveryDistrict;
}
