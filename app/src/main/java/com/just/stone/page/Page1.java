package com.just.stone.page;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.just.stone.ApplicationEx;
import com.just.stone.Manifest;
import com.just.stone.R;
import com.just.stone.activity.AnimationActivity;
import com.just.stone.activity.CustomViewActivity;
import com.just.stone.activity.ImageShowActivity;
import com.just.stone.activity.NotifyActivity;
import com.just.stone.activity.PermissionTestActivity;
import com.just.stone.activity.ScrollActivity;
import com.just.stone.activity.TestActivity;
import com.just.stone.async.Async;
import com.just.stone.broadcast.DeviceAdminSampleReceiver;
import com.just.stone.manager.AlarmTestManager;
import com.just.stone.manager.CustomProvider;
import com.just.stone.manager.ImageDownload;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.manager.UploadManager;
import com.just.stone.model.eventbus.OnListenerCreated;
import com.just.stone.service.ForegroundService;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.util.Msg;
import com.just.stone.util.ResourceUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangjw on 2016/8/25.
 */

public class Page1 extends Page {
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

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
        EventBus.getDefault().register(this);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
//            isRequireCheck = true;
//            allPermissionsGranted();
//        } else {
//            isRequireCheck = false;
//            showMissingPermissionDialog();
//        }
//    }
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

        mView.findViewById(R.id.tv_ps_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AppManagerUtil.showInstalledAppDetails("com.lm.powersecurity");
            }
        });

        mView.findViewById(R.id.tv_call_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + "13320605490"));
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_request_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mContext.startActivity(new Intent(mContext, PermissionTestActivity.class));
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
        mView.findViewById(R.id.tv_scroll_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ScrollActivity.class);
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_scroll_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(CustomProvider.name, "alpha");
                Uri uri = mContext.getContentResolver().insert(CustomProvider.CONTENT_URI, values);
                LogUtil.d("content", uri.toString());
                String[] projection =
                        {
                                "id",
                                "name"
                        };

                Cursor cursor = mContext.getContentResolver().query(CustomProvider.CONTENT_URI, projection, null, null, null);
                if (cursor.moveToFirst()){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    LogUtil.d("stone-content", "name: " + name);
                }
            }
        });

        mView.findViewById(R.id.tv_request_access_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });

        mView.findViewById(R.id.tv_test_notify_clone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, NotifyActivity.class));
            }
        });

        mView.findViewById(R.id.tv_send_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Async.schedule(5000, new Runnable() {
                    @Override
                    public void run() {
                        sendNotification(2014, "hello1", "this is message 1", "ok");
                        sendNotification(2015, "hello2", "this is message 2", "ok");
                        sendNotification(2016, "START", "START MAIN", "ok");
                        sendNotification(2017, "hello4", "", "");
                    }
                });
            }
        });

        mView.findViewById(R.id.tv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("upload", "start upload!");
                Msg.show(mView, "start upload!");
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

        mView.findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async.run(new Runnable() {
                    @Override
                    public void run() {
                        ImageDownload.download();
                    }
                });
            }
        });

        mView.findViewById(R.id.tv_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageShowActivity.class);
                mContext.startActivity(intent);
            }
        });

        mView.findViewById(R.id.tv_foreground_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AlarmTestManager.setAlarm();
                mContext.startService(new Intent(mContext, ForegroundService.class));
            }
        });
    }

    public void onEventMainThread(OnListenerCreated event){
        mContext.startActivity(new Intent(mContext, NotifyActivity.class));
    }

    private void sendNotification(int id, String title, String content, String actionText){
        Intent intent = new Intent(mContext, TestActivity.class);
        if (id == 2016){
            intent = mContext.getPackageManager().getLaunchIntentForPackage("com.lm.powersecurity");
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        RemoteViews remoteViews = new RemoteViews(ApplicationEx.getInstance().getPackageName(), R.layout.layout_notify_common_two_line);
        remoteViews.setImageViewResource(R.id.iv_icon, R.drawable.ic_ok);
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextColor(R.id.tv_title, ResourceUtil.getColor(R.color.color_E04E586A));

        if (!TextUtils.isEmpty(content)) {
            remoteViews.setTextViewText(R.id.tv_content, content);
        }
        remoteViews.setInt(R.id.tv_action, "setBackgroundResource", R.drawable.btn_red_selector_round100dp);
        remoteViews.setTextViewText(R.id.tv_action, actionText);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_checkbox_off)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent);

        NotificationManager mNotifyCenter = (NotificationManager) ApplicationEx.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyCenter.notify(id, builder.build());
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