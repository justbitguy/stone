package com.just.stone.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.constant.WhiteListConstant;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zac on 2016/8/2.
 */
public class ForceStopActivity extends Activity{

    ListView mListView;
    List<StopAppInfo> mAppList;
    ViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_force_stop);
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
    }

    private void initData(){
        mAppList = new ArrayList<>();
        Set<String> installedApps = InstalledPackageManager.getInstance().getPkgNameOfInstalledApp();
        for (String pkgName : installedApps){
            if (AppManagerUtil.isPackageStopped(pkgName)
                    || WhiteListConstant.systemList().contains(pkgName)
                    || AppManagerUtil.isSystemApp(pkgName)
                    || pkgName.equals(this.getPackageName())){
                continue;
            }
            StopAppInfo info = new StopAppInfo();
            info.packageName = pkgName;
            info.name = AppManagerUtil.getNameByPackage(pkgName);
            mAppList.add(info);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StoneAccessibilityService.getCallBackAction(this));
        this.registerReceiver(mBroadCastReceiver, intentFilter);
    }

    private void initView(){
        mListView = (ListView)findViewById(R.id.list_app_view);
        mAdapter = new ViewAdapter();
        mListView.setAdapter(mAdapter);

        findViewById(R.id.button_force_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForceStop();
            }
        });
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
                    forceStopNext();
                }
                break;
            default:
                break;
        }
    }

    private class ViewAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return mAppList.size();
        }

        @Override
        public Object getItem(int position){
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = ForceStopActivity.this.getLayoutInflater().inflate(R.layout.layout_app_item, null);
                ViewHolder.<CheckBox>get(convertView, R.id.app_item_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d("item", "click: " + (int)v.getTag());
                        CheckBox checkBox = (CheckBox)v;
                        boolean isChecked = checkBox.isChecked();
                        StopAppInfo item = (StopAppInfo)getItem((int)v.getTag());
                        item.isChecked = isChecked;
                    }
                });
                // set onclicklistener
            }

            final StopAppInfo itemData = (StopAppInfo) getItem(position);
            ViewHolder.<ImageView>get(convertView, R.id.app_item_icon).setImageDrawable(AppManagerUtil.getPackageIcon(itemData.packageName));
            ViewHolder.<TextView>get(convertView, R.id.app_item_name).setText(itemData.name);
            ViewHolder.<CheckBox>get(convertView, R.id.app_item_check).setTag(position);
            return convertView;
        }
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
        if (mAppList.size() == 0){
            this.finishActivity(AppManagerUtil.REQUEST_CODE_FORCE_STOP);
            return;
        }
        synchronized (mAppList) {
            StopAppInfo stopInfo = null;
            for (StopAppInfo info : mAppList){
                if (info.isChecked){
                    stopInfo = info;
                }
            }
            if (stopInfo != null) {
                AppManagerUtil.forceStopApp(this, stopInfo.packageName, false);
                mAppList.remove(stopInfo);
            }
        }
    }
}
