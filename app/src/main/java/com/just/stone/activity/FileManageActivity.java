package com.just.stone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.util.LogUtil;

import java.io.File;
import java.security.PublicKey;

/**
 * Created by zhangjinwei on 2017/3/14.
 */

public class FileManageActivity extends Activity implements View.OnClickListener{
    String msg = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manage);
        bindAction();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_action:
                action();
                break;
            default:
                break;
        }
    }
    private void bindAction() {
        findViewById(R.id.tv_action).setOnClickListener(this);
    }

    private void action() {
        addToMsg("getDataDirectory", Environment.getDataDirectory().toString());
        addToMsg("getDownloadCacheDirectory", Environment.getDownloadCacheDirectory().toString());
        addToMsg("getExternalStorageDirectory", Environment.getExternalStorageDirectory().toString());
        addToMsg("getExternalStoragePublicDirectory", Environment.getExternalStoragePublicDirectory("ddd").toString());
        addToMsg("getRootDirectory", Environment.getRootDirectory().toString());
        addToMsg("getFilesDir", getFilesDir().toString());
        addToMsg("getCacheDir", getCacheDir().toString());
        addToMsg("getExternalFilesDir", getExternalFilesDir(null).toString());
        addToMsg("getExternalFilesDir(Download)", getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        addToMsg("getExternalCacheDir", getExternalCacheDir().toString());
        show(msg);
        LogUtil.d("file-manage", msg);
    }

    private void addToMsg(String title, String content) {
        msg += title + ": " + content + "\n";
    }

    private void show(String msg) {
        ((TextView)findViewById(R.id.tv_content)).setText(msg);
    }
}
