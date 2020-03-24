package com.jackson_siro.visongbook.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

public class Contextor {
    @SuppressLint("StaticFieldLeak")
    private static Contextor sInstance;
    private Context mContext;

    public static Contextor getInstance() {
        if (sInstance == null) {
            Log.d("TAG", "creating MyApplication context");
            sInstance = new Contextor();
        }
        return sInstance;
    }

    public void init(Context pContext) {
        mContext = pContext;
    }

    public Context getContext() {
        return mContext;
    }

}