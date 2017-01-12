package com.just.stone.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangjinwei on 2016/12/2.
 */
public class MyParcelable implements Parcelable {
    private String mData;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mData);
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    private MyParcelable(Parcel in) {
        mData = in.readString();
    }

    public void setData(String data){
        mData = data;
    }

    public MyParcelable(String data) {
        mData = data;
    }
}
