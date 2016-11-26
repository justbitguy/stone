package com.just.stone;

import android.app.Application;
import android.content.Intent;

import com.just.stone.manager.NotifyManager;
import com.just.stone.service.LocalService;
//import com.squareup.leakcanary.LeakCanary;

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
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

        sInstance = this;
        startService();
        NotifyManager.getInstance();
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
