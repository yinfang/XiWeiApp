package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.StationOndutyRecorde;

import java.util.List;

/**
 * 站长值班记录
 */
public class StationOndutyManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public StationOndutyManager(Context context) {
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
    public Boolean updateData(StationOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getStationOndutyRecordeDao().update(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }
    /**
     * 插入站长值班记录数据 insertOrReplaceInTx 更新插入集合
     *
     * @param data
     * @return
     */
    public Boolean insertData(StationOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getStationOndutyRecordeDao().insert(data);
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
    public List<StationOndutyRecorde> loadAllData() {
        return daoSession.getStationOndutyRecordeDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getStationOndutyRecordeDao().deleteAll();
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
            daoSession.getStationOndutyRecordeDao().deleteByKey(id);
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
    public boolean deleteData(StationOndutyRecorde data) {
        boolean flag;
        try {
            daoSession.getStationOndutyRecordeDao().delete(data);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }


}
