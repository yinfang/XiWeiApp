package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.CivilizedTypes;
import com.kunbo.xiwei.db.entity.CivilizedTypesDao;
import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.LaneTypesDao;
import com.kunbo.xiwei.db.entity.SpecialTypes;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 便民服务类型
 */
public class CivilizedTypesManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public CivilizedTypesManager(Context context) {
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
    public Boolean insertData(CivilizedTypes entity) {
        boolean flag;
        try {
            daoSession.getCivilizedTypesDao().insert(entity);
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
    public List<CivilizedTypes> loadTypeData(String code) {
        List<CivilizedTypes> list = null;
        try {
            QueryBuilder<CivilizedTypes> queryBuilder = daoSession.getCivilizedTypesDao().queryBuilder();
            queryBuilder.where(CivilizedTypesDao.Properties.Code.like(code + "%"));//模糊查询已code开头的数据
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
    public List<CivilizedTypes> loadAllData() {
        return daoSession.getCivilizedTypesDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getCivilizedTypesDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
