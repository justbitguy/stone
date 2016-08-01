package com.just.stone.page;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.StoneActivity;
import com.just.stone.constant.WhiteListConstant;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.model.AppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zac on 2016/8/1.
 */
public class Page2 extends Page{

    ListView mListView;
    ArrayList<AppInfo> mAppList;
    ViewAdapter mAdapter;
    List<AppInfo> mListToKill;

    public Page2(Activity context, int mViewId){
        super(context, mViewId);
    }

    @Override
    protected void initData(){
        super.initData();
        mAppList = new ArrayList<>();
        Set<String> appList = InstalledPackageManager.getInstance().getPkgNameOfInstalledApp();
        List<String> pkgNameList = new ArrayList<>();
        pkgNameList.addAll(appList);
        for (String pkgName : pkgNameList){
            if (AppManagerUtil.isPackageStopped(pkgName) || WhiteListConstant.systemList().contains(pkgName)){
                continue;
            }
            AppInfo info = new AppInfo();
            info.packageName = pkgName;
            info.name = AppManagerUtil.getNameByPackage(pkgName);
            mAppList.add(info);
        }
    }

    @Override
    protected void initView(){
        super.initView();
        mListView = (ListView)mView.findViewById(R.id.list_app_view);
        mAdapter = new ViewAdapter();
        mListView.setAdapter(mAdapter);
        if (mAppList.size() != 0) {
            mAdapter.notifyDataSetChanged();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StoneAccessibilityService.getCallBackAction(mContext));
//        mContext.registerReceiver(mBroadCastReceiver, intentFilter);

        TextView stopTv = (TextView)mView.findViewById(R.id.button_force_stop);
        stopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListToKill = (List)mAppList.clone();
                AppInfo info = mListToKill.get(0);
                AppManagerUtil.forceStopApp(mContext, info.packageName, false);
                mListToKill.remove(0);
            }
        });
    }

    @Override
    public void onPageSelected(){
        mAdapter.notifyDataSetChanged();
    }

    private class ViewAdapter extends BaseAdapter{

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
                // set onclicklistener
            }

            final AppInfo itemData = (AppInfo) getItem(position);
            ViewHolder.<ImageView>get(convertView, R.id.app_item_icon).setImageDrawable(AppManagerUtil.getPackageIcon(itemData.packageName));
            ViewHolder.<TextView>get(convertView, R.id.app_item_name).setText(itemData.name);
            return convertView;
        }
    }

    public void continueKill(){
        if (mListToKill.size() == 0){
            return;
        }
        AppInfo info = mListToKill.get(0);
        if (AppManagerUtil.isPackageStopped(info.packageName)){
            mListToKill.remove(0);
            return;
        }

        if (info.packageName.equals("com.just.stone")){
            return;
        }
        AppManagerUtil.forceStopApp(mContext, info.packageName, false);
        LogUtil.d("force-stop", info.packageName);
        mListToKill.remove(0);
    }

//    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
//                LogUtil.d("access", "receive broadcast");
//                if (intent.getIntExtra("result", 1) == 1) {
//                }
//            }
//        }
//    };
}
