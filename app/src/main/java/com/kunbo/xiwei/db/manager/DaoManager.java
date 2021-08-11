package com.kunbo.xiwei.db.manager;

import android.content.Context;
import android.os.Environment;

import com.kunbo.xiwei.db.entity.DaoMaster;
import com.kunbo.xiwei.db.entity.DaoSession;
import com.kunbo.xiwei.db.entity.MyDBHelper;
import com.zyf.domain.C;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;

/**
 * 初始化DB，获取各表单例时使用
 * <p>
 * 增
 * insert(User entity)：插入一条记录  有重复数据
 * save(User entity)： 插入一条记录，判断对象是否有Key值，有则更新，否则插入
 * insertInTx(List<User> entitys)：插入多条记录  有重复数据
 * insertOrReplace（User entity）：插入一条记录，传入的对象主键如果存在于数据库中，有则更新，否则插入
 * insertOrReplaceInTx（List<User> entitys）：插入多条记录，传入的对象主键如果存在于数据库中，有则更新，否则插入
 * 删
 * deleteBykey(Long key) ：根据主键删除一条记录。
 * delete(User entity) ：根据实体类删除一条记录，一般结合查询方法，查询出一条记录之后删除。
 * deleteInTx(List<User> entitys)：删除多条记录
 * deleteAll()： 删除所有记录
 * 改
 * update(User entity)：更新一条记录
 * updateInTx(List<User> entitys)：更新多条记录
 * 查
 * loadAll()：查询所有记录
 * load(Long key)：根据主键查询一条记录
 * queryBuilder().list()：查询多条记录 返回List列表
 * queryBuilder().where(UserDao.Properties.Name.eq("")).list()：返回List列表  .orderAsc对查询结果进行升降序排列   .orderDesc 降序排序
 * queryRaw(String where,String selectionArg)：根据一个字段查询另一个字段，返回List列表
 */
public class DaoManager {

    private static final String TAG = DaoManager.class.getSimpleName();
    private static volatile DaoManager manager;
    private static DaoMaster daoMaster;
    private MyDBHelper helper;
    private DaoSession daoSession;

    public static DaoManager getInstance(Context context) {
        if (manager == null) {
            synchronized (DaoManager.class) {
                if (manager == null) {
                    manager = new DaoManager(context);
                    manager.setDebug(true);
                }
            }
        }
        return manager;
    }

    private DaoManager(Context context) {
        DaoUtils.init(context);
        init(context);
    }

    public void init(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        // helper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        //TODO 指定数据库路径(注意：无SIM卡功能的PAD无此路径，软件会出现闪退无法打开)
        //String dbpath = Environment.getExternalStorageDirectory()+ File.separator + "/xiwei/" + C.DB_NAME;
        helper = new MyDBHelper(context, C.DB_NAME, null);//建库
        if (daoMaster == null) {
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
    }

    public MyDBHelper getDbHelper() {
        return helper;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 输出日志
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    public void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }
}
