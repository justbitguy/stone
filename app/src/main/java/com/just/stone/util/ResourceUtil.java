package com.just.stone.util;

import android.graphics.drawable.Drawable;

import com.just.stone.ApplicationEx;


public class ResourceUtil {
    private static Drawable DEFAULT_ICON = ApplicationEx.getInstance().getResources().getDrawable(android.R.drawable.sym_def_app_icon);

    public static String getString(int resId) {
        return ApplicationEx.getInstance().getString(resId);
    }

    public static int getColor(int colorId) {
        return ApplicationEx.getInstance().getResources().getColor(colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return ApplicationEx.getInstance().getResources().getDrawable(drawableId);
    }

    public static Drawable getDefaultIcon() {
        return DEFAULT_ICON;
    }
}
