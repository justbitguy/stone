package com.just.stone.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.just.stone.ApplicationEx;
import com.just.stone.R;
import com.just.stone.activity.MainActivity;

/**
 * Created by zhangjinwei on 2017/2/15.
 */

public class ForegroundService extends Service{

    private static final int FOREGROUND_NOTIFICATION_ID = 12301;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();

        keepForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

        startService(new Intent(this, ForegroundService.class));

    }

    private void keepForeground() {
        Resources resources = ApplicationEx.getInstance().getResources();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))
                .setContentTitle(resources.getText(R.string.app_name))
                .setContentIntent(pendingIntent);

        //RemoteViews remoteView = inflateForegroundToolBarView(true);
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notify_common_two_line);
        mBuilder.setContent(remoteViews);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        Notification notification = mBuilder.getNotification();
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
//            boolean isTr = LanguageSettingUtil.get().getLanguage().equals("tr");
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);
    }
}
