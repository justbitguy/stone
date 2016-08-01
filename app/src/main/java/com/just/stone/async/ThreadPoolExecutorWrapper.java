package com.just.stone.async;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class encapsulates 3 kinds of methods to "execute" code in background
 * thread, "execute" code in UI thread and "schedule" code to be run in the future.
 */

// an asynchronous task executor(thread pool)
public class ThreadPoolExecutorWrapper {
    private static final Long IDLE_THREAD_KEEP_ALIVE_TIME = 60L;
    private ExecutorService mThreadPoolExecutor;//normal thread pool
    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;//can schedule task thread pool
    private Handler mMainHandler;
    private TaskQueue mActionQueue;
    private Map<Runnable, Object> mScheduledJobRecord = new HashMap<>();//ScheduledThreadPoolExecutor will wrap Runnable, so we record this
    private Object mScheduledJobRecordMutex = new Object();

    /*
        maxThreadCount:thread pool max thread count
        activeThreadCount:thread pool min thread count even if all thread is idle
        IDLE_THREAD_KEEP_ALIVE_TIME:IDLE thread will be shutdown when TIME_OUT if (maxThreadCount - activeThreadCount) > 0
     */
    public ThreadPoolExecutorWrapper(int activeThreadCount, int maxThreadCount, int maxScheTaskThread) {
        mThreadPoolExecutor = new ThreadPoolExecutor(activeThreadCount, maxThreadCount,
                IDLE_THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new GameBoosterThreadFactory(Thread.MIN_PRIORITY));

        if (maxScheTaskThread > 0) {
            mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(maxScheTaskThread);
        }

        mMainHandler = new Handler(Looper.getMainLooper());
        mActionQueue = new TaskQueue(Async.class.getName());
        mActionQueue.start();
    }

    public void executeTask(Runnable task) {
        if (task == null) {
            return;
        }

        mThreadPoolExecutor.execute(task);
    }

    public <T> Future<T> submitTask(Callable <T> task) {
        return mThreadPoolExecutor.submit(task);
    }

    public void scheduleTask(long delay, Runnable task) {
        if (task == null) {
            return;
        }

        synchronized (mScheduledJobRecordMutex) {
            if (mScheduledJobRecord.containsKey(task)) {
                ScheduledFuture<?> internalJob = (ScheduledFuture<?>) mScheduledJobRecord.get(task);
                internalJob.cancel(true);
                mScheduledJobRecord.remove(task);
            }

            mScheduledJobRecord.put(task, mScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS));
        }
    }

    public void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long initialDelay, long period, Runnable task) {
        if (task == null) {
            return;
        }

        synchronized (mScheduledJobRecordMutex) {
            if (mScheduledJobRecord.containsKey(task)) {
                return;
            }

            mScheduledJobRecord.put(task, mScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS));
        }
    }

    public void scheduleTaskAtFixedRateIncludingTaskRunningTime(long initialDelay, long period, Runnable task) {
        if (task == null) {
            return;
        }

        synchronized (mScheduledJobRecordMutex) {
            if (mScheduledJobRecord.containsKey(task)) {
                return;
            }
            mScheduledJobRecord.put(task, mScheduledThreadPoolExecutor.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS));
        }
    }

    public boolean removeScheduledTask(Runnable task) {
        if (task == null) {
            return false;
        }

        synchronized (mScheduledJobRecordMutex) {
            if (!mScheduledJobRecord.containsKey(task)) {
                return false;
            }

            ScheduledFuture<?> internalJob = (ScheduledFuture<?>) mScheduledJobRecord.get(task);
            internalJob.cancel(true);
            mScheduledJobRecord.remove(task);
            return true;
        }
    }

    public void scheduleTaskOnUiThread(long delay, Runnable task) {
        if (task == null) {
            return;
        }
        mMainHandler.postDelayed(task, delay);
    }

    public void removeScheduledTaskOnUiThread(Runnable task) {
        if (task == null) {
            return;
        }
        mMainHandler.removeCallbacks(task);
    }

    public void runTaskOnUiThread(Runnable task) {
        if (task == null) {
            return;
        }

        mMainHandler.post(task);
    }

    public Handler getMainHandler() {
        return mMainHandler;
    }

    public void scheduleOnQueue(Runnable task) {
        if (task == null) {
            return;
        }

        mActionQueue.scheduleTask(task);
    }

    public void shutdown() {
        if (mThreadPoolExecutor != null) {
            mThreadPoolExecutor.shutdown();
            mThreadPoolExecutor = null;
        }

        if (mScheduledThreadPoolExecutor != null) {
            mScheduledThreadPoolExecutor.shutdown();
            mScheduledThreadPoolExecutor = null;
        }

        if (mActionQueue != null) {
            mActionQueue.stopTaskQueue();
        }
    }
}
