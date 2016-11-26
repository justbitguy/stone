package com.just.stone.model.eventbus;

import android.app.Notification;
import android.service.notification.StatusBarNotification;

/**
 * Created by zhangjinwei on 2016/11/11.
 */

public class OnNotifyEvent {
    StatusBarNotification sbn;

    public OnNotifyEvent(StatusBarNotification sbn){
        this.sbn = sbn;
    }

    public StatusBarNotification getNotification(){
        return this.sbn;
    }
}
