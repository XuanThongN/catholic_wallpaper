package com.xuanthongn.catholicwallpaper.models;

import com.google.gson.annotations.SerializedName;

public class WallpaperResponse {

    @SerializedName("urls")
    private Urls url;
    @SerializedName("alt_description")
    private String description;

    public WallpaperResponse(Urls url, String description) {
        this.url = url;
        this.description = description;
    }

    public Urls getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

}

