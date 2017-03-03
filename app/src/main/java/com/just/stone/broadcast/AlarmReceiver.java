package com.just.stone.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2017/2/14.
 */

public class AlarmReceiver extends BroadcastReceiver{
    public static final String ALARM_ACTION = "com.just.stone.action.alarm";
    public static final String ALARM_TYPE_KEY = "alarm_type";

    public static final String ALARM_TYPE_A = "alarm_type_a";

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(ALARM_TYPE_KEY);
        switch (type) {
            case ALARM_TYPE_A:
                LogUtil.d("alarm-test", "ALARM_TYPE_A fired!");
                break;
            default:
                break;
        }
    }
}
