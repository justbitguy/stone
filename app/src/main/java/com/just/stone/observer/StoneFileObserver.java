package com.just.stone.observer;

import android.os.FileObserver;

import com.just.stone.util.LogUtil;

/**
 * Created by zhangjinwei on 2017/3/20.
 */

public class StoneFileObserver extends FileObserver{

    public StoneFileObserver(String path) {
        super(path);
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event) {
            case FileObserver.ALL_EVENTS:
                LogUtil.d("file-observer", "path:"+ path);
                break;
            case FileObserver.CREATE:
                LogUtil.d("file-observer", "path:"+ path);
                break;
        }
    }
}
