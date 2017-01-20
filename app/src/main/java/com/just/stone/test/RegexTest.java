package com.just.stone.test;

import com.just.stone.util.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangjinwei on 2017/1/20.
 */

public class RegexTest {
    public static final String TAG =  "regex-test";

    public static void  startTest() {
        String str = "Android.Smssend.xxx.Origin";
        String pattern = "Android\\.Smssend\\.(.*)\\.(Origin)";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(str);
        if (matcher.find()){
            for (int i = 0 ; i <= matcher.groupCount(); ++i){
                LogUtil.d(TAG, matcher.group(i));
            }
        }
    }
}
