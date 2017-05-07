package com.feeder.android;

import android.app.Application;

import com.feeder.android.util.ImageLoaderManager;
import com.feeder.common.LogUtil;
import com.feeder.android.util.StatManager;
import com.feeder.common.SPManager;
import com.feeder.common.ThreadManager;
import com.feeder.domain.AssertManager;
import com.feeder.domain.DBManager;
import com.feeder.domain.model.AccountModel;
import com.feeder.domain.net.VolleySingleton;

import me.zsr.feeder.BuildConfig;
import me.zsr.feeder.R;

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

        // Need to init first start ******************

        ThreadManager.init();
        VolleySingleton.init(this);

        SPManager.init(this);

        DBManager.init(this);

        StatManager.init(this);

        ImageLoaderManager.init(this);

        AssertManager.setEnabled(BuildConfig.DEBUG);

        // Need to init first end ******************

        AccountModel.setDefaultAccountName(getResources().getString(R.string.default_account_name));
    }
}
