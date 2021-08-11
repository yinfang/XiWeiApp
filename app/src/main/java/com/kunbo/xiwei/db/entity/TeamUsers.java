package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 站长登录角色 所在班组人员信息
 */
@Entity
public class TeamUsers {
    @Id()
    private Long id;//唯一标识
    /*站id*/
    private String deptId;
    /*岗位id*/
    private String postId;
    /*姓名*/
    private String name;
    /*所在班组id*/
    private String teamId;
    /*民族*/
    private String nation;
    /*身份证号*/
    private String idCard;
    /*籍贯*/
    private String nativePlace;
    /*职位类别  管理/非管理*/
    private String personType;
    /*电话*/
    private String contactInformation;
    @Generated(hash = 1049894085)
    public TeamUsers(Long id, String deptId, String postId, String name,
            String teamId, String nation, String idCard, String nativePlace,
            String personType, String contactInformation) {
        this.id = id;
        this.deptId = deptId;
        this.postId = postId;
        this.name = name;
        this.teamId = teamId;
        this.nation = nation;
        this.idCard = idCard;
        this.nativePlace = nativePlace;
        this.personType = personType;
        this.contactInformation = contactInformation;
    }
    @Generated(hash = 621135465)
    public TeamUsers() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeptId() {
        return this.deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getPostId() {
        return this.postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTeamId() {
        return this.teamId;
    }
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
    public String getNation() {
        return this.nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public String getIdCard() {
        return this.idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getNativePlace() {
        return this.nativePlace;
    }
    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
    public String getPersonType() {
        return this.personType;
    }
    public void setPersonType(String personType) {
        this.personType = personType;
    }
    public String getContactInformation() {
        return this.contactInformation;
    }
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}


