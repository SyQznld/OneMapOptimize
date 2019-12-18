package com.oneMap.module.common.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

import onemap.greendao.gen.BzDataDao;
import onemap.greendao.gen.DaoMaster;
import onemap.greendao.gen.GatherDataDao;
import onemap.greendao.gen.LoginDataDao;


public class UpgradeHelper extends DaoMaster.OpenHelper {
    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory) {
        super(context, name, cursorFactory);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);

        Database database = new StandardDatabase(db);
        MigrationHelper.getInstance().migrate(database, LoginDataDao.class, BzDataDao.class, GatherDataDao.class);

    }
}
