package com.feeder.android;

import android.app.Application;

import com.feeder.android.util.ImageLoaderManager;
import com.feeder.common.LogUtil;
import com.feeder.android.util.StatManager;
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

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.enable(BuildConfig.DEBUG);
        LogUtil.d("App Init");

        ThreadManager.init();
        VolleySingleton.init(this);

        SPManager.init(this);

        DBManager.init(this);

        StatManager.init(this);

        ImageLoaderManager.init(this);
    }
}
