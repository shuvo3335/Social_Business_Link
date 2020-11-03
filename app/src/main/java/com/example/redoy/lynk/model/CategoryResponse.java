package com.example.redoy.lynk.model;

import com.google.gson.annotations.SerializedName;

public class CategoryResponse {

    @SerializedName("data")
    private CategoryData[] categoryData;

    public CategoryData[] getCategoryData() {
        return categoryData;
    }

    public void setThanaData(CategoryData[] categoryData) {
        this.categoryData = categoryData;
    }
}