package com.xuanthongn.catholicwallpaper.models;

public class Wallpaper {

    private String url;
    private String description;

    public Wallpaper(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

}

