package com.just.stone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2017/3/15.
 */

public class NewIntentTestActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_intent_test);
        findViewById(R.id.tv_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.tv_1).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        findViewById(R.id.tv_1).setVisibility(View.VISIBLE);
    }
}
