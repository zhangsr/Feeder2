package com.feeder.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * @description:
 * @author: Match
 * @date: 1/22/17
 */

public class DBOpenHelper extends DaoMaster.OpenHelper {
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        MigrationHelper.getInstance().migrate(db,
                AccountDao.class,
                ArticleDao.class,
                SubscriptionDao.class);
    }
}
