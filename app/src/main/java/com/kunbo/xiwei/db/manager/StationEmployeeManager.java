package com.kunbo.xiwei.db.manager;

import android.content.Context;
import android.text.TextUtils;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.entity.StationEmployeeDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 站人员信息
 */
public class StationEmployeeManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public StationEmployeeManager(Context context) {
        manager = DaoManager.getInstance(context);
        daoSession = manager.getDaoSession();
        daoSession.clear();
    }

    /**
     * 插入数据 insert更新插入集合
     *
     * @param entity
     * @return
     */
    public Boolean insertData(StationEmployee entity) {
        boolean flag;
        try {
            daoSession.getStationEmployeeDao().insert(entity);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 根据postcode查询数据
     * 站长：SFZZZ  SFZZZ-F
     * 班长：SFBZ
     * 票证：PZY
     * 收费员：SFY
     * 内勤：NQ
     *
     * @return
     */
    public List<StationEmployee> loadTypeData(String postCode, String postCode1) {
        List<StationEmployee> list = null;
        try {
            QueryBuilder<StationEmployee> queryBuilder = daoSession.getStationEmployeeDao().queryBuilder();
            if (TextUtils.isEmpty(postCode1))
                queryBuilder.where(StationEmployeeDao.Properties.PostCode.like(postCode + "%"));//模糊查询已postCode开头的数据
            else
                queryBuilder.whereOr(StationEmployeeDao.Properties.PostCode.like(postCode + "%"), StationEmployeeDao.Properties.PostCode.like(postCode1 + "%"));//模糊查询已postCode开头的数据
            list = queryBuilder.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据name查询数据
     *
     * @return
     */
    public List<StationEmployee> searchByName(String name) {
        List<StationEmployee> list = null;
        try {
            QueryBuilder<StationEmployee> queryBuilder = daoSession.getStationEmployeeDao().queryBuilder();
            queryBuilder.where(StationEmployeeDao.Properties.Name.like("%" + name + "%"));//模糊查询包含name的数据
            list = queryBuilder.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<StationEmployee> loadAllData() {
        return daoSession.getStationEmployeeDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getStationEmployeeDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
