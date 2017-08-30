package com.jackson_siro.visongbook.tools;

import android.app.Application;
 
public class AppManager extends Application {
 
    private static AppManager mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        mInstance = this;
    }
 
    public static synchronized AppManager getInstance() {
        return mInstance;
    }
 
    public void setConnectivityListener(AppConnected.ConnectivityReceiverListener listener) {
    	AppConnected.connectivityReceiverListener = listener;
    }
}
