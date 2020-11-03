package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class ThanaResponse {

    @SerializedName("data")
    private ThanaData[] thanaData;

    public ThanaData[] getThanaData() {
        return thanaData;
    }

    public void setThanaData(ThanaData[] thanaData) {
        this.thanaData = thanaData;
    }
}
