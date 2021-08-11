package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.LaneTypesDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 车道信息 出入口根据code区分
 */
public class LaneTypesManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public LaneTypesManager(Context context) {
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
    public Boolean insertData(LaneTypes entity) {
        boolean flag;
        try {
            daoSession.getLaneTypesDao().insert(entity);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 根据code查询数据
     *
     * @return
     */
    public List<LaneTypes> loadTypeData(String code) {
        List<LaneTypes> list = null;
        try {
            QueryBuilder<LaneTypes> queryBuilder = daoSession.getLaneTypesDao().queryBuilder();
            queryBuilder.where(LaneTypesDao.Properties.Code.like(code + "%"));//模糊查询已code开头的数据
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
    public List<LaneTypes> loadAllData() {
        return daoSession.getLaneTypesDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getLaneTypesDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
