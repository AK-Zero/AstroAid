package com.example.astroassistant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ivl_response {

    @SerializedName("collection")
    private Collec_obj collection;

    public Collec_obj getCollectio() {
        return collection;
    }

    public class Collec_obj{
        @SerializedName("items")
        private List<item_obj> item;

        public List<item_obj> getItem() {
            return item;
        }

        public class item_obj{
            @SerializedName("data")
            private List<data_obj> data;

            public List<data_obj> getData() {
                return data;
            }

            public class data_obj{
                @SerializedName("description")
                private String description;

                @SerializedName("media_type")
                private String media_type;

                @SerializedName("title")
                private String title;

                @SerializedName("nasa_id")
                private String nasa_id;

                public String getDescription() {
                    return description;
                }

                public String getMedia_type() {
                    return media_type;
                }

                public String getTitle() {
                    return title;
                }

                public String getNasa_id() {
                    return nasa_id;
                }
            }

        }

    }
}
