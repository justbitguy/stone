package com.just.stone.page;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.activity.ImageShowActivity;
import com.just.stone.async.Async;
import com.just.stone.manager.ImageDownload;
import com.just.stone.manager.UploadManager;
import com.just.stone.util.LogUtil;
import com.just.stone.util.Msg;

/**
 * Created by Zac on 2016/8/16.
 */

public class Page3 extends Page{

    public Page3(Activity context, int mViewId){
        super(context, mViewId);
    }

    @Override
    protected  void init(){
        super.init();
    }
    @Override
    protected void initData(){
        super.initData();
    }

    @Override
    protected void initView(){
        super.initView();
        mView.findViewById(R.id.tv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("upload", "start upload!");
                Msg.show(mView, "start upload!");
                Async.run(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            UploadManager.upLoadByCommonPost();
                        } catch (Exception e){
                            LogUtil.error(e);
                        }
                    }
                });
            }
        });

        mView.findViewById(R.id.tv_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async.run(new Runnable() {
                    @Override
                    public void run() {
                        ImageDownload.download();
                    }
                });
            }
        });

        mView.findViewById(R.id.tv_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageShowActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPageSelected(){
        // TODO: 2016/8/16
    }

}
