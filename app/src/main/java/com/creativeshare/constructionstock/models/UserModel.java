package com.creativeshare.constructionstock.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String username;
    private String phone_code;
    private String phone;
    private String email;
    private String image;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
