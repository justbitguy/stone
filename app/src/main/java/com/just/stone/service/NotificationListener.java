package com.just.stone.service;

import android.annotation.TargetApi;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.just.stone.model.eventbus.OnListenerCreated;
import com.just.stone.model.eventbus.OnNotifyEvent;
import com.just.stone.util.LogUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/23.
 */

@TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationListener extends NotificationListenerService {

    @Override
    public void onCreate(){
        super.onCreate();
        EventBus.getDefault().post(new OnListenerCreated());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtil.d("zpf", "open"+"-----"+ sbn );
        EventBus.getDefault().post(new OnNotifyEvent(sbn));
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtil.d("zpf", "shut"+"-----"+sbn);

    }

}