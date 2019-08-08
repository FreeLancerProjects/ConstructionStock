package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categories_Model implements Serializable {
    private List<CData> data;
    public class CData implements Serializable {
        private int id;
        private String en_title;
        private String ar_title;
        private String created_at;
        private String updated_at;
        private String image;

        public int getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getImage() {
            return image;
        }

    }

    public List<CData> getData() {
        return data;
    }
}
