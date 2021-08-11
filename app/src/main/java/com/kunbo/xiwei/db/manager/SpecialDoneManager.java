package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.SpecialForm;

import java.util.List;

/**
 * 特情记录
 */
public class SpecialDoneManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public SpecialDoneManager(Context context) {
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
    public Boolean updateData(SpecialForm data) {
        boolean flag;
        try {
            daoSession.getSpecialFormDao().update(data);
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
    public Boolean insertData(SpecialForm data) {
        boolean flag;
        try {
            daoSession.getSpecialFormDao().insert(data);
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
    public List<SpecialForm> loadAllData() {
        return daoSession.getSpecialFormDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getSpecialFormDao().deleteAll();
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
            daoSession.getSpecialFormDao().deleteByKey(id);
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
    public boolean deleteData(SpecialForm data) {
        boolean flag;
        try {
            daoSession.getSpecialFormDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
