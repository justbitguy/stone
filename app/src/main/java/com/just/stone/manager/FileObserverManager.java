package com.just.stone.manager;

import com.just.stone.observer.StoneFileObserver;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangjinwei on 2017/3/20.
 */

public class FileObserverManager {

    private static FileObserverManager sInstance;

    private ConcurrentHashMap<String, StoneFileObserver> mObserverMap = new ConcurrentHashMap<>();

    private FileObserverManager () {
    }

    public static FileObserverManager getInstance() {
        synchronized (FileObserverManager.class) {
            if (sInstance == null) {
                sInstance = new FileObserverManager();
            }
        }
        return sInstance;
    }

    public boolean createFileObserver(String path) {
        if (mObserverMap.containsKey(path)) {
            return false;
        }
        StoneFileObserver observer = new StoneFileObserver(path);
        mObserverMap.put(path, observer);
        return true;
    }

    public StoneFileObserver getFileObserver(String path) {
        return mObserverMap.get(path);
    }
}
