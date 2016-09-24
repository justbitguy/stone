package com.just.stone.activity;

import android.app.Activity;
import android.os.Bundle;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2016/9/20.
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView(){

    }
}


