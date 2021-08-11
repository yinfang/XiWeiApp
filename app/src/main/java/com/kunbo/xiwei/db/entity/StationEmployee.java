package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 站人员信息 根据postCode查询各类型人员
 * 站长：SFZZZ  SFZZZ-F
 * 班长：SFBZ
 * 票证：PZY
 * 收费员：SFY
 * 内勤：NQ
 */
@Entity
public class StationEmployee {
    @Id()
    private Long id;//唯一标识
    /*站id*/
    private String deptId;
    /*站名称*/
    private String deptName;
    /*岗位id*/
    private String postId;
    /*岗位名称*/
    private String postDesc;
    /*岗位标识*/
    private String postCode;
    /*姓名*/
    private String name;
    /*所在班组id*/
    private String teamId;
    /*所在班组名称*/
    private String teamName;
    @Generated(hash = 2136214806)
    public StationEmployee(Long id, String deptId, String deptName, String postId,
            String postDesc, String postCode, String name, String teamId,
            String teamName) {
        this.id = id;
        this.deptId = deptId;
        this.deptName = deptName;
        this.postId = postId;
        this.postDesc = postDesc;
        this.postCode = postCode;
        this.name = name;
        this.teamId = teamId;
        this.teamName = teamName;
    }
    @Generated(hash = 550771859)
    public StationEmployee() {
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
    public String getDeptName() {
        return this.deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getPostId() {
        return this.postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getPostDesc() {
        return this.postDesc;
    }
    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
    public String getPostCode() {
        return this.postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
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
    public String getTeamName() {
        return this.teamName;
    }
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
}


