package com.just.stone.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2016/11/3.
 */

public class ChargingStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean chargingEnable = intent.getBooleanExtra("boostChargingOpen", false);
        LogUtil.d("stone-charging", "receive powersecurity launch: " + chargingEnable);
    }
}
