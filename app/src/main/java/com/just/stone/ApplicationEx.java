package com.just.stone;

import android.app.Application;
import android.content.Intent;

import com.just.stone.service.LocalService;

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
        startService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void startService(){
        Intent intent = new Intent(this.getApplicationContext(), LocalService.class);
        startService(intent);
    }
}
