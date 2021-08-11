package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 巡检项目
 */
@Entity
public class SecurityItems {
    @Id()
    private Long id;//检查项目id 唯一标识
    /* 是否选中*/
    private boolean isSelecte;
    /* 检查周期*/
    private String examinePeriodDesc;
    /*检查时间*/
    private String examineTime;
    /*检查地点*/
    private String examineSite;
    /*检查位置*/
    private String examineLocation;
    /*检查项目*/
    private String examineProject;
    /*检查内容*/
    private String examineContext;
    /*状态描述*/
    private String state;
    /*检查人员*/
    private String checker;
    /*检查人员id*/
    private String checkerId;
    /*异常情况备注*/
    private String remark;
    @Generated(hash = 23577604)
    public SecurityItems(Long id, boolean isSelecte, String examinePeriodDesc,
            String examineTime, String examineSite, String examineLocation,
            String examineProject, String examineContext, String state,
            String checker, String checkerId, String remark) {
        this.id = id;
        this.isSelecte = isSelecte;
        this.examinePeriodDesc = examinePeriodDesc;
        this.examineTime = examineTime;
        this.examineSite = examineSite;
        this.examineLocation = examineLocation;
        this.examineProject = examineProject;
        this.examineContext = examineContext;
        this.state = state;
        this.checker = checker;
        this.checkerId = checkerId;
        this.remark = remark;
    }
    @Generated(hash = 1950896898)
    public SecurityItems() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsSelecte() {
        return this.isSelecte;
    }
    public void setIsSelecte(boolean isSelecte) {
        this.isSelecte = isSelecte;
    }
    public String getExaminePeriodDesc() {
        return this.examinePeriodDesc;
    }
    public void setExaminePeriodDesc(String examinePeriodDesc) {
        this.examinePeriodDesc = examinePeriodDesc;
    }
    public String getExamineTime() {
        return this.examineTime;
    }
    public void setExamineTime(String examineTime) {
        this.examineTime = examineTime;
    }
    public String getExamineSite() {
        return this.examineSite;
    }
    public void setExamineSite(String examineSite) {
        this.examineSite = examineSite;
    }
    public String getExamineLocation() {
        return this.examineLocation;
    }
    public void setExamineLocation(String examineLocation) {
        this.examineLocation = examineLocation;
    }
    public String getExamineProject() {
        return this.examineProject;
    }
    public void setExamineProject(String examineProject) {
        this.examineProject = examineProject;
    }
    public String getExamineContext() {
        return this.examineContext;
    }
    public void setExamineContext(String examineContext) {
        this.examineContext = examineContext;
    }
    public String getState() {
        return this.state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getChecker() {
        return this.checker;
    }
    public void setChecker(String checker) {
        this.checker = checker;
    }
    public String getCheckerId() {
        return this.checkerId;
    }
    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

}


