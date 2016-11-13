package com.feeder.android;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();

        ThreadManager.init();
        VolleySingleton.init(this);

        SPManager.init(this);

        DBManager.init(this);

        if (BuildConfig.DEBUG) {
            AVAnalytics.setAnalyticsEnabled(false);
        }
        AVOSCloud.initialize(this, "ms2lsbjilfbqjeb5fitysvm0lkt38nnw2bvwe60sy7j5g50t",
                "84gf4pv73s99zme304ks1e5f5qwdpls1exgg5cx7c2rah0u4");
    }
}
