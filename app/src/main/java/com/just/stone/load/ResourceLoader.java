package com.just.stone.load;

/**
 * Created by zhangjinwei on 2016/6/21.
 */

import android.app.ActivityManager;;
import android.content.Context;
import android.os.Environment;
import android.util.LruCache;

import com.just.stone.ApplicationEx;
import com.just.stone.async.Async;
import com.just.stone.util.LogHelper;
import com.just.stone.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Resource Loader
 * - load from memory
 * - or load from disk
 * - or load from network
 */
public class ResourceLoader {
    private final static int MIN_MEMORY_CACHE_SIZE = 40 * 1024 * 1024;
    private final static int HTTP_CACHE_SIZE = 200 * 1024 * 1024;

    static LruCache<String, CacheResource> mMemoryCache;
    Object mDiskCacheLock = new Object();

    Context context = ApplicationEx.getInstance().getApplicationContext();
    File mCacheDirectory;
    int mCacheSize;

    private final Map<String, List<OnResourceLoadCallback>> mCallbackListMap = new HashMap<>();

    private void initCacheSetting(){
        String diskCacheDirStr;
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            diskCacheDirStr = externalFilesDir.getAbsolutePath() + "/cache/resource";
        } else {
            diskCacheDirStr = context.getFilesDir().getAbsolutePath() + "/cache/resource";
        }
        //int memoryCacheSize = (int)(((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024 * 0.3f);
        mCacheSize = 1024 * 1024 * 300;   // 300 megabytes
        mCacheDirectory = new File(diskCacheDirStr);
        mCacheDirectory.mkdirs();
    }

    public ResourceLoader(){
        if (mMemoryCache == null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int memClassBytes = am.getMemoryClass() * 1024 * 1024;
            int cacheSize = memClassBytes / 8;
            mMemoryCache = new LruCache<String, CacheResource>(cacheSize);
        }
        initCacheSetting();
    }

    private void invokeCallback(final OnResourceLoadCallback callback, final CacheResource resource){
        Async.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.onResourceLoad(resource);
            }
        });
    }

    private CacheResource getFromMemoryCache(String uri){
        String key = MD5Util.getMD5String(uri);
        CacheResource object = mMemoryCache.get(key);
        // TODO: 2016/6/21 是否需要考虑超时淘汰的问题
        return object;

    }
    public void get(final String uri, final OnResourceLoadCallback callback){
        if ((uri == null || uri.length() == 0)) {
            invokeCallback(callback, null);
            return;
        }

        final String key = MD5Util.getMD5String(uri);
//        Bitmap bitmap = getFromMemoryCache(key, uri, maxSize);
        CacheResource resource = getFromMemoryCache(uri);

        if (resource != null) {
            LogUtil.d("resource", "getFromMemoryCache");
            invokeCallback(callback, resource);
        } else {
            synchronized (mCallbackListMap) {
                List<OnResourceLoadCallback> callbackList = mCallbackListMap.get(key);
                if (callbackList != null) {
                    callbackList.add(callback);
                    LogUtil.d("resource", ">>>> [resource] there is already a task running for key: " + key + ", " + uri);
                    return;
                }

                callbackList = new ArrayList<>();
                mCallbackListMap.put(key, callbackList);
            }

            Async.run(new Runnable() {
                @Override
                public void run() {
                    final CacheResource resource = load(uri, key);
                    invokeCallback(callback, resource);

                    List<OnResourceLoadCallback> callbackList;
                    synchronized (mCallbackListMap) {
                        callbackList = mCallbackListMap.remove(key);
                    }
                    if (callbackList != null && callbackList.size() > 0) {
                        LogUtil.d("resource", String.format(">>>> [bitmap] doCallback, %s, %s, %d", key, uri, callbackList.size()));
                        for (int i = 0; i < callbackList.size(); ++i) {
                            invokeCallback(callbackList.get(i), resource);
                        }
                    }
                    if (resource != null){
                        Async.run(new Runnable() {
                            @Override
                            public void run() {
                                mMemoryCache.put(key, resource);
                                LogUtil.d("resource", "mMemoryCache.size: " + mMemoryCache.size());
                            }
                        });
                    }
                }
            });
        }
    }

    class CacheInterceptor implements Interceptor {
        private boolean fromCache = false;

        public CacheInterceptor(boolean fromCache){
            this.fromCache = fromCache;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (fromCache) {
                request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
            } else {
                request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
            }
            Response response = chain.proceed(request);
            Response response1 = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    //cache for 30 days
                    .header("Cache-Control", "max-age=" + 3600 * 24 * 30)
                    .build();

            return response1;
        }
    }

    private CacheResource load(String uri, String key){
        CacheResource resource = loadResource(uri, key, true);
        if (resource == null){
            LogUtil.d("resource", "load from network");
            loadResource(uri, key, false);
        }
        return resource;
    }

    private CacheResource loadResource(String uri, String key, boolean fromCache){
        CacheResource resource = null;
        Cache cache = new Cache(mCacheDirectory, mCacheSize);
        OkHttpClient okHttpClient = new OkHttpClient();

        OkHttpClient newClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(new CacheInterceptor(fromCache))
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(uri)
                .build();

        Response response = null;
        try {
            response = newClient.newCall(request).execute();
        } catch (IOException e){
            LogUtil.error(e);
        }
        if (response != null){
            try {
                resource = new CacheResource();
                resource.addBytes(response.body().bytes());
            } catch (Exception e){
                LogUtil.error(e);
            }
        }
        return resource;
    }
}
