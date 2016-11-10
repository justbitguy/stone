package com.just.stone.view;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.just.stone.R;
import com.just.stone.util.DeviceUtil;
import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2016/11/3.
 */

public class StoneScrollView extends ScrollView {

    public StoneScrollView(Context context) {
        super(context);
    }


    public StoneScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        LogUtil.d("scroll-view", "fling: " + velocityY + ", scrollY: " + getScrollY());
        //super.fling(0);
    }


    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        LogUtil.d("scroll-view", l + ", " + t + ", " + oldl + ", " + oldt);
    }

    @Override
    protected int computeVerticalScrollRange (){
        LogUtil.d("scroll-view", "computeVerticalScrollRange: " + DeviceUtil.px2Dp(super.computeVerticalScrollRange()));
        return DeviceUtil.dp2Px(50);
    }

}
