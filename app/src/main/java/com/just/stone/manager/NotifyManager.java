package com.just.stone.manager;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.just.stone.ApplicationEx;
import com.just.stone.R;
import com.just.stone.model.eventbus.OnNotifyEvent;
import com.just.stone.util.LogUtil;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import de.greenrobot.event.EventBus;

/**
 * Created by zhangjinwei on 2016/11/11.
 */

public class NotifyManager {
    private static NotifyManager sInstance;

    private String mPackageName;
    private RemoteViews mView;
    public PendingIntent mIntent;

    private NotifyManager(){
        EventBus.getDefault().register(this);
    }

    public static NotifyManager getInstance(){
        synchronized (NotifyManager.class){
            if (sInstance == null){
                sInstance = new NotifyManager();
            }
        }
        return sInstance;
    }

    public void onEventAsync(OnNotifyEvent event){
        StatusBarNotification sbn = event.getNotification();;
        Notification nt = sbn.getNotification();

        LogUtil.d("zpf", "event: id " + sbn.getId() + ", " + sbn.getPackageName());
        if (sbn.getPackageName().equals("com.lm.powersecurity") && sbn.getId() == 32){
            if (sbn.getNotification() != null && sbn.getNotification().contentView != null){
                mView = sbn.getNotification().contentView;
                LogUtil.d("zpf", "tickerText: " + nt.tickerText);
                mIntent = nt.contentIntent;
                extractText(sbn.getNotification(),sbn.getNotification().contentView);
//                extractText2(sbn.getNotification());
                getText(sbn.getNotification());
            }
        }
    }


    public String getPackageName(){
        return mPackageName;
    }

    public RemoteViews getView(){
        return mView;
    }


    private void extractText(Notification notification, RemoteViews views) {
        Class secretClass = views.getClass();
        try {
            Map<Integer, String> text = new HashMap<Integer, String>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions")) continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
                        .get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    Integer type = null;
                    Integer viewId = null;
                    Integer size = -1;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        } else if (field.getName().equals("viewId")) {
                            viewId = field.getInt(action);
                        } else if (field.getName().equals("size")){
                            size = field.getInt(action);
                        }
                    }

//                    if (type == 9 || type == 10) {
                        text.put(viewId, value.toString());
                        LogUtil.d("zpf", "viewId: " + viewId + ", string: " + value.toString() + ", type: " + type + ", size: " + size);
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void extractText2(Notification notify){
//        Bundle extras = notify.extras;
//        if (extras != null) {
//            String title = extras.getString("android.title");
//            String text = extras.getString("android.text");
//            LogUtil.d("zpf", "title: " + title + ", text: " + text);
//        }
//    }

    public static List<String> getText(Notification notification)
    {
        // We have to extract the information from the view
        RemoteViews        views = notification.bigContentView;
        if (views == null) views = notification.contentView;
        if (views == null) return null;

        // Use reflection to examine the m_actions member of the given RemoteViews object.
        // It's not pretty, but it works.
        List<String> text = new ArrayList<String>();
        try
        {
            Field field = views.getClass().getDeclaredField("mActions");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);

            // Find the setText() and setTime() reflection actions
            for (Parcelable p : actions)
            {
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                parcel.setDataPosition(0);

//                LogUtil.d("zpf", "getText, methodName: " + parcel.readString());

                // The tag tells which type of action it is (2 is ReflectionAction, from the source)
                int tag = parcel.readInt();
//                if (tag != 2) continue;

                // View ID
                parcel.readInt();

                String methodName = parcel.readString();
                LogUtil.d("zpf", "getText, methodName: " + parcel.readString());

                if (methodName == null) {
                    continue;
                }
                    // Save strings
                else if (methodName.equals("setText"))
                {
                    // Parameter type (10 = Character Sequence)
                    parcel.readInt();

                    // Store the actual string
                    String t = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString().trim();
                    text.add(t);
                }

                // Save times. Comment this section out if the notification time isn't important
                else if (methodName.equals("setTime"))
                {
                    // Parameter type (5 = Long)
                    parcel.readInt();

//                    String t = new SimpleDateFormat("h:mm a").format(new Date(parcel.readLong()));
//                    text.add(t);
                }

                parcel.recycle();
            }
        }

        // It's not usually good style to do this, but then again, neither is the use of reflection...
        catch (Exception e)
        {
            //Log.e("NotificationClassifier", e.toString());
        }

        LogUtil.d("zpf", "text: " + text);
        return text;
    }
}
