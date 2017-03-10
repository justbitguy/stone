package com.just.stone.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.just.stone.R;
import com.just.stone.fragment.FragmentA;
import com.just.stone.fragment.FragmentB;

import java.security.PublicKey;

/**
 * Created by zhangjinwei on 2017/3/7.
 */

public class FragmentActivity extends Activity implements View.OnClickListener, FragmentA.OnActionListener{
    // TODO: 2017/3/7

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_a).setOnClickListener(this);
        findViewById(R.id.tv_b).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_a:
                switchToFragmentA();
                break;
            case R.id.tv_b:
                switchToFragmentB();
                break;
        }
    }

    private void switchToFragmentA() {
        // Create new fragment and transaction
        Fragment newFragment = new FragmentA();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void switchToFragmentB() {
        Fragment newFragment = new FragmentB();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAction(String msg) {
        
    }
}
