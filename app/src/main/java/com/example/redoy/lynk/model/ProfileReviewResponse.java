package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class ProfileReviewResponse {

    @SerializedName("data")
    private ProfileReviewData[] profileReviewData;

    private Links links;

    private Meta meta;

    public ProfileReviewData[] getProfileReviewData() {
        return profileReviewData;
    }

    public void setProfileReviewData(ProfileReviewData[] data) {
        this.profileReviewData = data;
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
