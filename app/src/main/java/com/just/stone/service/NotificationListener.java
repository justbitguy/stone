package com.just.stone.service;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.test.suitebuilder.annotation.Suppress;

import com.just.stone.util.LogUtil;

/**
 * Created by Zac on 2016/8/23.
 */


public class NotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        LogUtil.d("zpf", "open"+"-----"+sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtil.d("zpf", "shut"+"-----"+sbn);

    }

}