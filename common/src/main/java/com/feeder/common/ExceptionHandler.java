package com.feeder.common;

import android.content.ActivityNotFoundException;

/**
 * @description:
 * @author: Match
 * @date: 3/19/17
 */

public class ExceptionHandler {

    public static void silentHandle(Exception e) {
        if (e instanceof ActivityNotFoundException) {

        }
    }
}
