package com.just.stone.manager;

import android.content.Context;

import com.just.stone.model.pojo.StopAppInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangjinwei on 2016/11/4.
 */

public class LeakTestManager {
    public static LeakTestManager sInstance;

    Context mContext;

    public LeakTestManager(Context context){
        this.mContext = context;
    }

    public static LeakTestManager getInstance(Context context){
        synchronized (ForceStopManager.class){
            if (sInstance == null){
                sInstance = new LeakTestManager(context);
            }
        }
        return sInstance;
    }
}
