package com.just.stone.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.just.stone.ApplicationEx;
import com.just.stone.R;
import com.just.stone.manager.NotifyManager;
import com.just.stone.model.eventbus.OnNotifyEvent;
import com.just.stone.util.LogUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangjinwei on 2016/11/11.
 */

public class NotifyActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        initView();
    }

    private void initView(){

        LinearLayout parent = (LinearLayout)findViewById(R.id.layout_notify_container);

        if (NotifyManager.getInstance().getView() != null) {
            View view = NotifyManager.getInstance().getView().apply(NotifyActivity.this, parent);
            parent.addView(view);
        }
//        loopViews((ViewGroup)view);

        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NotifyManager.getInstance().mIntent.send();
                } catch (Exception e){

                }
            }
        });
    }

//    Notification notification = (Notification) event.getParcelableData();
//    RemoteViews views = notification.contentView;

    private void loopViews(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View v = view.getChildAt(i);

            if (v instanceof TextView) {
                // Do something
                LogUtil.d("zpf", "loopViews: " + ((TextView) v).getText());

            } else if (v instanceof ViewGroup) {

                this.loopViews((ViewGroup) v);
            }
        }
    }

}
