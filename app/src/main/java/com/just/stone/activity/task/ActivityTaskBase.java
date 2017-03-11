package com.just.stone.activity.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.just.stone.R;
import com.just.stone.constant.LocalPreferenceConstant;
import com.just.stone.manager.LocalPreferenceManager;
import com.just.stone.util.ToastUtil;

/**
 * Created by zhangjinwei on 2017/3/10.
 */

public class ActivityTaskBase extends Activity implements View.OnClickListener{

    public static final int START_BY_XML = 0;
    public static final int START_BY_FLAG = 1;
    public static final int START_BY_FLAG_CLEAR_TOP = 2;
    public static final int START_MODE_SIZE = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_base);
        updateTitle();
        bindAction();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_a:
                tryStartActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.start_a_1:
                tryStartActivity(new Intent(this, ActivityA_1.class));
                break;
            case R.id.start_b:
                tryStartActivity(new Intent(this, ActivityB.class));
                break;
            case R.id.start_b_1:
                tryStartActivity(new Intent(this, ActivityB_1.class));
                break;
            case R.id.start_c:
                tryStartActivity(new Intent(this, ActivityC.class));
                break;
            case R.id.start_d:
                tryStartActivity(new Intent(this, ActivityD.class));
                break;
            case R.id.tv_start_mode:
                switchStartMode();
                break;
            default:
                break;
        }
    }

    private void tryStartActivity(Intent intent) {
        int mode = LocalPreferenceManager.getInt(LocalPreferenceConstant.ACTIVITY_START_MODE, START_BY_XML);
        switch (mode){
            case START_BY_XML:
                startActivity(intent);
                break;
            case START_BY_FLAG:
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case START_BY_FLAG_CLEAR_TOP:
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void bindAction() {
        findViewById(R.id.start_a).setOnClickListener(this);
        findViewById(R.id.start_a_1).setOnClickListener(this);
        findViewById(R.id.start_b).setOnClickListener(this);
        findViewById(R.id.start_b_1).setOnClickListener(this);
        findViewById(R.id.start_c).setOnClickListener(this);
        findViewById(R.id.start_d).setOnClickListener(this);
        findViewById(R.id.tv_start_mode).setOnClickListener(this);
    }

    public void switchStartMode() {
        int mode = LocalPreferenceManager.getInt(LocalPreferenceConstant.ACTIVITY_START_MODE, START_BY_XML);
        mode = ++mode % START_MODE_SIZE;
        LocalPreferenceManager.putInt(LocalPreferenceConstant.ACTIVITY_START_MODE, mode);
        updateTitle();
    }


    private void updateTitle() {
        int mode = LocalPreferenceManager.getInt(LocalPreferenceConstant.ACTIVITY_START_MODE, START_BY_XML);
        switch (mode) {
            case START_BY_XML:
                ((TextView)findViewById(R.id.tv_start_mode)).setText("xml");
                break;
            case START_BY_FLAG:
                ((TextView)findViewById(R.id.tv_start_mode)).setText("flag");
                break;
            case START_BY_FLAG_CLEAR_TOP:
                ((TextView)findViewById(R.id.tv_start_mode)).setText("flag|clearTop");
                break;
        }
    }

    public void setTitle(String title) {
        ((TextView)findViewById(R.id.tv_title)).setText(title);
    }
}
