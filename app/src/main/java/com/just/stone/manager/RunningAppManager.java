package com.just.stone.manager;

import android.app.ActivityManager;
import android.content.Context;

import com.just.stone.util.LogUtil;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by zhangjinwei on 2017/3/8.
 */

public class RunningAppManager {

    public static RunningAppManager sInstance;

    private RunningAppManager() {

    }

    public static RunningAppManager getInstance() {
        synchronized (RunningAppManager.class) {
            if (sInstance == null) {
                sInstance = new RunningAppManager();
            }
        }
        return sInstance;
    }

    public void getRunningApps(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : procInfos) {
            LogUtil.d("running-app", "process: " + info.processName);
        }
    }
}
