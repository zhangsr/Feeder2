package com.feeder.common;

import android.os.Handler;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class ThreadManager {
    private static Handler mMainThreadHandler;

    public static void init() {
        mMainThreadHandler = new Handler();
    }

    public static void post(Runnable runnable) {
        mMainThreadHandler.post(runnable);
    }

    public static void postDelay(Runnable runnable, long delay) {
        mMainThreadHandler.postDelayed(runnable, delay);
    }
}
