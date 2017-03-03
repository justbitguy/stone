package com.just.stone.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.just.stone.ApplicationEx;
import com.just.stone.broadcast.AlarmReceiver;

/**
 * Created by zhangjinwei on 2017/2/14.
 */

public class AlarmTestManager {

    static public void setAlarm() {
        Context context = ApplicationEx.getInstance();
        AlarmManager alarmManager = (AlarmManager) ApplicationEx.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AlarmReceiver.ALARM_ACTION);
        intent.putExtra(AlarmReceiver.ALARM_TYPE_KEY, AlarmReceiver.ALARM_TYPE_A);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20 * 1000, pendingIntent);
    }
}
