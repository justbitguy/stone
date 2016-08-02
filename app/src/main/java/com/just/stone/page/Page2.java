package com.just.stone.page;

import android.app.Activity;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.constant.WhiteListConstant;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.model.eventbus.OnForceStopApps;
import com.just.stone.model.eventbus.OnNotifyService;
import com.just.stone.model.pojo.StopAppInfo;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/1.
 */
public class Page2 extends Page{

    ListView mListView;
    ArrayList<StopAppInfo> mAppList;
    ViewAdapter mAdapter;
    ArrayList<StopAppInfo> mListToKill;

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
            if (AppManagerUtil.isPackageStopped(pkgName)
                    || WhiteListConstant.systemList().contains(pkgName)
                    || AppManagerUtil.isSystemApp(pkgName)
                    || pkgName.equals("com.just.stone")){
                continue;
            }
            StopAppInfo info = new StopAppInfo();
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

        TextView stopTv = (TextView)mView.findViewById(R.id.button_force_stop);
        stopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForceStop();
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

    private void startForceStop(){
        EventBus.getDefault().post(new OnForceStopApps((List)mListToKill.clone()));
    }

    public void onEventMainThread(){

    }
}
