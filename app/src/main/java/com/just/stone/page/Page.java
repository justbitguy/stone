package com.just.stone.page;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.view.View;

import com.just.stone.model.eventbus.OnNoneEvent;

import de.greenrobot.event.EventBus;

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
        mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                onAttached();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                onDetached();
            }
        });
    }

    protected void initData(){
    }

    protected void initView(){
        mView = mContext.getLayoutInflater().inflate(mViewId, null);
    }

    public void onPageSelected(){
    }

    public void onResume(){
    }

    public View getView(){
        return mView;
    }

    protected void onAttached(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void onDetached(){
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    protected  void onEventAsync(OnNoneEvent event){
    }
}
