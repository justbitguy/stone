package com.just.stone.page;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ParagraphStyle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.activity.ImageShowActivity;
import com.just.stone.async.Async;
import com.just.stone.constant.WhiteListConstant;
import com.just.stone.manager.ImageDownload;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.manager.UploadManager;
import com.just.stone.model.pojo.BaseAppInfo;
import com.just.stone.model.pojo.MyParcelable;
import com.just.stone.model.pojo.BaseAppInfo;
import com.just.stone.test.TestManager;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.util.Msg;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Zac on 2016/8/16.
 */

public class Page3 extends Page{

    public Page3(Activity context, int mViewId){
        super(context, mViewId);
    }

    ListView mListView;
    List<BaseAppInfo> mBaseAppList = new ArrayList<>();
    List<BaseAppInfo> mAllList = new ArrayList<>();

    ViewAdapter mAdapter;
    EditText mEditSearch;

    @Override
    protected  void init(){
        super.init();
    }
    @Override
    protected void initData(){
        super.initData();
        mBaseAppList = new ArrayList<>();
        mAllList = new ArrayList<>();
        //updateAppList();
    }

    @Override
    protected void initView(){
        super.initView();
        mListView = (ListView)mView.findViewById(R.id.list_app_view);
        mAdapter = new ViewAdapter();
        mListView.setAdapter(mAdapter);
        // Locate the EditText in listview_main.xml
        mEditSearch = (EditText) mView.findViewById(R.id.search);

        // Capture Text in EditText
        mEditSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = mEditSearch.getText().toString().toLowerCase(Locale.getDefault());
                mAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        updateAppList();
    }

    @Override
    public void onPageSelected(){
        mAdapter.notifyDataSetChanged();
    }

    private void updateAppList(){
        Set<String> installedApps = InstalledPackageManager.getInstance().getPkgNameOfInstalledApp();
        mBaseAppList.clear();
        mAllList.clear();
        for (String pkgName : installedApps){
            if (pkgName.equals(mContext.getPackageName()) || AppManagerUtil.isSystemApp(pkgName) ){
                continue;
            }
            addToAppList(pkgName);
        }
        mAllList.addAll(mBaseAppList);
    }

    private void addToAppList(String packageName){
        synchronized (mBaseAppList){
            boolean found = false;
            for (BaseAppInfo info : mBaseAppList){
                if (info.packageName.equals(packageName)){
                    found = true;
                    break;
                }
            }
            if (!found){
                BaseAppInfo newInfo = new BaseAppInfo();
                newInfo.packageName = packageName;
                newInfo.name = AppManagerUtil.getNameByPackage(packageName);
                mBaseAppList.add(newInfo);
            }
        }
    }

    private class ViewAdapter extends BaseAdapter {

        @Override
        public int getCount(){
            return mBaseAppList.size();
        }

        @Override
        public Object getItem(int position){
            return mBaseAppList.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView (int position, View contentView, ViewGroup parent){
            if (contentView == null){
                contentView = mContext.getLayoutInflater().inflate(R.layout.layout_app_info_item, null);
                ViewHolder.<TextView>get(contentView, R.id.tv_action).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/10/8
                        BaseAppInfo item = (BaseAppInfo)getItem((int)v.getTag());
                        AppManagerUtil.launchApp(mContext, item.packageName);

                    }
                });
                // set onclicklistener
                ViewHolder.<LinearLayout>get(contentView, R.id.linear_layout_app_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseAppInfo item = (BaseAppInfo)getItem((int)v.getTag());
                        AppManagerUtil.showInstalledAppDetails(item.packageName);
                    }
                });
            }

            final BaseAppInfo itemData = (BaseAppInfo) getItem(position);
            ViewHolder.<ImageView>get(contentView, R.id.app_item_icon).setImageDrawable(AppManagerUtil.getPackageIcon(itemData.packageName));
            ViewHolder.<TextView>get(contentView, R.id.app_item_name).setText(itemData.name);
            ViewHolder.<TextView>get(contentView, R.id.app_package_name).setText(itemData.packageName);
            ViewHolder.<TextView>get(contentView, R.id.tv_action).setTag(position);
            ViewHolder.<LinearLayout>get(contentView, R.id.linear_layout_app_item).setTag(position);
            return contentView;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            //worldpopulationlist.clear();
            mBaseAppList.clear();
            if (charText.length() == 0) {
                mBaseAppList.addAll(mAllList);
            }
            else
            {
                for (BaseAppInfo info : mAllList)
                {
                    if (containsText(info.name, charText) || containsText(info.packageName, charText)) {
                        mBaseAppList.add(info);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    private boolean containsText(String str, String subStr) {
        return !TextUtils.isEmpty(str) && str.contains(subStr);
    }
}
