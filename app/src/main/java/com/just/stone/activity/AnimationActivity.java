package com.just.stone.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.just.stone.R;

/**
 * Created by zhangjinwei on 2016/9/18.
 */

public class AnimationActivity extends Activity{
    ImageView mStartImage;
    ImageView mEndImage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initView();
    }

    private void initView(){
        findViewById(R.id.tv_start_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    private void startAnimation(){
        mStartImage = (ImageView)findViewById(R.id.iv_start);
        float startHeight = mStartImage.getHeight();
        float startX = mStartImage.getX();
        float startY = mStartImage.getY();

        mEndImage = (ImageView)findViewById(R.id.iv_end);
        float endHeight = mEndImage.getHeight();
        float endX = mEndImage.getX();
        float endY = mEndImage.getY();

//        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mStartImage, "alpha", 1f, 0f);
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mStartImage, "scale", 1f, );

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mStartImage, "x", startX, endX);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mStartImage, "y", startY, endY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mStartImage, "scaleX", 1f, endHeight / startHeight);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mStartImage, "scaleY", 1f, endHeight / startHeight);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2).with(scaleX).with(scaleY);
        animSet.setDuration(2000);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mStartImage.setVisibility(View.INVISIBLE);
                mEndImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animSet.start();
    }
}
