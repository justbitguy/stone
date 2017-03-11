package com.just.stone.util;

import android.content.Context;
import android.widget.Toast;

import com.just.stone.ApplicationEx;

/**
 * Created by zhangjinwei on 2017/3/10.
 */

public class ToastUtil {

    public static void showToast(String text, int lengthType) {
        Context context = ApplicationEx.getInstance();
        Toast toast = Toast.makeText(context, text, lengthType);
        toast.show();
    }
}
