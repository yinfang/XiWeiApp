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
 * DAO for table "TEAM_USERS".
*/
public class TeamUsersDao extends AbstractDao<TeamUsers, Long> {

    public static final String TABLENAME = "TEAM_USERS";

    /**
     * Properties of entity TeamUsers.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property DeptId = new Property(1, String.class, "deptId", false, "DEPT_ID");
        public final static Property PostId = new Property(2, String.class, "postId", false, "POST_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property TeamId = new Property(4, String.class, "teamId", false, "TEAM_ID");
        public final static Property Nation = new Property(5, String.class, "nation", false, "NATION");
        public final static Property IdCard = new Property(6, String.class, "idCard", false, "ID_CARD");
        public final static Property NativePlace = new Property(7, String.class, "nativePlace", false, "NATIVE_PLACE");
        public final static Property PersonType = new Property(8, String.class, "personType", false, "PERSON_TYPE");
        public final static Property ContactInformation = new Property(9, String.class, "contactInformation", false, "CONTACT_INFORMATION");
    }


    public TeamUsersDao(DaoConfig config) {
        super(config);
    }
    
    public TeamUsersDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TEAM_USERS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"DEPT_ID\" TEXT," + // 1: deptId
                "\"POST_ID\" TEXT," + // 2: postId
                "\"NAME\" TEXT," + // 3: name
                "\"TEAM_ID\" TEXT," + // 4: teamId
                "\"NATION\" TEXT," + // 5: nation
                "\"ID_CARD\" TEXT," + // 6: idCard
                "\"NATIVE_PLACE\" TEXT," + // 7: nativePlace
                "\"PERSON_TYPE\" TEXT," + // 8: personType
                "\"CONTACT_INFORMATION\" TEXT);"); // 9: contactInformation
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TEAM_USERS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TeamUsers entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deptId = entity.getDeptId();
        if (deptId != null) {
            stmt.bindString(2, deptId);
        }
 
        String postId = entity.getPostId();
        if (postId != null) {
            stmt.bindString(3, postId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String teamId = entity.getTeamId();
        if (teamId != null) {
            stmt.bindString(5, teamId);
        }
 
        String nation = entity.getNation();
        if (nation != null) {
            stmt.bindString(6, nation);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(7, idCard);
        }
 
        String nativePlace = entity.getNativePlace();
        if (nativePlace != null) {
            stmt.bindString(8, nativePlace);
        }
 
        String personType = entity.getPersonType();
        if (personType != null) {
            stmt.bindString(9, personType);
        }
 
        String contactInformation = entity.getContactInformation();
        if (contactInformation != null) {
            stmt.bindString(10, contactInformation);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TeamUsers entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String deptId = entity.getDeptId();
        if (deptId != null) {
            stmt.bindString(2, deptId);
        }
 
        String postId = entity.getPostId();
        if (postId != null) {
            stmt.bindString(3, postId);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String teamId = entity.getTeamId();
        if (teamId != null) {
            stmt.bindString(5, teamId);
        }
 
        String nation = entity.getNation();
        if (nation != null) {
            stmt.bindString(6, nation);
        }
 
        String idCard = entity.getIdCard();
        if (idCard != null) {
            stmt.bindString(7, idCard);
        }
 
        String nativePlace = entity.getNativePlace();
        if (nativePlace != null) {
            stmt.bindString(8, nativePlace);
        }
 
        String personType = entity.getPersonType();
        if (personType != null) {
            stmt.bindString(9, personType);
        }
 
        String contactInformation = entity.getContactInformation();
        if (contactInformation != null) {
            stmt.bindString(10, contactInformation);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TeamUsers readEntity(Cursor cursor, int offset) {
        TeamUsers entity = new TeamUsers( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // deptId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // postId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // teamId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nation
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // idCard
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // nativePlace
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // personType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // contactInformation
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TeamUsers entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDeptId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPostId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTeamId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNation(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIdCard(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setNativePlace(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPersonType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setContactInformation(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TeamUsers entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TeamUsers entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TeamUsers entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
