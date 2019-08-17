package com.creativeshare.constructionstock.models;

import java.io.Serializable;

public class NotificationStatusModel implements Serializable {

    private  int status;

    public NotificationStatusModel(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
