package com.just.stone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.just.stone.R;
import com.just.stone.manager.FileObserverManager;

/**
 * Created by zhangjinwei on 2017/3/20.
 */

public class FileObserveActivity extends Activity implements View.OnClickListener{

    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_oberve);
        bindAction();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                startWatching();
                break;
            case R.id.tv_stop:
                stopWatching();
                break;

        }
    }

    private void bindAction() {
        findViewById(R.id.tv_start).setOnClickListener(this);
        findViewById(R.id.tv_stop).setOnClickListener(this);
    }

    private void startWatching() {
        FileObserverManager.getInstance().getFileObserver(mPath).startWatching();
    }

    private void stopWatching() {
        FileObserverManager.getInstance().getFileObserver(mPath).stopWatching();
    }

    private void initData() {
        mPath = Environment.getExternalStoragePublicDirectory("Download").getAbsolutePath();
        FileObserverManager.getInstance().createFileObserver(mPath);
    }
}
