package com.feeder.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class ThreadManager {
    // TODO: 11/15/16 multi thread, handle synchronized
    public static ExecutorService mExecutorService = Executors.newFixedThreadPool(5);
    private static Handler mMainThreadHandler;

    private static Handler mBackgroundHandler;

    public static void init() {
        mMainThreadHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mBackgroundHandler = new Handler();
                Looper.loop();
            }
        }).start();
    }

    public static void post(Runnable runnable) {
        mMainThreadHandler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long delay) {
        mMainThreadHandler.postDelayed(runnable, delay);
    }

    public static void postInBackground(Runnable runnable) {
//        mExecutorService.execute(runnable);
        mBackgroundHandler.post(runnable);
    }
}
