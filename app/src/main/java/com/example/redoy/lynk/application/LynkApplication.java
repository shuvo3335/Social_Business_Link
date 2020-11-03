package com.example.redoy.lynk.application;

import android.app.Application;
import android.content.Context;
import com.example.redoy.lynk.service.CustomSharedPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Redoy on 3/31/2018.
 */

public class LynkApplication extends Application {

    private static GsonBuilder builder;
    private static Gson gson;
    private static CustomSharedPreference shared;
    private static LynkApplication mInstance;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        //MultiDex.install(this);
    }

    public static synchronized LynkApplication getInstance() {
        return mInstance;
    }

    public static CustomSharedPreference getShared(Context context) {
        if (shared == null) {
            shared = new CustomSharedPreference(context);
        }
        return shared;
    }

    public static Gson getGsonObject() {
        if (gson == null) {
            builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }
}
