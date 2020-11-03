package com.example.redoy.lynk.model;

public class ReviewItem {

    private String name;
    private String timestamp;
    private String description;
    private String ratedByUser;

    public ReviewItem(String name, String timestamp, String description, String ratedByUser) {
        this.name = name;
        this.timestamp = timestamp;
        this.description = description;
        this.ratedByUser = ratedByUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRatedByUser() {
        return ratedByUser;
    }

    public void setRatedByUser(String ratedByUser) {
        this.ratedByUser = ratedByUser;
    }
}
