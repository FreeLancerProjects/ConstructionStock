package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
    private int current_page;

    public List<NotificationModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public class NotificationModel implements Serializable
    {
        private int id;
        private int from_id;
        private int to_id;
        private int order_id;
        private int offer_id;
        private int notification_date;
        private int action_type;
        private int status;
        private double offer_value;
        private String company_name;
        private String company_image;
        private String company_phone;

        public int getId() {
            return id;
        }

        public int getFrom_id() {
            return from_id;
        }

        public int getTo_id() {
            return to_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public int getOffer_id() {
            return offer_id;
        }

        public int getNotification_date() {
            return notification_date;
        }

        public int getAction_type() {
            return action_type;
        }

        public int getStatus() {
            return status;
        }

        public double getOffer_value() {
            return offer_value;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getCompany_phone() {
            return company_phone;
        }

        public String getCompany_image() {
            return company_image;
        }
    }

}
