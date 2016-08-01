package com.just.stone.page;

import android.app.Activity;
import android.view.View;

/**
 * Created by Zac on 2016/8/1.
 */
public abstract class Page {
    Activity mContext;
    View mView;
    int mViewId;

    public Page(){
    }

    public Page(Activity context, int viewId) {
        mContext = context;
        mViewId = viewId;
        init();
    }

    protected void init(){
        initData();
        initView();
    }

    protected void initData(){
    }

    protected void initView(){
        mView = mContext.getLayoutInflater().inflate(mViewId, null);
    }

    public void onPageSelected(){
    }

    public View getView(){
        return mView;
    }
}
