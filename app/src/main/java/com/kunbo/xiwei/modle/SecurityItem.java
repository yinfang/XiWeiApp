package com.kunbo.xiwei.modle;

/**
 * 巡检项目
 */
public class SecurityItem {
    /*检查项目id 唯一标识*/
    private String id;
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
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    public SecurityItem(String id, boolean isSelecte, String examinePeriodDesc, String examineTime, String examineSite,
                        String examineLocation, String examineProject, String examineContext, String state, String checker,
                        String checkerId, String remark, boolean isSubmit, boolean isComplete) {
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
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelecte() {
        return isSelecte;
    }

    public void setSelecte(boolean selecte) {
        isSelecte = selecte;
    }

    public String getExaminePeriodDesc() {
        return examinePeriodDesc;
    }

    public void setExaminePeriodDesc(String examinePeriodDesc) {
        this.examinePeriodDesc = examinePeriodDesc;
    }

    public String getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(String examineTime) {
        this.examineTime = examineTime;
    }

    public String getExamineSite() {
        return examineSite;
    }

    public void setExamineSite(String examineSite) {
        this.examineSite = examineSite;
    }

    public String getExamineLocation() {
        return examineLocation;
    }

    public void setExamineLocation(String examineLocation) {
        this.examineLocation = examineLocation;
    }

    public String getExamineProject() {
        return examineProject;
    }

    public void setExamineProject(String examineProject) {
        this.examineProject = examineProject;
    }

    public String getExamineContext() {
        return examineContext;
    }

    public void setExamineContext(String examineContext) {
        this.examineContext = examineContext;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}


