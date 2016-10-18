package com.just.stone.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.stone.R;
import com.just.stone.manager.InstalledPackageManager;
import com.just.stone.util.AppManagerUtil;
import com.just.stone.util.LogUtil;
import com.just.stone.util.ResourceUtil;
import com.just.stone.view.ViewHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangjinwei on 2016/9/18.
 */

public class AnimationActivity extends Activity{
    ImageView mStartImage;
    ImageView mEndImage;

    List<Drawable> mAppIconList = new ArrayList();
    GridView mGridView;
    BaseAdapter mGridViewAdapter;

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
//                startAnimation();
                startGridViewAnimation();
            }
        });

        mGridView = (GridView)findViewById(R.id.gv_hz);
        mGridViewAdapter = new MyAdapter();
        mGridView.setAdapter(mGridViewAdapter);

        List<PackageInfo> list = InstalledPackageManager.getInstance().getPackageInfoList();
        for (int i = 0; i < 5; ++i){
            PackageInfo info = list.get(i);
            Drawable icon = AppManagerUtil.getPackageIcon(info.packageName);
            mAppIconList.add(icon);
        }
    }

    private void startAnimation(){
        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.layout_center);
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        LogUtil.d("layout", "" + relativeLayout.getLeft() + ", " + relativeLayout.getTop() + ", " + relativeLayout.getRight() + " , " + relativeLayout.getRight());
        int centerX = relativeLayout.getLeft() + layoutParams.width / 2;
        int centerY = relativeLayout.getTop() + layoutParams.height / 2;

        ImageView image1 = (ImageView)findViewById(R.id.iv_app1);
        LogUtil.d("layout", "" + image1.getX() + ", " + image1.getY());
        ObjectAnimator anim1x = ObjectAnimator.ofFloat(image1, "x", image1.getX(), centerX - relativeLayout.getLeft() - image1.getWidth() / 2 );
        ObjectAnimator anim1y = ObjectAnimator.ofFloat(image1, "y", image1.getY(), centerY - relativeLayout.getTop() - image1.getHeight() / 2);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(anim1x).with(anim1y);
        animatorSet1.setDuration(2000);
        animatorSet1.start();

        ImageView image2 = (ImageView)findViewById(R.id.iv_app_2);
        LogUtil.d("layout", "" + image2.getX() + ", " + image2.getY());
        ObjectAnimator anim2x = ObjectAnimator.ofFloat(image2, "x", image2.getX(), centerX - relativeLayout.getLeft() - image2.getWidth() / 2 );
        ObjectAnimator anim2y = ObjectAnimator.ofFloat(image2, "y", image2.getY(), centerY - relativeLayout.getTop() - image2.getHeight() / 2);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.play(anim2x).with(anim2y);
        animatorSet2.setDuration(2000);
        animatorSet2.start();

        ImageView image3 = (ImageView)findViewById(R.id.iv_app_3);
        LogUtil.d("layout", "" + image3.getX() + ", " + image3.getY());
        ObjectAnimator anim3x = ObjectAnimator.ofFloat(image3, "x", image3.getX(), centerX - relativeLayout.getLeft() - image3.getWidth() / 2 );
        ObjectAnimator anim3y = ObjectAnimator.ofFloat(image3, "y", image3.getY(), centerY - relativeLayout.getTop() - image3.getHeight() / 2);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.play(anim3x).with(anim3y);
        animatorSet3.setDuration(2000);
        animatorSet3.start();

        ImageView image4 = (ImageView)findViewById(R.id.iv_app_4);
        LogUtil.d("layout", "" + image4.getX() + ", " + image4.getY());
        ObjectAnimator anim4x = ObjectAnimator.ofFloat(image4, "x", image4.getX(), centerX - relativeLayout.getLeft() - image4.getWidth() / 2 );
        ObjectAnimator anim4y = ObjectAnimator.ofFloat(image4, "y", image4.getY(), centerY - relativeLayout.getTop() - image4.getHeight() / 2);
        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.play(anim4x).with(anim4y);
        animatorSet4.setDuration(2000);
        animatorSet4.start();
    }

    private void startAnimationOld(){
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

    private void startGridViewAnimation(){
        View view = mGridView.getChildAt(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", view.getY(), view.getY() - 200);
        animator.setDuration(2000);
        animator.start();
    }

    private class MyAdapter extends BaseAdapter {
        private MyAdapter() {
        }

        @Override
        public int getCount() {
            return mAppIconList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppIconList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = AnimationActivity.this.getLayoutInflater().inflate(R.layout.layout_app_icon_item, null);
            }

            Drawable drawable = mAppIconList.get(position);
            ViewHolder.<ImageView>get(convertView, R.id.iv_image).setImageDrawable(drawable);
            return convertView;
        }
    }
}
