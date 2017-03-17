package com.just.stone.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import com.just.stone.R;
import com.just.stone.util.LogUtil;

import java.security.PublicKey;

/**
 * Created by zhangjinwei on 2017/3/14.
 */

public class DownloadActivity extends Activity implements View.OnClickListener{

    BroadcastReceiver broadcastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        bindAction();
        registerBroadcast();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_download:
                download();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void bindAction() {
        findViewById(R.id.tv_download).setOnClickListener(this);
    }

    private void download() {
        // TODO: 2017/3/14
    }

    private void registerBroadcast() {
        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                LogUtil.d("download", "download id: " + id);
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }
}
