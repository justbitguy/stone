package com.just.stone.manager;

/**
 * Created by zhangjinwei on 2017/3/17.
 */

public class MediaFileManager {

    private static MediaFileManager sInstance;

    private MediaFileManager() {
    }

    public MediaFileManager getInstance() {
        synchronized (MediaFileManager.class) {
            if (sInstance == null) {
                sInstance = new MediaFileManager();
            }
        }
        return sInstance;
    }

    // TODO: 2017/3/17  
}
