package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class AllDealsResponse {

    @SerializedName("data")
    private DataForNotification[] dataForNotifications;
    private Links links;
    private Meta meta;

    public DataForNotification[] getDataForNotification() {
        return dataForNotifications;
    }

    public void setDataForNotification(DataForNotification[] dataForNotifications) {
        this.dataForNotifications = dataForNotifications;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
