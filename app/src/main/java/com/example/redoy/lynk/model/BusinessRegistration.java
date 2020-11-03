package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class BusinessRegistration {

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("password_confirmation")
    private String password_confirmation;

    @SerializedName("email")
    private String email;

    @SerializedName("verified_mobile")
    private String verified_mobile;

    @SerializedName("business_name")
    private String business_name;

    @SerializedName("business_description")
    private String business_description;

    @SerializedName("address")
    private String address;

    @SerializedName("category")
    private String category;

    @SerializedName("thana")
    private String thana;

    @SerializedName("city")
    private String city;

    @SerializedName("zip_code")
    private String zip_code;

    @SerializedName("country")
    private String country;

    @SerializedName("google_location")
    private String google_location;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("deal_titl")
    private String deal_title;

    public BusinessRegistration(String name, String password, String password_confirmation, String email, String verified_mobile, String business_name, String business_description, String address, String category, String thana, String city, String zip_code, String country, String google_location, String lat, String lng, String deal_title) {
        this.name = name;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.email = email;
        this.verified_mobile = verified_mobile;
        this.business_name = business_name;
        this.business_description = business_description;
        this.address = address;
        this.category = category;
        this.thana = thana;
        this.city = city;
        this.zip_code = zip_code;
        this.country = country;
        this.google_location = google_location;
        this.lat = lat;
        this.lng = lng;
        this.deal_title = deal_title;
    }
}
