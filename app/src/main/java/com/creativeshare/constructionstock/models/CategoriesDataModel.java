package com.creativeshare.constructionstock.models;

import java.io.Serializable;
import java.util.List;

public class CategoriesDataModel implements Serializable {
    private List<CategoryModel> data;
    public class CategoryModel implements Serializable {
        private int id;
        private String en_title;
        private String ar_title;
        private String image;
        private List<SubCategory> sub_categories;


        public int getId() {
            return id;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getAr_title() {
            return ar_title;
        }


        public String getImage() {
            return image;
        }

        public List<SubCategory> getSub_categories() {
            return sub_categories;
        }
    }
    public class SubCategory implements Serializable
    {
        private int id;
        private int cat_id;
        private String ar_title;
        private String en_title;
        private String image;

        public int getId() {
            return id;
        }

        public int getCat_id() {
            return cat_id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }

        public String getImage() {
            return image;
        }
    }
    public List<CategoryModel> getData() {
        return data;
    }

}
