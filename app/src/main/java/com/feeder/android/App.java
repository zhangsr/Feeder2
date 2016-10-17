package com.feeder.android;

import android.app.Application;

import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.VolleySingleton;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: 7/22/16 初始化时机？
        ThreadManager.init();
        VolleySingleton.init(this);

        SPManager.init(this);
    }
}
