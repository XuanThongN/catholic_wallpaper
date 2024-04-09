package com.xuanthongn.catholicwallpaper.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("results")
    private List<WallpaperResponse> results;

    public List<WallpaperResponse> getResults() {
        return results;
    }
}

