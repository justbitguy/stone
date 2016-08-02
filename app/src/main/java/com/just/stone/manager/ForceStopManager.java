package com.just.stone.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/2.
 */
public class ForceStopManager {
    public static ForceStopManager sInstance;
    List<StopAppInfo> mStopList;
    Context mContext;
    AtomicBoolean isRunning = new AtomicBoolean(false);

    public ForceStopManager(Context context){
        this.mContext = context;
        EventBus.getDefault().register(this);
    }

    public static ForceStopManager getInstance(Context context){
        synchronized (ForceStopManager.class){
            if (sInstance == null){
                sInstance = new ForceStopManager(context);
            }
        }
        return sInstance;
    }

    @Override
    protected void finalize(){
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void start(List<StopAppInfo> list){
        if (isRunning.get()){
            return;
        }
        mStopList = list;
    }

    private void forceStopNext(){
//        AppManagerUtil.forceStopApp();
    }

//    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
//                LogUtil.d("access", "receive broadcast");
//                if (intent.getIntExtra("result", 1) == 1) {
//                    // TODO: 2016/8/2  sleep and continue to kill
//                }
//            }
//        }
//    };
}
