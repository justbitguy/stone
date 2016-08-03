package com.just.stone.page;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.BuildConfig;
import com.just.stone.R;
import com.just.stone.activity.ForceStopActivity;
import com.just.stone.constant.WhiteListConstant;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.model.eventbus.OnAppForceStopped;
import com.just.stone.model.eventbus.OnStartForceStop;
import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/1.
 */
public class Page2 extends Page{



    public Page2(Activity context, int mViewId){
        super(context, mViewId);
    }

    @Override
    protected void initData(){
        super.initData();
    }

    @Override
    protected void initView(){
        super.initView();
        mView.findViewById(R.id.start_force_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForceStopActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onPageSelected(){
    }
}
