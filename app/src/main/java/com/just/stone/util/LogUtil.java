package com.just.stone.util;

import android.util.Log;

import com.just.stone.BuildConfig;

/**
 * Created by Zac on 2016/7/28.
 */

public class LogUtil {

    private static boolean LogEnabled = BuildConfig.DEBUG ? true : false;

    public static void d(String TAG, String msg) {
        if (LogEnabled)
            Log.d(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        if (LogEnabled)
            Log.e(TAG, msg);
    }

    public static void error(Exception e) {
        if (LogEnabled) {
            LogUtil.d("error", LogHelper.getFileLineMethod(2) + e.getMessage());
        }
    }

    public static void i(String TAG, String msg) {
        if (LogEnabled)
            Log.i(TAG, msg);
    }

    public static void v(String TAG, String msg) {
        if (LogEnabled)
            Log.v(TAG, msg);
    }

}