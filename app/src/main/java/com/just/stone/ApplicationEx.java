package com.just.stone;

import android.app.Application;

/**
 * Created by Zac on 2016/8/1.
 */
public class ApplicationEx extends Application {
    private static ApplicationEx sInstance;

    public static ApplicationEx getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
