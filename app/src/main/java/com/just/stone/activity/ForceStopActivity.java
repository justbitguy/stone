package com.just.stone.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import com.just.stone.R;

import com.just.stone.async.Async;
import com.just.stone.model.eventbus.OnAllStopped;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/2.
 */
public class ForceStopActivity extends Activity{

//    ListView mListView;
//    List<StopAppInfo> mAppList;
    List<String> mStopList;
//    ViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mStopList = getIntent().getStringArrayListExtra("stopList");
        init();
    }

    @Override
    protected void onDestroy(){
        this.unregisterReceiver(mBroadCastReceiver);
        super.onDestroy();
    }

    private void init(){
        initData();
        initView();
        Async.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startForceStop();
            }
        });
    }

    private void initData(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StoneAccessibilityService.getCallBackAction(this));
        this.registerReceiver(mBroadCastReceiver, intentFilter);
    }

    private void initView(){
        setContentView(R.layout.activiy_force_stop);
    }

    private void startForceStop(){
        if (!StoneAccessibilityService.isEnabled(this)) {
            StoneAccessibilityService.showAccessibilitySettings(this);
            return;
        }
        forceStopNext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case StoneAccessibilityService.REQUEST_SHOW_ACCESSIBILITY_SETTINGS:
                if (StoneAccessibilityService.isEnabled(getApplicationContext())) {
                    finishActivity(StoneAccessibilityService.REQUEST_SHOW_ACCESSIBILITY_SETTINGS);
                    startForceStop();
                }
                break;
            default:
                break;
        }
    }

    private void updateUI(String packageName){
        TextView tv = (TextView)findViewById(R.id.tv_app_name);
        tv.setText(packageName);
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
                LogUtil.d("access", "receive broadcast");
                if (intent.getIntExtra("result", 1) == 1) {
                    SystemClock.sleep(1000);
                    forceStopNext();
                }
            }
        }
    };

    private void forceStopNext(){
        if (mStopList.size() == 0) {
            this.finishActivity(AppManagerUtil.REQUEST_CODE_FORCE_STOP);
            EventBus.getDefault().post(new OnAllStopped());
            return;
        }

        String stopPackage = null;
        synchronized (mStopList) {
            stopPackage = mStopList.remove(0);
            if (stopPackage != null) {
                updateUI(stopPackage);
                AppManagerUtil.forceStopApp(this, stopPackage, false);
            }
        }
    }
}