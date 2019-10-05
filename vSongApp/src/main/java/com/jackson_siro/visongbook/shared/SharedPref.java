package com.jackson_siro.visongbook.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {

    public static final int APPLY = 0;
    public static final int COMMIT = 1;
    private static int mode;

    public static void put(int key, Object value) {
        Context ctx = Contextor.getInstance().getContext();
        put(ctx.getString(key), value);
    }

    @SuppressLint("CommitPrefEdits")
    public static void put(String key, Object value) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (value instanceof String) {
            if (mode == APPLY) {
                prefs.edit().putString(key, (String) value).apply();
            } else {
                prefs.edit().putString(key, (String) value).commit();
            }
        }/* else if (value instanceof Integer) {
            if (mode == APPLY) {
                prefs.edit().putInt(key, (int) value).apply();
            } else {
                prefs.edit().putInt(key, (int) value).commit();
            }
        } else if (value instanceof Boolean) {
            if (mode == APPLY) {
                prefs.edit().putBoolean(key, (boolean) value).apply();
            } else {
                prefs.edit().putBoolean(key, (boolean) value).commit();
            }
        } else if (value instanceof Float) {
            if (mode == APPLY) {
                prefs.edit().putFloat(key, (float) value).apply();
            } else {
                prefs.edit().putFloat(key, (float) value).commit();
            }
        } else if (value instanceof Long) {
            if (mode == APPLY) {
                prefs.edit().putLong(key, (long) value).apply();
            } else {
                prefs.edit().putLong(key, (long) value).commit();
            }
        }*/
    }

    public static String getString(int key, String defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(ctx.getString(key), defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(key, defaultValue);
    }

    public static int getInt(int key, int defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(ctx.getString(key), defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(key, defaultValue);
    }

    public static boolean getBoolean(int key, boolean defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(ctx.getString(key), defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(key, defaultValue);
    }

    public static float getFloat(int key, float defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getFloat(ctx.getString(key), defaultValue);
    }

    public static long getLong(int key, long defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getLong(ctx.getString(key), defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getLong(key, defaultValue);
    }

    @SuppressLint("CommitPrefEdits")
    public static void remove(int key) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (mode == APPLY) {
            prefs.edit().remove(ctx.getString(key)).apply();
        } else {
            prefs.edit().remove(ctx.getString(key)).commit();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public static void remove(String key) {
        Context ctx = Contextor.getInstance().getContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        if (mode == APPLY) {
            prefs.edit().remove(key).apply();
        } else {
            prefs.edit().remove(key).commit();
        }
    }
}
