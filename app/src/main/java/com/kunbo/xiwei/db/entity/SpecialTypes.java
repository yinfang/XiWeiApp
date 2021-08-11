package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 特情类型
 */
@Entity
public class SpecialTypes {
    @Id()
    private Long id;//唯一标识
    /*类型code*/
    private String code;
    /*组id*/
    private String groupId;
    /*名称*/
    private String name;
    @Generated(hash = 2051886469)
    public SpecialTypes(Long id, String code, String groupId, String name) {
        this.id = id;
        this.code = code;
        this.groupId = groupId;
        this.name = name;
    }
    @Generated(hash = 91690486)
    public SpecialTypes() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }


}


