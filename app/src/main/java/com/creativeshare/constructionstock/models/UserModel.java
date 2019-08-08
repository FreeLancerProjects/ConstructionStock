package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private User user;

    public class User implements Serializable
    {   private int id;
        private String username;
        private String phone_code;
        private String phone;
        private String email;
        private String software_type;



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

        public String getSoftware_type() {
            return software_type;
        }
    }

    public User getUser() {
        return user;
    }
}
