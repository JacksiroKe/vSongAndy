package com.jackson_siro.visongbook.setting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
    public static boolean isConnectCheck(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null){
                if (networkInfo.isConnected() || networkInfo.isConnectedOrConnecting()){
                    return true;
                }else {
                    return false;
                }

            }else {
                return false;
            }

        }catch (Exception e){
            return false;
        }
    }
}