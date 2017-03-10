package com.just.stone.activity.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2017/3/10.
 */

public class ActivityTaskBase extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_base);
        bindAction();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_a:
                startActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.start_b:
                startActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.start_b_1:
                startActivity(new Intent(this, ActivityB_1.class));
                break;
            case R.id.start_c:
                startActivity(new Intent(this, ActivityC.class));
                break;
            case R.id.start_d:
                startActivity(new Intent(this, ActivityD.class));
                break;
            default:
                break;
        }
    }

    public void bindAction() {
        findViewById(R.id.start_a).setOnClickListener(this);
        findViewById(R.id.start_b).setOnClickListener(this);
        findViewById(R.id.start_b_1).setOnClickListener(this);
        findViewById(R.id.start_c).setOnClickListener(this);
        findViewById(R.id.start_d).setOnClickListener(this);
    }

    public void setTitle(String title) {
        ((TextView)findViewById(R.id.tv_title)).setText(title);
    }
}
