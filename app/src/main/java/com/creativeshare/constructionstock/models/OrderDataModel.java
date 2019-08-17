package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private int current_page;
    private List<OrderModel> data;

    public List<OrderModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class OrderModel implements Serializable
    {
        private int id;
        private int user_id;
        private int company_id;
        private int status;
        private int offer_id;
        private String company_name;
        private String company_phone;
        private String company_image;
        private double offer_value;
        private List<OrderDetails> order_details;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public int getStatus() {
            return status;
        }

        public int getOffer_id() {
            return offer_id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getCompany_phone() {
            return company_phone;
        }

        public double getOffer_value() {
            return offer_value;
        }

        public List<OrderDetails> getOrder_details() {
            return order_details;
        }

        public String getCompany_image() {
            return company_image;
        }
    }

    public class OrderDetails implements Serializable
    {

        private int id;
        private int order_id;
        private int cat_id;
        private int sub_id;
        private double amount;
        private String cat_en_title;
        private String cat_ar_title;
        private String sub_image;
        private String sub_ar_title;
        private String sub_en_title;
        private double offer_value;

        public int getId() {
            return id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getCat_id() {
            return cat_id;
        }

        public int getSub_id() {
            return sub_id;
        }

        public double getAmount() {
            return amount;
        }

        public String getCat_en_title() {
            return cat_en_title;
        }

        public String getCat_ar_title() {
            return cat_ar_title;
        }

        public String getSub_image() {
            return sub_image;
        }

        public String getSub_ar_title() {
            return sub_ar_title;
        }

        public String getSub_en_title() {
            return sub_en_title;
        }

        public double getOffer_value() {
            return offer_value;
        }
    }

}
