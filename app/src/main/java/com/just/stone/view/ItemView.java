package com.just.stone.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2017/2/3.
 */
public class ItemView extends LinearLayout {

    public ItemView(Context context){
        super(context);
        init(context, null);
    }

    public ItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ItemView,
                0, 0
        );

        String text = a.getString(R.styleable.ItemView_text);
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
        ((TextView)itemView.findViewById(R.id.tv_content)).setText(text);
        addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
}
