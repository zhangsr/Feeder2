package com.feeder.android.utils;

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

    public static void init(Context context) {
        if (BuildConfig.DEBUG) {
            AVAnalytics.setAnalyticsEnabled(false);
            AVAnalytics.enableCrashReport(context, false);
            AVOSCloud.initialize(context, BuildConfig.AVOS_APP_ID, BuildConfig.AVOS_CLIENT_KEY);
        }
    }

    public static void trackAppOpened(Intent intent) {
        if (BuildConfig.DEBUG) {
            AVAnalytics.trackAppOpened(intent);
        }
    }
}
