package com.jackson_siro.visongbook.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings extends Activity {
    Context mContext;

    public AppSettings(Context mContext) {
        this.mContext = mContext;
    }

    public void createNotification(int icontype) {
    	SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
	    localEditor.putBoolean("as_vsb_is_paid", true);
	    localEditor.commit();
	    
    }
}
