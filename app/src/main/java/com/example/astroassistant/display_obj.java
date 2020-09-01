package com.example.astroassistant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class display_obj {

    @SerializedName("collection")
    private Collec_obj1 collection;

    public Collec_obj1 getCollectio() {
        return collection;
    }

    public class Collec_obj1{
        @SerializedName("items")
        private List<item_obj1> item;

        public List<item_obj1> getItem() {
            return item;
        }

        public class item_obj1 {

            @SerializedName("href")
            String href;

            public String getHref() {
                return href;
            }
        }
        }
}
