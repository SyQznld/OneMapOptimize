package com.oneMap.module.common.dao;

import com.oneMap.module.common.base.BaseApplication;
import com.oneMap.module.common.bean.BzData;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import onemap.greendao.gen.BzDataDao;

/**
 * 标注
 */

public class BzDao {

    //添加数据
    public static void insertBz(BzData data) {
        BaseApplication.getDaoSession().getBzDataDao().insert(data);
    }

    //根据id删除数据
    public static void deleteBzById(long id) {
        BaseApplication.getDaoSession().getBzDataDao().deleteByKey(id);
    }

    //更新数据
    public static void updateBz(BzData data) {
        BaseApplication.getDaoSession().getBzDataDao().update(data);
    }

    //查询全部数据
    public static List<BzData> queryAll() {
        List<BzData> bzDataList = BaseApplication.getDaoSession().getBzDataDao().loadAll();
        if (bzDataList.size() > 0) {
            return bzDataList;
        }
        return null;
    }

    //查询第一条数据
    public static BzData getBzData() {
        List<BzData> bzDataList = BaseApplication.getDaoSession().getBzDataDao().loadAll();
        if (bzDataList.size() > 0) {
            BzData bzData = bzDataList.get(0);
            return bzData;
        }
        return null;
    }

    //删除全部数据
    public static void deleteAllData() {
        BaseApplication.getDaoSession().getBzDataDao().deleteAll();
    }

    public static BzData queryByPolygon(String polygon) {

        Query<BzData> build = BaseApplication.getDaoSession().getBzDataDao().queryBuilder().where(
                BzDataDao.Properties.Polygon.eq(polygon)).build();
        if (build == null) {
            return null;
        } else {
            return build.list().get(0);
        }
    }

}
