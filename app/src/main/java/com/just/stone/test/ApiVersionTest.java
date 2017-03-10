package com.just.stone.test;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.just.stone.ApplicationEx;
import com.just.stone.BuildConfig;
import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2017/3/3.
 */

public class ApiVersionTest {
    static void startTest() {
        LogUtil.d("ApiVersionTest", "targetSdk: " + getTargetSdkVersion() + ", Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
    }

    private static int getTargetSdkVersion() {
        int version = 0;
        PackageManager pm = ApplicationEx.getInstance().getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(ApplicationEx.getInstance().getPackageName(), 0);
            if (applicationInfo != null) {
                version = applicationInfo.targetSdkVersion;
            }
        } catch (Exception e) {

        } finally {
            return version;
        }
    }

}
