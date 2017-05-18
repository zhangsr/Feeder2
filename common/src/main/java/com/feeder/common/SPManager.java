package com.feeder.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @description:
 * @author: Match
 * @date: 7/24/16
 */
public class SPManager {
    private static SharedPreferences sSharedPreferences;

    public static void init(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static int getInt(String key, int defValue) {
        return sSharedPreferences.getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sSharedPreferences.getBoolean(key, defValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setInt(String key, int value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setString(String key, String value) {
        SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return sSharedPreferences.getString(key, defValue);
    }
}
