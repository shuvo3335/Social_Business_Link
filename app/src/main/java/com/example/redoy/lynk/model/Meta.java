package com.example.redoy.lynk.model;

public class Meta {

    private String total;
    private String to;
    private String last_page;
    private String per_page;
    private String path;
    private String from;
    private String current_page;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    @Override
    public String toString() {
        return "ClassPojo [total = " + total + ", to = " + to + ", last_page = " + last_page + ", per_page = " + per_page + ", path = " + path + ", from = " + from + ", current_page = " + current_page + "]";
    }

}
