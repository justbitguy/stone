package com.just.stone.page;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.view.View;

import com.just.stone.ApplicationEx;
import com.just.stone.R;
import com.just.stone.activity.AnimationActivity;
import com.just.stone.activity.CustomViewActivity;
import com.just.stone.activity.TestActivity;
import com.just.stone.async.Async;
import com.just.stone.broadcast.DeviceAdminSampleReceiver;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.manager.UploadManager;
import com.just.stone.util.LogUtil;
import com.just.stone.util.Msg;

import java.util.List;

/**
 * Created by zhangjw on 2016/8/25.
 */

public class Page1 extends Page {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 12301;

    public Page1(Activity context, int mViewId) {
        super(context, mViewId);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        mView.findViewById(R.id.tv_device_admin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName mAdminName = new ComponentName(ApplicationEx.getInstance().getApplicationContext(), DeviceAdminSampleReceiver.class);

                // Launch the activity to have the user enable our admin.
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "extra string");
                mContext.startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
            }
        });

        mView.findViewById(R.id.tv_anim_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, AnimationActivity.class);
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_activity_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, TestActivity.class);
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_activity_custom_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, CustomViewActivity.class);
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_app_signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<PackageInfo> list = InstalledPackageManager.getInstance().getPackageInfoList();
                    for (PackageInfo info : list) {
                        Signature[] sigs = mContext.getPackageManager().getPackageInfo(info.packageName, PackageManager.GET_SIGNATURES).signatures;
                        for (Signature sig : sigs) {
                            LogUtil.d("signature", info.packageName + ":" + sig.toCharsString());
                        }
                    }
                } catch (Exception e){

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPageSelected() {
        // TODO: 2016/8/16
    }
}