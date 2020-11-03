package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class PhotosResponse {

    @SerializedName("data")
    private DataForPhotos[] dataForPhotos;
    private Links links;
    private Meta meta;

    public DataForPhotos[] getDataForPhotos() {
        return dataForPhotos;
    }

    public void setDataForPhotos(DataForPhotos[] dataForPhotos) {
        this.dataForPhotos = dataForPhotos;
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
