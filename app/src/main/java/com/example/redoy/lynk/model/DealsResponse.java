package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class DealsResponse {

    @SerializedName("data")
    private DataForDeals dataForDeals;

    public DataForDeals getDataForDeals() {
        return dataForDeals;
    }

    public void setDataForDeals(DataForDeals dataForDeals) {
        this.dataForDeals = dataForDeals;
    }
}
