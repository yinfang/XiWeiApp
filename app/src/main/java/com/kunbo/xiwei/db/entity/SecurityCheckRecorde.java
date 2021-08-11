package com.kunbo.xiwei.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunbo.xiwei.modle.SecurityItem;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全巡检记录
 */
@Entity
public class SecurityCheckRecorde {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /* 是否选中*/
    /*检查项目列表*/
    @Convert(/**指定转换器 **/converter = CheckListConverter.class,/**指定数据库中的列字段**/columnType = String.class)
    private List<SecurityItem> checkList;


    @Generated(hash = 927097955)
    public SecurityCheckRecorde(Long id, String userId, String personId, String staionId,
            List<SecurityItem> checkList) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.checkList = checkList;
    }

    @Generated(hash = 2052372325)
    public SecurityCheckRecorde() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SecurityItem> getCheckList() {
        return this.checkList;
    }

    public void setCheckList(List<SecurityItem> checkList) {
        this.checkList = checkList;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getStaionId() {
        return this.staionId;
    }

    public void setStaionId(String staionId) {
        this.staionId = staionId;
    }

    public static class CheckListConverter implements PropertyConverter<List<SecurityItem>, String> {
        @Override
        public List<SecurityItem> convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<ArrayList<SecurityItem>>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(List<SecurityItem> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }

}
