package com.just.stone.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * This class encapsulates 3 kinds of methods to "execute" code in background
 * thread, "execute" code in UI thread and "schedule" code to be run in the future.
 */

// an asynchronous task executor(thread pool)
public class Async {
    private static ThreadPoolExecutorWrapper sThreadPoolExecutorWrapper;

    private static ThreadPoolExecutorWrapper getThreadPoolExecutorWrapper() {
        if (sThreadPoolExecutorWrapper == null) {
            synchronized (Async.class) {
                if (sThreadPoolExecutorWrapper == null) {
                    sThreadPoolExecutorWrapper = new ThreadPoolExecutorWrapper(4, 8, 2);
                }
            }
        }

        return sThreadPoolExecutorWrapper;
    }

    public static void run(Runnable task) {
        getThreadPoolExecutorWrapper().executeTask(task);
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return getThreadPoolExecutorWrapper().submitTask(task);
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void schedule(long delay, Runnable task) {
        getThreadPoolExecutorWrapper().scheduleTask(delay, task);
    }

    public static void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long initialDelay, long period, Runnable task) {
        getThreadPoolExecutorWrapper().scheduleTaskAtFixedRateIgnoringTaskRunningTime(initialDelay, period, task);
    }

    public static void scheduleTaskAtFixedRateIncludingTaskRunningTime(long initialDelay, long period, Runnable task) {
        getThreadPoolExecutorWrapper().scheduleTaskAtFixedRateIncludingTaskRunningTime(initialDelay, period, task);
    }

    public static boolean removeScheduledTask(Runnable task) {
        return getThreadPoolExecutorWrapper().removeScheduledTask(task);
    }

    public static void scheduleTaskOnUiThread(long delay, Runnable task) {
        getThreadPoolExecutorWrapper().scheduleTaskOnUiThread(delay, task);
    }

    public static void removeScheduledTaskOnUiThread(Runnable task) {
        getThreadPoolExecutorWrapper().removeScheduledTaskOnUiThread(task);
    }

    public static void runOnUiThread(Runnable task) {
        getThreadPoolExecutorWrapper().runTaskOnUiThread(task);
    }

    public static Handler getMainHandler() {
        return getThreadPoolExecutorWrapper().getMainHandler();
    }

    /*
    single background thread to execute Job
     */
    public static void scheduleInQueue(Runnable task) {
        getThreadPoolExecutorWrapper().scheduleOnQueue(task);
    }

    public static void shutdown() {
        if (sThreadPoolExecutorWrapper != null) {
            sThreadPoolExecutorWrapper.shutdown();
            sThreadPoolExecutorWrapper = null;
        }
    }
}
