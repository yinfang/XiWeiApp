package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.MonitorOndutyRecorde;

import java.util.List;

/**
 * 班长值班记录
 */
public class MonitorOndutyManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public MonitorOndutyManager(Context context) {
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
    public Boolean updateData(MonitorOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getMonitorOndutyRecordeDao().update(data);
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
    public Boolean insertData(MonitorOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getMonitorOndutyRecordeDao().insert(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 查询所有城市数据
     *
     * @return
     */
    public List<MonitorOndutyRecorde> loadAllData() {
        return daoSession.getMonitorOndutyRecordeDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getMonitorOndutyRecordeDao().deleteAll();
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
            daoSession.getMonitorOndutyRecordeDao().deleteByKey(id);
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
    public boolean deleteData(MonitorOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getMonitorOndutyRecordeDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

}
