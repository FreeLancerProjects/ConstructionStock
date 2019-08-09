package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.List;

public class ItemCartUploadModel implements Serializable {
    private int user_id;
    private String address;
    private double latitude;
    private double longitude;
    private String arrival_time;
    private String responsible_phone;
    private List<ItemCartModel> order_details;

    public ItemCartUploadModel(int user_id, String address, double latitude, double longitude, String arrival_time, String responsible_phone, List<ItemCartModel> order_details) {
        this.user_id = user_id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.arrival_time = arrival_time;
        this.responsible_phone = responsible_phone;
        this.order_details = order_details;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getResponsible_phone() {
        return responsible_phone;
    }

    public void setResponsible_phone(String responsible_phone) {
        this.responsible_phone = responsible_phone;
    }

    public List<ItemCartModel> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<ItemCartModel> order_details) {
        this.order_details = order_details;
    }
}
