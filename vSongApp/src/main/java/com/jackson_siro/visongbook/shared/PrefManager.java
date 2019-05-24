package com.jackson_siro.visongbook.shared;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "SigninStore";

    private static final String SET_LOGGED_IN = "is_signedin";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setBoolean(String PREF_NAME,Boolean val) {
        editor.putBoolean(PREF_NAME, val);
        editor.commit();
    }
    public void setString(String PREF_NAME,String VAL) {
        editor.putString(PREF_NAME, VAL);
        editor.commit();
    }
    public void setInt(String PREF_NAME,int VAL) {
        editor.putInt(PREF_NAME, VAL);
        editor.commit();
    }
    public boolean getBoolean(String PREF_NAME) {
        return pref.getBoolean(PREF_NAME,true);
    }

    public void remove(String PREF_NAME){
        if(pref.contains(PREF_NAME)){
            editor.remove(PREF_NAME);
            editor.commit();
        }
    }
    public String getString(String PREF_NAME) {
        if(pref.contains(PREF_NAME)){
            return pref.getString(PREF_NAME,null);
        }
        return  "";
    }

    public void setSignin(boolean isLogged){
        editor.putBoolean(SET_LOGGED_IN, isLogged);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(SET_LOGGED_IN, false);
    }
}
