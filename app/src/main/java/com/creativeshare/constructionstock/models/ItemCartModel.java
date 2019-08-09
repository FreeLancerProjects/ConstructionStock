package com.creativeshare.constructionstock.models;

import java.io.Serializable;

public class ItemCartModel implements Serializable {
    private int id;
    private String ar_name;
    private String en_name;
    private String image;
    private int cat_id;
    private int sub_id;
    private String amount;

    public ItemCartModel(int id, String ar_name, String en_name, String image, int cat_id, int sub_id, String amount) {
        this.id = id;
        this.ar_name = ar_name;
        this.en_name = en_name;
        this.image = image;
        this.cat_id = cat_id;
        this.sub_id = sub_id;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAr_name() {
        return ar_name;
    }

    public void setAr_name(String ar_name) {
        this.ar_name = ar_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
