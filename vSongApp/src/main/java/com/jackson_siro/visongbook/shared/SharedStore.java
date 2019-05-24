package com.jackson_siro.visongbook.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedStore {
    private Context context;
    public SharedPreferences sharedPreferences;
    public SharedPreferences preferences;

    //Set First Launch
    private static final String FIRST_LAUNCH = "FIRST_OPEN_LAUNCH";

    public SharedStore(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFirstLaunch(boolean firstLaunch){
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, firstLaunch).apply();
    }

    public boolean whenLaunch(){
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }
}
