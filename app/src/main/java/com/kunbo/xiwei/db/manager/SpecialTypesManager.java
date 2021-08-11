package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.SpecialTypes;

import java.util.List;

/**
 * 特情类型
 */
public class SpecialTypesManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public SpecialTypesManager(Context context) {
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
    public Boolean insertData(SpecialTypes entity) {
        boolean flag;
        try {
            daoSession.getSpecialTypesDao().insert(entity);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<SpecialTypes> loadAllData() {
        return daoSession.getSpecialTypesDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getSpecialTypesDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
