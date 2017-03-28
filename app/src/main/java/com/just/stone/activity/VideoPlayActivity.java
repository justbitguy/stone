package com.just.stone.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.just.stone.R;

import java.security.PublicKey;

/**
 * Created by zhangjinwei on 2017/3/24.
 */

public class VideoPlayActivity extends Activity implements View.OnClickListener{

    private static final String VIDEO_PATH = "/sdcard/DCIM/Camera/VID_20170324_164932.mp4";
    VideoView mVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        bindAction();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_play:
                play();
                break;
            case R.id.tv_btn_pause:
                pause();
                break;
        }
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.vv_video);
        mVideoView.setVideoPath(VIDEO_PATH);
        final MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer(mVideoView);
        mediaController.setAnchorView(mVideoView);
        mediaController.showContextMenu();
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
                mediaController.show(30000);
            }
        });
    }

    private void bindAction() {
        findViewById(R.id.tv_btn_play).setOnClickListener(this);
        findViewById(R.id.tv_btn_pause).setOnClickListener(this);
    }

    private void play() {
        mVideoView.start();
    }

    private void pause() {
        mVideoView.pause();
    }
}
