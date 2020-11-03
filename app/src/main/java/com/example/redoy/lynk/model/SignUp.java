package com.example.redoy.lynk.model;

public class SignUp {

    private String name;
    private String email;
    private String mobile;
    private String password;
    private String password_confirmation;

    public SignUp(String name, String email, String mobile, String password, String password_confirmation) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }
}
