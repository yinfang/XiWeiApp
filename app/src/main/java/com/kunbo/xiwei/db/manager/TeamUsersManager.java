package com.kunbo.xiwei.db.manager;

import android.content.Context;

import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.TeamUsers;

import java.util.List;

/**
 * 班组人员信息
 */
public class TeamUsersManager {
    public DaoSession daoSession;
    public DaoManager manager;

    public TeamUsersManager(Context context) {
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
    public Boolean insertData(TeamUsers entity) {
        boolean flag;
        try {
            daoSession.getTeamUsersDao().insert(entity);
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
    public List<TeamUsers> loadAllData() {
        return daoSession.getTeamUsersDao().loadAll();
    }

    public boolean deleteAll() {
        boolean flag = false;
        try {
            daoSession.getTeamUsersDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

}
