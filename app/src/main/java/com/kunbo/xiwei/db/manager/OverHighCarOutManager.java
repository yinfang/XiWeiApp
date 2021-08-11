package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.OverHighCarOut;

import java.util.List;

/**
 * 治超出口查验表
 */
public class OverHighCarOutManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public OverHighCarOutManager(Context context) {
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
    public Boolean updateData(OverHighCarOut data) {
        boolean flag;
        try {
            daoSession.getOverHighCarOutDao().update(data);
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
    public Boolean insertData(OverHighCarOut data) {
        boolean flag;
        try {
            daoSession.getOverHighCarOutDao().insert(data);
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
    public List<OverHighCarOut> loadAllData() {
        return daoSession.getOverHighCarOutDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getOverHighCarOutDao().deleteAll();
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
            daoSession.getOverHighCarOutDao().deleteByKey(id);
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
    public boolean deleteData(OverHighCarOut data) {
        boolean flag;
        try {
            daoSession.getOverHighCarOutDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
