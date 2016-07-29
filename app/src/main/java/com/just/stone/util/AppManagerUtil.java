package com.just.stone.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Zac on 2016/7/28.
 */
public class AppManagerUtil {

    public static final int REQUEST_CODE_FORCE_STOP = 1024;

    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";

     /* 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2) */
    private static final String APP_PKG_NAME_22 = "pkg";

    /* InstalledAppDetails所在包名 */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";

    /* InstalledAppDetails类名 */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

    public static void forceStopApp(Activity activity, String packageName, boolean startFloatWindow) {
        LogUtil.d("force-stop", packageName + "");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }

        try {
            //为什么要用startActivityForResult？因为这样可以关闭activity
            activity.startActivityForResult(intent, REQUEST_CODE_FORCE_STOP);
            activity.overridePendingTransition(0, 0);
            LogUtil.d("force-stop", "app of process forceStopApp success: " + packageName);
        } catch (Exception e) {
            //Activity not found
//            EventBus.getDefault().post(new OnForceStopFail());
        } finally {

        }
    }

}
