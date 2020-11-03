package com.example.redoy.lynk.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.redoy.lynk.util.Constants;

public class CustomSharedPreference {
    private SharedPreferences sharedPref;

    public CustomSharedPreference(Context context) {
        sharedPref = context.getSharedPreferences(Constants.SHARED_PREF, 0);
    }

    public void setAccessToken(String token) {
        sharedPref.edit().putString(Constants.ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return sharedPref.getString(Constants.ACCESS_TOKEN, "");
    }

    public void setUserData(String userData) {
        sharedPref.edit().putString(Constants.USER_DATA, userData).apply();
    }

    public String getUserData() {
        return sharedPref.getString(Constants.USER_DATA, "");
    }

    public void saveNotification(boolean notification) {
        sharedPref.edit().putBoolean(Constants.NOTIFICATION, notification).apply();
    }

    public boolean getSavedNotification() {
        return sharedPref.getBoolean(Constants.NOTIFICATION, true);
    }

    public void setCheckQuiz(String quiz) {
        sharedPref.edit().putString(Constants.CHECK_QUIZ, quiz).apply();
    }

    public void saveIsFirstTimeOpening(boolean isFirstTime) {
        sharedPref.edit().putBoolean(Constants.FIRST_TIME_OPENING, isFirstTime).apply();
    }

    public boolean getSavedIsFirstTimeOpening() {
        return sharedPref.getBoolean(Constants.FIRST_TIME_OPENING, true);
    }

    public void saveLanguage(String language) {
        sharedPref.edit().putString(Constants.LANGUAGE, language).apply();
    }

    public String getSavedLanguage() {
        return sharedPref.getString(Constants.LANGUAGE, "English");
    }

    public void saveIsRemember(boolean flag) {
        sharedPref.edit().putBoolean(Constants.IS_LOGGED_REMEMBER, flag).apply();
    }

    public boolean getIsRemember() {
        return sharedPref.getBoolean(Constants.IS_LOGGED_REMEMBER, false);
    }

    public void saveRemember(String email, String password) {
        sharedPref.edit().putString(Constants.EMAIL, email).apply();
        sharedPref.edit().putString(Constants.PASSWORD, password).apply();
    }

    public String getRememberEmail() {
        return sharedPref.getString(Constants.EMAIL, "");
    }

    public String getRememberPassword() {
        return sharedPref.getString(Constants.PASSWORD, "");
    }

    public void saveIsLogin(boolean flag) {
        sharedPref.edit().putBoolean(Constants.IS_LOGGED_IN, flag).apply();
    }

    public boolean getIsLogin() {
        return sharedPref.getBoolean(Constants.IS_LOGGED_IN, false);
    }

    public void saveLogin(String email, String password) {
        sharedPref.edit().putString(Constants.EMAIL, email).apply();
        sharedPref.edit().putString(Constants.PASSWORD, password).apply();
    }
}
