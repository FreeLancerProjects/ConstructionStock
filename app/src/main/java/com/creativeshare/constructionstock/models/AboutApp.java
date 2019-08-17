package com.creativeshare.constructionstock.models;

import java.io.Serializable;

public class AboutApp implements Serializable {

    private AboutModel about;
    private TermsModel conditions;

    public AboutModel getAbout() {
        return about;
    }

    public TermsModel getConditions() {
        return conditions;
    }

    public class AboutModel implements Serializable
    {
        private String ar_content;
        private String en_content;

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }
    }

    public class TermsModel implements Serializable
    {
        private String ar_content;
        private String en_content;

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }
    }
}
