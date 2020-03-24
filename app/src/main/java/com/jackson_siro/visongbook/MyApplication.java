package com.jackson_siro.visongbook;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}