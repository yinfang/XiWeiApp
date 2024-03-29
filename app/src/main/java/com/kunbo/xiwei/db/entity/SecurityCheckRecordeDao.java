package com.kunbo.xiwei.db.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.kunbo.xiwei.db.entity.SecurityCheckRecorde.CheckListConverter;
import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SECURITY_CHECK_RECORDE".
*/
public class SecurityCheckRecordeDao extends AbstractDao<SecurityCheckRecorde, Long> {

    public static final String TABLENAME = "SECURITY_CHECK_RECORDE";

    /**
     * Properties of entity SecurityCheckRecorde.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property PersonId = new Property(2, String.class, "personId", false, "PERSON_ID");
        public final static Property StaionId = new Property(3, String.class, "staionId", false, "STAION_ID");
        public final static Property CheckList = new Property(4, String.class, "checkList", false, "CHECK_LIST");
    }

    private final CheckListConverter checkListConverter = new CheckListConverter();

    public SecurityCheckRecordeDao(DaoConfig config) {
        super(config);
    }
    
    public SecurityCheckRecordeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SECURITY_CHECK_RECORDE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"PERSON_ID\" TEXT," + // 2: personId
                "\"STAION_ID\" TEXT," + // 3: staionId
                "\"CHECK_LIST\" TEXT);"); // 4: checkList
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SECURITY_CHECK_RECORDE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SecurityCheckRecorde entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String personId = entity.getPersonId();
        if (personId != null) {
            stmt.bindString(3, personId);
        }
 
        String staionId = entity.getStaionId();
        if (staionId != null) {
            stmt.bindString(4, staionId);
        }
 
        List checkList = entity.getCheckList();
        if (checkList != null) {
            stmt.bindString(5, checkListConverter.convertToDatabaseValue(checkList));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SecurityCheckRecorde entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String personId = entity.getPersonId();
        if (personId != null) {
            stmt.bindString(3, personId);
        }
 
        String staionId = entity.getStaionId();
        if (staionId != null) {
            stmt.bindString(4, staionId);
        }
 
        List checkList = entity.getCheckList();
        if (checkList != null) {
            stmt.bindString(5, checkListConverter.convertToDatabaseValue(checkList));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SecurityCheckRecorde readEntity(Cursor cursor, int offset) {
        SecurityCheckRecorde entity = new SecurityCheckRecorde( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // personId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // staionId
            cursor.isNull(offset + 4) ? null : checkListConverter.convertToEntityProperty(cursor.getString(offset + 4)) // checkList
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SecurityCheckRecorde entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPersonId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStaionId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCheckList(cursor.isNull(offset + 4) ? null : checkListConverter.convertToEntityProperty(cursor.getString(offset + 4)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SecurityCheckRecorde entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SecurityCheckRecorde entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SecurityCheckRecorde entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
