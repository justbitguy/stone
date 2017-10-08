package com.just.stone.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.just.stone.R;
import com.just.stone.async.Async;
import com.just.stone.broadcast.DeviceAdminSampleReceiver;
import com.just.stone.manager.LeakTestManager;
import com.just.stone.model.eventbus.OnFontSizeChanged;
import com.just.stone.model.eventbus.OnNotifyService;
import com.just.stone.page.Page1;
import com.just.stone.page.Page2;
import com.just.stone.page.Page3;
import com.just.stone.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity implements View.OnClickListener{
    List<View> viewList = new ArrayList<View>();
    List<String> titleList = new ArrayList<>();
    ViewPager mViewPager;
    Page1 mPage1;
    Page2 mPage2;
    Page3 mPage3;
    DeviceAdminSampleReceiver mDeviceAdminSample;

    Context mContext;

    private static final int REQUEST_CODE_ENABLE_ADMIN = 12021;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone);
        init();
//        LeakTestManager.getInstance(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPage2.onResume();
        mPage3.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init(){
        mViewPager = (ViewPager)findViewById(R.id.stone_view_pager);
        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPage1 = new Page1(this, R.layout.layout_view1);
        mPage2 = new Page2(this, R.layout.layout_view2);
        mPage3 = new Page3(this, R.layout.layout_view3);

        viewList.add(mPage1.getView());
        viewList.add(mPage2.getView());
        viewList.add(mPage3.getView());

        titleList.add("view1");
        titleList.add("view2");
        titleList.add("view3");
        findViewById(R.id.layout_bottom_tool_first).setSelected(true);
        findViewById(R.id.layout_bottom_tool_first_image).setSelected(true);
        findViewById(R.id.layout_bottom_tool_first_text).setSelected(true);
        mPagerAdapter.notifyDataSetChanged();
        bindAction();
        pageSelected(mViewPager.getCurrentItem());
    }

    static int i = 1, j = 1;
    private void bindAction(){
        findViewById(R.id.layout_bottom_tool_first).setOnClickListener(this);
        findViewById(R.id.layout_bottom_tool_second).setOnClickListener(this);
        findViewById(R.id.layout_bottom_tool_third).setOnClickListener(this);
    }

    private void pageSelected(int position) {
        switch(position) {
            case 0:
                ((ImageView)findViewById(R.id.layout_bottom_tool_first_image)).setImageResource(R.drawable.button_green);
                ((ImageView)findViewById(R.id.layout_bottom_tool_second_image)).setImageResource(R.drawable.button);
                ((ImageView)findViewById(R.id.layout_bottom_tool_third_image)).setImageResource(R.drawable.button);
                mPage1.onPageSelected();
                break;
            case 1:
                ((ImageView)findViewById(R.id.layout_bottom_tool_first_image)).setImageResource(R.drawable.button);
                ((ImageView)findViewById(R.id.layout_bottom_tool_second_image)).setImageResource(R.drawable.button_green);
                ((ImageView)findViewById(R.id.layout_bottom_tool_third_image)).setImageResource(R.drawable.button);
                mPage2.onPageSelected();
                break;
            case 2:
                ((ImageView)findViewById(R.id.layout_bottom_tool_first_image)).setImageResource(R.drawable.button);
                ((ImageView)findViewById(R.id.layout_bottom_tool_second_image)).setImageResource(R.drawable.button);
                ((ImageView)findViewById(R.id.layout_bottom_tool_third_image)).setImageResource(R.drawable.button_green);
                mPage3.onPageSelected();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_bottom_tool_first:
                switch2Page(0);
                break;
            case R.id.layout_bottom_tool_second:
                switch2Page(1);
                break;
            case R.id.layout_bottom_tool_third:
                switch2Page(2);
                break;
            default:
                break;
        }
    }

    private void switch2Page(int position) {
        mViewPager.setCurrentItem(position);
    }

    public void onEventMainThread(OnFontSizeChanged event){
        Configuration configuration = getResources().getConfiguration();
        LogUtil.d("font-size", configuration.fontScale + "");
        configuration.fontScale=(float) 0.75; //0.85 small size, 1 normal size, 1,15 big etc

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }
}
