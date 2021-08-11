package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 稽查管理登记表
 */
@Entity
public class InspectionForm {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*稽查单位*/
    private String inspection;
    /*稽查单位id*/
    private String inspectionId;
    /*被稽查单位*/
    private String inspected;
    /*被稽查单位*/
    private String inspectedId;
    /*稽查时间*/
    private String dateTime;
    /*稽查内容*/
    private String content;
    /*问题摘要*/
    private String mainQes;
    /*当事人签字*/
    private String partySignature;
    /*处理结果*/
    private String result;
    /*领队签字*/
    private String leaderSignature;
    /*稽查人员签字*/
    private String inspectorSignature;
    /*备注*/
    private String remark;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    @Generated(hash = 1044290147)
    public InspectionForm(Long id, String userId, String personId, String staionId,
            String inspection, String inspectionId, String inspected,
            String inspectedId, String dateTime, String content, String mainQes,
            String partySignature, String result, String leaderSignature,
            String inspectorSignature, String remark, boolean isSubmit,
            boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.inspection = inspection;
        this.inspectionId = inspectionId;
        this.inspected = inspected;
        this.inspectedId = inspectedId;
        this.dateTime = dateTime;
        this.content = content;
        this.mainQes = mainQes;
        this.partySignature = partySignature;
        this.result = result;
        this.leaderSignature = leaderSignature;
        this.inspectorSignature = inspectorSignature;
        this.remark = remark;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }
    @Generated(hash = 125171022)
    public InspectionForm() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getInspection() {
        return this.inspection;
    }
    public void setInspection(String inspection) {
        this.inspection = inspection;
    }
    public String getInspectionId() {
        return this.inspectionId;
    }
    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }
    public String getInspected() {
        return this.inspected;
    }
    public void setInspected(String inspected) {
        this.inspected = inspected;
    }
    public String getInspectedId() {
        return this.inspectedId;
    }
    public void setInspectedId(String inspectedId) {
        this.inspectedId = inspectedId;
    }
    public String getDateTime() {
        return this.dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMainQes() {
        return this.mainQes;
    }
    public void setMainQes(String mainQes) {
        this.mainQes = mainQes;
    }
    public String getPartySignature() {
        return this.partySignature;
    }
    public void setPartySignature(String partySignature) {
        this.partySignature = partySignature;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getLeaderSignature() {
        return this.leaderSignature;
    }
    public void setLeaderSignature(String leaderSignature) {
        this.leaderSignature = leaderSignature;
    }
    public String getInspectorSignature() {
        return this.inspectorSignature;
    }
    public void setInspectorSignature(String inspectorSignature) {
        this.inspectorSignature = inspectorSignature;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public boolean getIsSubmit() {
        return this.isSubmit;
    }
    public void setIsSubmit(boolean isSubmit) {
        this.isSubmit = isSubmit;
    }
    public boolean getIsComplete() {
        return this.isComplete;
    }
    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
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
    
}
