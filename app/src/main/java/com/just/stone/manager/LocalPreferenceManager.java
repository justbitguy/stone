package com.just.stone.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.just.stone.ApplicationEx;

/**
 * Created by zhangjinwei on 2017/3/10.
 */

public class LocalPreferenceManager {
    static final String  SHARE_PREFERENCE_FILE = "stone_share_preference";
    static SharedPreferences sharedPreferences = ApplicationEx.getInstance().getSharedPreferences(SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);

    public static void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
}
