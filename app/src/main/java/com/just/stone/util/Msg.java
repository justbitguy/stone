package com.just.stone.util;

import android.view.View;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.async.Async;

import org.w3c.dom.Text;

/**
 * Created by Zac on 2016/8/16.
 */

public class Msg {

    public static void show(final View container, final String msg){
        if (container == null){
            return;
        }
        Async.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView)container.findViewById(R.id.tv_message);
                if (tv != null){
                    tv.append(msg);
                }
            }
        });
    }
}
