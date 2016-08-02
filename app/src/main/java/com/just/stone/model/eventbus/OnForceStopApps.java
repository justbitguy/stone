package com.just.stone.model.eventbus;

import com.just.stone.model.pojo.StopAppInfo;

import java.util.List;

/**
 * Created by Zac on 2016/8/2.
 */
public class OnForceStopApps {
    public List<StopAppInfo> appList;
    public OnForceStopApps(List<StopAppInfo> list){
        this.appList = list;
    }
}
