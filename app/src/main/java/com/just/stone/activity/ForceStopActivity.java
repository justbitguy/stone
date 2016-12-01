package com.just.stone.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.LinkAddress;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final long STOP_INTERVAL = 600;
    private static final long PER_STOP_TIMEOUT = 2 * 1000;
    private static final String TAG = "force-stop";

    List<String> mStopList;
    ViewGroup mCoverView;
    ViewGroup.LayoutParams mCoverViewLayoutParams;
    private WindowManager mWindowManager;
    private final Object mCoverLock = new Object();
    private AtomicBoolean isCovered = new AtomicBoolean(false);
    private AtomicBoolean isOver = new AtomicBoolean(false);
    private boolean forceStopped = false;

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
        setContentView(R.layout.layout_stop);
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

    private void updateUI(String packageName){
        TextView tv = (TextView)mCoverView.findViewById(R.id.tv_app_name);
        tv.setText(AppManagerUtil.getNameByPackage(packageName));
        ImageView iv = (ImageView)mCoverView.findViewById(R.id.iv_app_icon);
        iv.setImageDrawable(AppManagerUtil.getPackageIcon(packageName));
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv, "translationY", 200, 0);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(tv, "alpha", 1f, 0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2).with(anim3);
        animSet.setDuration(500);
        animSet.start();
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

    private void startForceStop(){
        if (mStopList.size() == 0){
            Toast.makeText(this, "nothing to clear!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!StoneAccessibilityService.isEnabled(this)) {
            StoneAccessibilityService.showAccessibilitySettings(this);
            return;
        }

        isOver.set(false);
        addCover();
        Async.schedule(STOP_INTERVAL * 4 *  mStopList.size(), mAllTimeout);
        forceStopNext();
    }

    @Override
    public void onBackPressed(){
        forceStopped = true;
    }

    private void forceStopNext(){
        if (mStopList.size() == 0 || forceStopped) {
            LogUtil.d(TAG, "stopList.size == 0");
            this.finishActivity(AppManagerUtil.REQUEST_CODE_FORCE_STOP);
            Async.scheduleTaskOnUiThread(1000, mAllOverTask);
            return;
        }

        String stopPackage = null;
        synchronized (mStopList) {
            stopPackage = mStopList.remove(0);
            if (stopPackage != null) {
                StoneAccessibilityService.setCando(this, true);
                updateUI(stopPackage);
                AppManagerUtil.forceStopApp(this, stopPackage, false);
                Async.scheduleTaskOnUiThread(PER_STOP_TIMEOUT, mPerStopTimeout);
            }
        }
    }

    private Runnable mPerStopTimeout = new Runnable() {
        @Override
        public void run() {
            LogUtil.d(TAG, "per stop timeout.");
            forceStopNext();
        }
    };

    private Runnable mAllTimeout = new Runnable() {
        @Override
        public void run() {
            LogUtil.d(TAG, "Time Out!");
            Async.runOnUiThread(mAllOverTask);
        }
    };

    private Runnable mAllOverTask = new Runnable() {
        @Override
        public void run() {
            if (isOver.get()){
                return;
            }
            LogUtil.d(TAG, "stop over runnalbe!");
            isOver.set(true);
            removeCover();
            finishActivity(AppManagerUtil.REQUEST_CODE_FORCE_STOP);
            EventBus.getDefault().post(new OnAllStopped());
            finish();
        }
    };

    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(StoneAccessibilityService.getCallBackAction(context))){
                LogUtil.d("access", "receive broadcast");
                if (intent.getIntExtra("result", 1) == 1) {
                    Async.removeScheduledTaskOnUiThread(mPerStopTimeout);
                    SystemClock.sleep(STOP_INTERVAL);
                    forceStopNext();
                }
            }
        }
    };
}
