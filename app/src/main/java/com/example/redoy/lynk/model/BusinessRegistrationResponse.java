package com.example.redoy.lynk.model;

public class BusinessRegistrationResponse {

    private String message;
    private String directory_id;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDirectory_id() {
        return directory_id;
    }

    public void setDirectory_id(String directory_id) {
        this.directory_id = directory_id;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
