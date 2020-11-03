package com.example.redoy.lynk.model;

public class Review {

    private String id;
    private Updated_at updated_at;
    private String review_by;
    private Created_at created_at;
    private String rated_by_user;
    private String reviewable_type;
    private String user_id;
    private String reviewable_id;
    private String review_body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Updated_at getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Updated_at updated_at) {
        this.updated_at = updated_at;
    }

    public String getReview_by() {
        return review_by;
    }

    public void setReview_by(String review_by) {
        this.review_by = review_by;
    }

    public Created_at getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Created_at created_at) {
        this.created_at = created_at;
    }

    public String getRated_by_user() {
        return rated_by_user;
    }

    public void setRated_by_user(String rated_by_user) {
        this.rated_by_user = rated_by_user;
    }

    public String getReviewable_type() {
        return reviewable_type;
    }

    public void setReviewable_type(String reviewable_type) {
        this.reviewable_type = reviewable_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReviewable_id() {
        return reviewable_id;
    }

    public void setReviewable_id(String reviewable_id) {
        this.reviewable_id = reviewable_id;
    }

    public String getReview_body() {
        return review_body;
    }

    public void setReview_body(String review_body) {
        this.review_body = review_body;
    }
}
