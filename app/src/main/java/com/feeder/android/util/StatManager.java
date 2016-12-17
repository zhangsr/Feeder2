package com.feeder.android.util;

import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

import me.zsr.feeder.BuildConfig;

/**
 * @description:
 * @author: Match
 * @date: 11/20/16
 */

public class StatManager {
    public static final String EVENT_SET_HTML_ERROR = "set_html_error";

    public static void init(Context context) {
        if (BuildConfig.DEBUG) {
            AVAnalytics.setAnalyticsEnabled(false);
            AVAnalytics.enableCrashReport(context, false);
        } else {
            AVOSCloud.initialize(context, BuildConfig.AVOS_APP_ID, BuildConfig.AVOS_CLIENT_KEY);
        }
    }

    public static void trackAppOpened(Intent intent) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.trackAppOpened(intent);
        }
    }

    public static void statEvent(Context context, String event, String tag) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onEvent(context, event, tag);
        }
    }

    public static void onPause(Context context) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onPause(context);
        }
    }

    public static void onResume(Context context) {
        if (!BuildConfig.DEBUG) {
            AVAnalytics.onResume(context);
        }
    }
}
