package com.oneMap.module.common.dao;


import com.oneMap.module.common.bean.LoginData;
import com.oneMap.module.common.base.BaseApplication;

import java.util.List;

/**
 * 保存用户信息  数据库 基本操作
 */

public class LoginDao {
    /**
     * 添加数据，如果有重复则覆盖
     *
     * @param data
     */
    public static void insertLoginData(LoginData data) {
        BaseApplication.getDaoSession().getLoginDataDao().insert(data);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deleteLoginData(long id) {
        BaseApplication.getDaoSession().getLoginDataDao().deleteByKey(id);
    }

    /**
     * 更新数据
     *
     * @param data
     */
    public static void updateLoginData(LoginData data) {
        BaseApplication.getDaoSession().getLoginDataDao().update(data);
    }


    /**
     * 查询全部数据
     */
    public static List<LoginData> queryAll() {
        return BaseApplication.getDaoSession().getLoginDataDao().loadAll();
    }



    public static LoginData getLoginData() {
        List<LoginData> accountTables = BaseApplication.getDaoSession().getLoginDataDao().loadAll();
        if (accountTables.size() > 0) {
            LoginData adminData = accountTables.get(0);
            return adminData;
        }
        return null;

    }


    public static String getUserID() {
        List<LoginData> accountTables = BaseApplication.getDaoSession().getLoginDataDao().loadAll();
        if (accountTables.size() > 0) {
            LoginData userData = accountTables.get(0);
            return userData.getUserId();
        }
        return null;

    }

    /**
     * 删除全部数据
     */
    public static void deleAllData() {
        BaseApplication.getDaoSession().getLoginDataDao().deleteAll();
    }


}
