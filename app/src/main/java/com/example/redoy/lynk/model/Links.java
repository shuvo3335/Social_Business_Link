package com.example.redoy.lynk.model;

public class Links {

    private String last;
    private String next;
    private String first;
    private String prev;

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "ClassPojo [last = " + last + ", next = " + next + ", first = " + first + ", prev = " + prev + "]";
    }

}
