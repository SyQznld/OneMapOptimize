package com.oneMap.module.common.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.oneMap.module.common.utils.UpgradeHelper;
import com.oneMap.module.common.utils.Utils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.List;

import onemap.greendao.gen.DaoMaster;
import onemap.greendao.gen.DaoSession;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 */
public class BaseApplication extends Application {

    private String TAG = getClass().getSimpleName();
    public static final String ROOT_PACKAGE = "com.oneMap.module";

    private static BaseApplication baseApplication;

    private List<IApplicationDelegate> mAppDelegateList;


    private static DaoSession daoSession;
    private Context context;



    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        context = this;
        Logger.init("pattern").logLevel(LogLevel.FULL);
        Utils.init(this);
        mAppDelegateList = ClassUtils.getObjectsWithInterface(this, IApplicationDelegate.class, ROOT_PACKAGE);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onCreate();
        }

        setupDatabase();


        // 百度地图初始化
        SDKInitializer.initialize(this);

        //捕捉崩溃日志
//        CrashHandlerUtil crashHandlerUtil = new CrashHandlerUtil();
//        crashHandlerUtil.init(context);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTerminate();
        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for (IApplicationDelegate delegate : mAppDelegateList) {
            delegate.onTrimMemory(level);
        }
    }


    private void setupDatabase() {
        UpgradeHelper helper = new UpgradeHelper(context, "onemap.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();

    }
}
