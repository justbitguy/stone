package com.just.stone.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.LinkAddress;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.stone.ApplicationEx;
import com.just.stone.R;

import com.just.stone.async.Async;
import com.just.stone.model.eventbus.OnAllStopped;
import com.just.stone.service.StoneAccessibilityService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.greenrobot.event.EventBus;

/**
 * Created by Zac on 2016/8/2.
 */
public class ForceStopActivity extends Activity{

    List<String> mStopList;
    ViewGroup mCoverView;
    ViewGroup.LayoutParams mCoverViewLayoutParams;
    private WindowManager mWindowManager;
    private final Object mCoverLock = new Object();
    private AtomicBoolean isCovered = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mStopList = getIntent().getStringArrayListExtra("stopList");
        init();
    }

    @Override
    protected void onDestroy(){
        removeCover();
        this.unregisterReceiver(mBroadCastReceiver);
        super.onDestroy();
    }

    private void init(){
        initData();
        initView();
        Async.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startForceStop();
            }
        });
    }

    private void initData(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StoneAccessibilityService.getCallBackAction(this));
        this.registerReceiver(mBroadCastReceiver, intentFilter);
    }

    private void initView(){
        setContentView(R.layout.layout_stop_cover);
        mCoverView = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_stop_cover, null);
        mWindowManager = (WindowManager) ApplicationEx.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mCoverViewLayoutParams = new WindowManager.LayoutParams
                (
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_TOAST,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT
                );
    }

    private void addCover(){
        if (isCovered.get()){
            return;
        }
        synchronized (mCoverLock) {
            if (mWindowManager != null) {
                mWindowManager.addView(mCoverView, mCoverViewLayoutParams);
                isCovered.set(true);
            }
        }
    }

    private void removeCover(){
        if (!isCovered.get()){
            return;
        }

        synchronized (mCoverLock) {
            if (mWindowManager != null) {
                mWindowManager.removeView(mCoverView);
                isCovered.set(false);
            }
        }
    }

    private void startForceStop(){
        if (!StoneAccessibilityService.isEnabled(this)) {
            StoneAccessibilityService.showAccessibilitySettings(this);
            return;
        }
        addCover();
        forceStopNext();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case StoneAccessibilityService.REQUEST_SHOW_ACCESSIBILITY_SETTINGS:
                if (StoneAccessibilityService.isEnabled(getApplicationContext())) {
                    finishActivity(StoneAccessibilityService.REQUEST_SHOW_ACCESSIBILITY_SETTINGS);
                    startForceStop();
                }
                break;
            default:
                break;
        }
    }

    private void updateUI(String packageName){
        TextView tv = (TextView)mCoverView.findViewById(R.id.tv_app_name);
        tv.setText(AppManagerUtil.getNameByPackage(packageName));
        ImageView iv = (ImageView)mCoverView.findViewById(R.id.iv_app_icon);
        iv.setBackgroundDrawable(AppManagerUtil.getPackageIcon(packageName));
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout_app);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv, "translationY", 200, 0);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2);
        animSet.setDuration(1000);
        animSet.start();
    }

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
                LogUtil.d("access", "receive broadcast");
                if (intent.getIntExtra("result", 1) == 1) {
                    SystemClock.sleep(1000);
                    forceStopNext();
                }
            }
        }
    };

    private void forceStopNext(){
        if (mStopList.size() == 0) {
            this.finishActivity(AppManagerUtil.REQUEST_CODE_FORCE_STOP);
            removeCover();
            EventBus.getDefault().post(new OnAllStopped());
            finish();
            return;
        }

        String stopPackage = null;
        synchronized (mStopList) {
            stopPackage = mStopList.remove(0);
            if (stopPackage != null) {
                updateUI(stopPackage);
                AppManagerUtil.forceStopApp(this, stopPackage, false);
            }
        }
    }
}
