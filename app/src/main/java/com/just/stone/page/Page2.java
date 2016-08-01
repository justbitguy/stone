package com.just.stone.page;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.model.AppInfo;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zac on 2016/8/1.
 */
public class Page2 extends Page{

    ListView mListView;
    List<AppInfo> mAppList;
    ViewAdapter mAdapter;

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
}
