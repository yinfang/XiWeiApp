package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.SecurityItems;

import java.util.List;

/**
 * 安全巡检 项目
 */
public class SecurityItemsManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public SecurityItemsManager(Context context) {
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
    public Boolean insertData(SecurityItems entity) {
        boolean flag;
        try {
            daoSession.getSecurityItemsDao().insert(entity);
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
    public List<SecurityItems> loadAllData() {
        return daoSession.getSecurityItemsDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getSecurityItemsDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 删除数据
     *
     * @return
     */
    public boolean deleteData(SecurityItems data) {
        boolean flag;
        try {
            daoSession.getSecurityItemsDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
