package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.CivilizedServiceForm;
import com.kunbo.xiwei.db.entity.DaoSession;

import java.util.List;

/**
 * 文明服务记录
 */
public class CivilizedServiceManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public CivilizedServiceManager(Context context) {
        manager = DaoManager.getInstance(context);
        daoSession = manager.getDaoSession();
        daoSession.clear();
    }

    /**
     * 更新数据 insertOrReplaceInTx 更新插入集合
     *
     * @param data
     * @return
     */
    public Boolean updateData(CivilizedServiceForm data) {
        boolean flag;
        try {
            daoSession.getCivilizedServiceFormDao().update(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 插入数据 insertOrReplaceInTx 更新插入集合
     *
     * @param data
     * @return
     */
    public Boolean insertData(CivilizedServiceForm data) {
        boolean flag;
        try {
            daoSession.getCivilizedServiceFormDao().insert(data);
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
    public List<CivilizedServiceForm> loadAllData() {
        return daoSession.getCivilizedServiceFormDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getCivilizedServiceFormDao().deleteAll();
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
    public boolean deleteById(Long id) {
        boolean flag;
        try {
            daoSession.getCivilizedServiceFormDao().deleteByKey(id);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    /**
     * 删除数据
     *
     * @return
     */
    public boolean deleteData(CivilizedServiceForm data) {
        boolean flag;
        try {
            daoSession.getCivilizedServiceFormDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
