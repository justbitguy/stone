package com.just.stone.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2017/2/10.
 */

public class PackageInstallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            LogUtil.d("install", "ACTION_PACKAGE_REMOVED" +  ", replace: " + replacing);
        }  else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            LogUtil.d("install", "ACTION_PACKAGE_ADDED" +  ", replace: " + replacing);
        } else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){
            LogUtil.d("install", "ACTION_PACKAGE_REPLACED" +  ", replace: " + replacing);
        }
    }
}
