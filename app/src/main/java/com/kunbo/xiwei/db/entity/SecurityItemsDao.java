package com.kunbo.xiwei.db.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SECURITY_ITEMS".
*/
public class SecurityItemsDao extends AbstractDao<SecurityItems, Long> {

    public static final String TABLENAME = "SECURITY_ITEMS";

    /**
     * Properties of entity SecurityItems.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IsSelecte = new Property(1, boolean.class, "isSelecte", false, "IS_SELECTE");
        public final static Property ExaminePeriodDesc = new Property(2, String.class, "examinePeriodDesc", false, "EXAMINE_PERIOD_DESC");
        public final static Property ExamineTime = new Property(3, String.class, "examineTime", false, "EXAMINE_TIME");
        public final static Property ExamineSite = new Property(4, String.class, "examineSite", false, "EXAMINE_SITE");
        public final static Property ExamineLocation = new Property(5, String.class, "examineLocation", false, "EXAMINE_LOCATION");
        public final static Property ExamineProject = new Property(6, String.class, "examineProject", false, "EXAMINE_PROJECT");
        public final static Property ExamineContext = new Property(7, String.class, "examineContext", false, "EXAMINE_CONTEXT");
        public final static Property State = new Property(8, String.class, "state", false, "STATE");
        public final static Property Checker = new Property(9, String.class, "checker", false, "CHECKER");
        public final static Property CheckerId = new Property(10, String.class, "checkerId", false, "CHECKER_ID");
        public final static Property Remark = new Property(11, String.class, "remark", false, "REMARK");
    }


    public SecurityItemsDao(DaoConfig config) {
        super(config);
    }
    
    public SecurityItemsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SECURITY_ITEMS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"IS_SELECTE\" INTEGER NOT NULL ," + // 1: isSelecte
                "\"EXAMINE_PERIOD_DESC\" TEXT," + // 2: examinePeriodDesc
                "\"EXAMINE_TIME\" TEXT," + // 3: examineTime
                "\"EXAMINE_SITE\" TEXT," + // 4: examineSite
                "\"EXAMINE_LOCATION\" TEXT," + // 5: examineLocation
                "\"EXAMINE_PROJECT\" TEXT," + // 6: examineProject
                "\"EXAMINE_CONTEXT\" TEXT," + // 7: examineContext
                "\"STATE\" TEXT," + // 8: state
                "\"CHECKER\" TEXT," + // 9: checker
                "\"CHECKER_ID\" TEXT," + // 10: checkerId
                "\"REMARK\" TEXT);"); // 11: remark
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SECURITY_ITEMS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SecurityItems entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsSelecte() ? 1L: 0L);
 
        String examinePeriodDesc = entity.getExaminePeriodDesc();
        if (examinePeriodDesc != null) {
            stmt.bindString(3, examinePeriodDesc);
        }
 
        String examineTime = entity.getExamineTime();
        if (examineTime != null) {
            stmt.bindString(4, examineTime);
        }
 
        String examineSite = entity.getExamineSite();
        if (examineSite != null) {
            stmt.bindString(5, examineSite);
        }
 
        String examineLocation = entity.getExamineLocation();
        if (examineLocation != null) {
            stmt.bindString(6, examineLocation);
        }
 
        String examineProject = entity.getExamineProject();
        if (examineProject != null) {
            stmt.bindString(7, examineProject);
        }
 
        String examineContext = entity.getExamineContext();
        if (examineContext != null) {
            stmt.bindString(8, examineContext);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(9, state);
        }
 
        String checker = entity.getChecker();
        if (checker != null) {
            stmt.bindString(10, checker);
        }
 
        String checkerId = entity.getCheckerId();
        if (checkerId != null) {
            stmt.bindString(11, checkerId);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(12, remark);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SecurityItems entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getIsSelecte() ? 1L: 0L);
 
        String examinePeriodDesc = entity.getExaminePeriodDesc();
        if (examinePeriodDesc != null) {
            stmt.bindString(3, examinePeriodDesc);
        }
 
        String examineTime = entity.getExamineTime();
        if (examineTime != null) {
            stmt.bindString(4, examineTime);
        }
 
        String examineSite = entity.getExamineSite();
        if (examineSite != null) {
            stmt.bindString(5, examineSite);
        }
 
        String examineLocation = entity.getExamineLocation();
        if (examineLocation != null) {
            stmt.bindString(6, examineLocation);
        }
 
        String examineProject = entity.getExamineProject();
        if (examineProject != null) {
            stmt.bindString(7, examineProject);
        }
 
        String examineContext = entity.getExamineContext();
        if (examineContext != null) {
            stmt.bindString(8, examineContext);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(9, state);
        }
 
        String checker = entity.getChecker();
        if (checker != null) {
            stmt.bindString(10, checker);
        }
 
        String checkerId = entity.getCheckerId();
        if (checkerId != null) {
            stmt.bindString(11, checkerId);
        }
 
        String remark = entity.getRemark();
        if (remark != null) {
            stmt.bindString(12, remark);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SecurityItems readEntity(Cursor cursor, int offset) {
        SecurityItems entity = new SecurityItems( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getShort(offset + 1) != 0, // isSelecte
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // examinePeriodDesc
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // examineTime
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // examineSite
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // examineLocation
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // examineProject
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // examineContext
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // state
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // checker
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // checkerId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // remark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SecurityItems entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsSelecte(cursor.getShort(offset + 1) != 0);
        entity.setExaminePeriodDesc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setExamineTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setExamineSite(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setExamineLocation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setExamineProject(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setExamineContext(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setState(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setChecker(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCheckerId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRemark(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SecurityItems entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SecurityItems entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SecurityItems entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}