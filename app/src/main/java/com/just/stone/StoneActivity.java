package com.just.stone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class StoneActivity extends Activity {
    List<View> viewList = new ArrayList<View>();
    List<String> titleList = new ArrayList<>();
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stone);
        init();
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
        View view1 = getLayoutInflater().inflate(R.layout.layout_view1, null);
        View view2 = getLayoutInflater().inflate(R.layout.layout_view2, null);
        View view3 = getLayoutInflater().inflate(R.layout.layout_view3, null);
        viewList.add(view1);
        viewList.add(view2);
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

    }


}
