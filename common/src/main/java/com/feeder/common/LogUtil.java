package com.feeder.common;

import android.util.Log;

/**
 * Description: More powerful logger.
 * Author: Match
 * Date: 15-4-25
 */
public class LogUtil {
    private static String DEFAULT_TAG = "Feeder";
    private static boolean sLogAll = false;
    private static final boolean LOGD_DEBUG = true;
    private static final boolean LOGI_DEBUG = true;
    private static final boolean LOGW_DEBUG = true;
    private static final boolean LOGE_DEBUG = true;
    private static final int STACK_INDEX = 3;

    public static void enable(boolean enable) {
        sLogAll = enable;
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    /**
     * Used for persistent log
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (LOGD_DEBUG && sLogAll) {
            Log.d(tag, getInformation(msg));
        }
    }

    /**
     * Used for temp log
     * @param msg
     */
    public static void i(String msg) {
        if (LOGI_DEBUG && sLogAll) {
            Log.i(DEFAULT_TAG, getInformation(msg));
        }
    }

    /**
     * Used for exception log
     * @param msg
     */
    public static void w(String msg) {
        if (LOGW_DEBUG && sLogAll) {
            Log.e(DEFAULT_TAG, getInformation(msg));
        }
    }

    /**
     * Used for exception log
     * @param msg
     */
    public static void e(String msg) {
        if (LOGE_DEBUG && sLogAll) {
            Log.e(DEFAULT_TAG, getInformation(msg));
        }
    }

    private static String getInformation(String msg) {
        Exception exception = new Exception();
        return exception.getStackTrace()[STACK_INDEX].getFileName() + "|"
                + exception.getStackTrace()[STACK_INDEX].getLineNumber() + "|" + msg;
    }
}
