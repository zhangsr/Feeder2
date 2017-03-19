package com.feeder.android.util;

import android.content.ActivityNotFoundException;
import android.content.Context;

import com.feeder.common.ExceptionHandler;

/**
 * @description:
 * @author: Match
 * @date: 3/19/17
 */

public class ExceptionHandlerEx extends ExceptionHandler{

    public static void statException(Context context, Exception e) {
        silentHandle(e);

        if (e instanceof ActivityNotFoundException) {
            StatManager.statEvent(context, StatManager.EXCEPTION_ACTIVITY_NOT_FOUND);
        }
    }
}
