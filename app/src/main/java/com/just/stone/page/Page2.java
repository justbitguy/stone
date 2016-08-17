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
import com.just.stone.model.eventbus.OnAllStopped;
import com.just.stone.model.eventbus.OnAppForceStopped;
import com.just.stone.model.eventbus.OnStartForceStop;
import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.view.ViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/1.
 */
public class Page2 extends Page{

    ListView mListView;
    List<StopAppInfo> mAppList = new ArrayList<>();
    ArrayList<String> mStopList = new ArrayList<>();
    ViewAdapter mAdapter;


    public Page2(Activity context, int mViewId){
        super(context, mViewId);
    }

    @Override
    protected  void init(){
        super.init();
    }
    @Override
    protected void initData(){
        super.initData();
        mAppList = new ArrayList<>();
        updateAppList();
    }

    private void updateAppList(){
        Set<String> installedApps = InstalledPackageManager.getInstance().getPkgNameOfInstalledApp();
        for (String pkgName : installedApps){
            if (AppManagerUtil.isPackageStopped(pkgName)
                    || WhiteListConstant.systemList().contains(pkgName)
                    || WhiteListConstant.inputMethodList().contains(pkgName)
                    || AppManagerUtil.isSystemApp(pkgName)
                    || pkgName.equals(mContext.getPackageName())){
                continue;
            }
            addToAppList(pkgName);
        }
    }

    @Override
    protected void initView(){
        super.initView();
        mListView = (ListView)mView.findViewById(R.id.list_app_view);
        mAdapter = new ViewAdapter();
        mListView.setAdapter(mAdapter);
        mView.findViewById(R.id.start_force_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStopList.clear();
                synchronized (mAppList){
                    for (StopAppInfo info : mAppList){
                        if (info.isChecked){
                            mStopList.add(info.packageName);
                        }
                    }
                }
                Intent intent = new Intent(mContext, ForceStopActivity.class);
                intent.putStringArrayListExtra("stopList",(ArrayList<String>)mStopList.clone());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        updateAppList();
    }

    private void addToAppList(String packageName){
        synchronized (mAppList){
            boolean found = false;
            for (StopAppInfo info : mAppList){
                if (info.packageName.equals(packageName)){
                    found = true;
                    break;
                }
            }
            if (!found){
                StopAppInfo newInfo = new StopAppInfo();
                newInfo.packageName = packageName;
                newInfo.name = AppManagerUtil.getNameByPackage(packageName);
                mAppList.add(newInfo);
            }
        }
    }

    public void onEventMainThread(OnAllStopped event){
        for (String packageName : mStopList){
            synchronized (mAppList) {
                int index = -1;
                for (StopAppInfo info : mAppList) {
                    if (packageName.equals(info.packageName)) {
                        index = mAppList.indexOf(info);
                    }
                }
                if (index != -1) {
                    mAppList.remove(index);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageSelected(){
        mAdapter.notifyDataSetChanged();
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
                convertView = mContext.getLayoutInflater().inflate(R.layout.layout_app_item, null);
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
}
