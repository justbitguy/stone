package com.just.stone.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.stone.R;
import com.just.stone.async.Async;
import com.just.stone.manager.UploadManager;
import com.just.stone.model.eventbus.OnNotifyService;
import com.just.stone.page.Page;
import com.just.stone.page.Page2;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class StoneActivity extends Activity {
    List<View> viewList = new ArrayList<View>();
    List<String> titleList = new ArrayList<>();
    ViewPager mViewPager;
    Page2 mPage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone);
        init();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mPage2.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                if (position == 1){
                    mPage2.onPageSelected();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        View view1 = getLayoutInflater().inflate(R.layout.layout_view1, null);
        mPage2 = new Page2(this, R.layout.layout_view2);
        View view3 = getLayoutInflater().inflate(R.layout.layout_view3, null);
        viewList.add(view1);
        viewList.add(mPage2.getView());
        viewList.add(view3);
        titleList.add("view1");
        titleList.add("view2");
        titleList.add("view3");
        findViewById(R.id.layout_bottom_tool_first).setSelected(true);
        findViewById(R.id.layout_bottom_tool_first_image).setSelected(true);
        findViewById(R.id.layout_bottom_tool_first_text).setSelected(true);
        mPagerAdapter.notifyDataSetChanged();
        bindAction();
    }

    private void bindAction(){
        findViewById(R.id.layout_top_tool_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("upload", "start upload!");

                Async.run(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            UploadManager.upLoadByCommonPost();
                        } catch (Exception e){
                            LogUtil.error(e);
                        }
                    }
                });

            }
        });

        findViewById(R.id.layout_top_tool_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnNotifyService());
                DisplayMetrics metric = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metric);

                int width = metric.widthPixels;  // 宽度（PX）
                int height = metric.heightPixels;  // 高度（PX）
                LogUtil.d("screen", "width: " + width + ", height: " + height);

            }
        });
    }
}
