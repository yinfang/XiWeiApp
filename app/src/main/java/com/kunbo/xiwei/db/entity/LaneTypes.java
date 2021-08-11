package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 车道信息
 */
@Entity
public class LaneTypes {
    @Id()
    private Long id;//唯一标识
    /*类型code*/
    private String code;
    /*组id*/
    private String groupId;
    /*名称*/
    private String name;
    @Generated(hash = 2079975966)
    public LaneTypes(Long id, String code, String groupId, String name) {
        this.id = id;
        this.code = code;
        this.groupId = groupId;
        this.name = name;
    }
    @Generated(hash = 1145606419)
    public LaneTypes() {
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


