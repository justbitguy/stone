package com.just.stone.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.LogUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/2.
 */
public class ForceStopManager {
    List<StopAppInfo> mStopList;
    Context mContext;

    public ForceStopManager(Context context, List<StopAppInfo> list){
        this.mContext = context;
        this.mStopList = list;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void finalize(){
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void startForceStop(){
        // TODO: 2016/8/2  
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
                LogUtil.d("access", "receive broadcast");
                if (intent.getIntExtra("result", 1) == 1) {
                    // TODO: 2016/8/2  sleep and continue to kill
                }
            }
        }
    };
}
