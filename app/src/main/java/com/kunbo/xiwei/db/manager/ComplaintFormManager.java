package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.ComplaintForm;
import com.kunbo.xiwei.db.entity.DaoSession;

import java.util.List;

/**
 * 投诉管理记录
 */
public class ComplaintFormManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public ComplaintFormManager(Context context) {
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
    public Boolean updateData(ComplaintForm data) {
        boolean flag;
        try {
            daoSession.getComplaintFormDao().update(data);
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
    public Boolean insertData(ComplaintForm data) {
        boolean flag;
        try {
            daoSession.getComplaintFormDao().insert(data);
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
    public List<ComplaintForm> loadAllData() {
        return daoSession.getComplaintFormDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getComplaintFormDao().deleteAll();
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
            daoSession.getComplaintFormDao().deleteByKey(id);
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
    public boolean deleteData(ComplaintForm data) {
        boolean flag;
        try {
            daoSession.getComplaintFormDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
}
