package com.feeder.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.feeder.model.AccountDao;
import com.feeder.model.ArticleDao;
import com.feeder.model.DBOpenHelper;
import com.feeder.model.DaoMaster;
import com.feeder.model.DaoSession;
import com.feeder.model.SubscriptionDao;

/**
 * @description:
 * @author: Match
 * @date: 10/17/16
 */

public class DBManager {
    private static final String DB_NAME = "feeder";
    private static DaoSession sDaoSession;

    public static void init(Context context) {
        DaoMaster.OpenHelper helper = new DBOpenHelper(context, DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        sDaoSession = daoMaster.newSession();
    }

    public static SubscriptionDao getSubscriptionDao() {
        return sDaoSession.getSubscriptionDao();
    }

    public static ArticleDao getArticleDao() {
        return sDaoSession.getArticleDao();
    }

    public static AccountDao getAccountDao() {
        return sDaoSession.getAccountDao();
    }
}
