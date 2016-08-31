package com.just.stone.load;

import android.util.Log;

import com.just.stone.util.LogUtil;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by zhangjinwei on 2016/6/21.
 */
public class CacheResource {
    private static final int INIT_SIZE = 1024;

    private byte[] mData;
    private int mCapacity;
    private int length;

    public CacheResource(){
        mCapacity = INIT_SIZE;
        length = 0;
        mData = new byte[mCapacity];
        Log.d("resource", "" + mData.length);
    }

    public void addBytes(final byte[] bytes){
        if (length + bytes.length > mCapacity){
            mCapacity = length + bytes.length;
            mData = Arrays.copyOfRange(mData, 0, mCapacity);
        }
        System.arraycopy(bytes, 0, mData, length, bytes.length);
        length += bytes.length;
    }

    public byte[] data(){
        return mData;
    }

    public int size(){
        return length;
    }

    public void printData(){
        LogUtil.d("resource", "" + mData.toString());
    }
}
