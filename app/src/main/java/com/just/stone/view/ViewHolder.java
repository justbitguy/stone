package com.just.stone.view;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Zac on 2016/8/1.
 */

public class ViewHolder {
    private static final int TAG_KEY = 0x7f000000;//viewHolder cache key

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        //we use view.getTag(int key) for cache, so we can use getTag() for other use
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(TAG_KEY);
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>(5);
            view.setTag(TAG_KEY, viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }
}