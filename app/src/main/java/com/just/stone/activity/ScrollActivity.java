package com.just.stone.activity;

import android.app.Activity;
import android.os.Bundle;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2016/11/3.
 */

public class ScrollActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
