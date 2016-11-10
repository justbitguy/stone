package com.just.stone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.just.stone.R;
import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2016/9/26.
 */
public class MyCheck extends View {
    boolean mIsChecked = false;
    Context context;
    public MyCheck(Context context){
        super(context);
        init(context, null);
    }

    public MyCheck(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyCheck,
                0, 0
        );

        try {
            mIsChecked = a.getBoolean(R.styleable.MyCheck_isChecked, false);
        } catch (Exception e){
            LogUtil.error(e);
        }

        setBackgroundResource(mIsChecked ? R.drawable.ic_checkbox_on : R.drawable.ic_checkbox_off);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsChecked = !mIsChecked;
                setBackgroundResource(mIsChecked ? R.drawable.ic_checkbox_on : R.drawable.ic_checkbox_off);
            }
        });
    }
}
