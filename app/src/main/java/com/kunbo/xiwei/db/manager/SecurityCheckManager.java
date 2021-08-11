package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.SecurityCheckRecorde;

import java.util.List;

/**
 * 安全巡检
 */
public class SecurityCheckManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public SecurityCheckManager(Context context) {
        manager = DaoManager.getInstance(context);
        daoSession = manager.getDaoSession();
        daoSession.clear();
    }

    /**
     * 插入数据 insertOrReplaceInTx 更新插入集合
     *
     * @param data
     * @return
     */
    public Boolean insertData(SecurityCheckRecorde data) {
        boolean flag;
        try {
            daoSession.getSecurityCheckRecordeDao().insert(data);
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
    public List<SecurityCheckRecorde> loadAllData() {
        return daoSession.getSecurityCheckRecordeDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getSecurityCheckRecordeDao().deleteAll();
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
            daoSession.getSecurityCheckRecordeDao().deleteByKey(id);
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
    public boolean deleteData(SecurityCheckRecorde data) {
        boolean flag;
        try {
            daoSession.getSecurityCheckRecordeDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

}
