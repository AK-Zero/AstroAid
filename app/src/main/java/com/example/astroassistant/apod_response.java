package com.example.astroassistant;

import com.google.gson.annotations.SerializedName;

public class apod_response {

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("media_type")
    private String media_type;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public String getExplanation() {
        return explanation;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }



}
