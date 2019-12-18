package com.oneMap.module.common.dao;

import android.app.Notification;

import com.oneMap.module.common.base.BaseApplication;
import com.oneMap.module.common.bean.GatherData;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import onemap.greendao.gen.GatherDataDao;

public class GatherDao {
    //添加数据
    public static void insertGather(GatherData data) {
        BaseApplication.getDaoSession().getGatherDataDao().insert(data);
    }

    //根据id删除数据
    public static void deleteGatherById(long id) {
        BaseApplication.getDaoSession().getGatherDataDao().deleteByKey(id);
    }

    //更新数据
    public static void updateGather(GatherData data) {
        BaseApplication.getDaoSession().getGatherDataDao().update(data);
    }

    //查询全部数据
    public static List<GatherData> queryAll() {
        List<GatherData> gatherDataList = BaseApplication.getDaoSession().getGatherDataDao().loadAll();
        if (gatherDataList.size() > 0) {
            return gatherDataList;
        }
        return null;
    }


    //删除全部数据
    public static void deleteAllData() {
        BaseApplication.getDaoSession().getGatherDataDao().deleteAll();
    }

    public static GatherData queryGatherPolygon(String polygon) {
        Query<GatherData> gather = BaseApplication.getDaoSession().getGatherDataDao().queryBuilder().where(
                GatherDataDao.Properties.GatherPolygon.eq(polygon)).build();
        if (gather == null) {
            return null;
        } else {
            return gather.list().get(0);
        }
    }



    /**
     *  按时间排序
     * “orderAsc” 以字段升序排序
     * “orderDesc”以字段降序
     */
    public static List<GatherData> descGatherList() {
        Query<GatherData> gather = BaseApplication.getDaoSession().getGatherDataDao().queryBuilder().orderDesc(GatherDataDao.Properties.GatherTime).build();
        if (gather == null) {
            return null;
        } else {
            return gather.list();
        }
    }




}
