package com.feeder.android;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.DBManager;
import com.feeder.domain.VolleySingleton;

import me.zsr.feeder.BuildConfig;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        ThreadManager.init();
        VolleySingleton.init(this);

        SPManager.init(this);

        DBManager.init(this);

        if (BuildConfig.DEBUG) {
            AVAnalytics.setAnalyticsEnabled(false);
        }
        AVOSCloud.initialize(this, BuildConfig.AVOS_APP_ID, BuildConfig.AVOS_CLIENT_KEY);
    }
}
