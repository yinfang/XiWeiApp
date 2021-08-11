package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 投诉处理登记表
 */
@Entity
public class ComplaintForm {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*用户名*/
    private String name;
    /*时间戳*/
    private String timeStamp;
    /*时间*/
    private String date;
    /*投诉来源*/
    private String complainantFrom;
    /*投诉来源id*/
    private String complainantFromId;
    /*投诉类型*/
    private String complainantType;
    /*投诉类型id*/
    private String complainantTypeId;
    /*车牌号*/
    private String carNo;
    /*票据号码*/
    private String billNo;
    /*联系方式*/
    private String mobileNo;
    /*投诉内容*/
    private String complaintDesc;
    /*调查情况*/
    private String investigation;
    /*处理意见*/
    private String doneAdvice;
    /*投诉人*/
    private String complainantSignature;
    /*受理人*/
    private String receiverSignature;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;
    @Generated(hash = 1852147587)
    public ComplaintForm(Long id, String userId, String personId, String staionId,
            String name, String timeStamp, String date, String complainantFrom,
            String complainantFromId, String complainantType,
            String complainantTypeId, String carNo, String billNo, String mobileNo,
            String complaintDesc, String investigation, String doneAdvice,
            String complainantSignature, String receiverSignature, boolean isSubmit,
            boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.name = name;
        this.timeStamp = timeStamp;
        this.date = date;
        this.complainantFrom = complainantFrom;
        this.complainantFromId = complainantFromId;
        this.complainantType = complainantType;
        this.complainantTypeId = complainantTypeId;
        this.carNo = carNo;
        this.billNo = billNo;
        this.mobileNo = mobileNo;
        this.complaintDesc = complaintDesc;
        this.investigation = investigation;
        this.doneAdvice = doneAdvice;
        this.complainantSignature = complainantSignature;
        this.receiverSignature = receiverSignature;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }
    @Generated(hash = 2023561198)
    public ComplaintForm() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTimeStamp() {
        return this.timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getComplainantFrom() {
        return this.complainantFrom;
    }
    public void setComplainantFrom(String complainantFrom) {
        this.complainantFrom = complainantFrom;
    }
    public String getComplainantFromId() {
        return this.complainantFromId;
    }
    public void setComplainantFromId(String complainantFromId) {
        this.complainantFromId = complainantFromId;
    }
    public String getComplainantType() {
        return this.complainantType;
    }
    public void setComplainantType(String complainantType) {
        this.complainantType = complainantType;
    }
    public String getComplainantTypeId() {
        return this.complainantTypeId;
    }
    public void setComplainantTypeId(String complainantTypeId) {
        this.complainantTypeId = complainantTypeId;
    }
    public String getCarNo() {
        return this.carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getBillNo() {
        return this.billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getMobileNo() {
        return this.mobileNo;
    }
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getComplaintDesc() {
        return this.complaintDesc;
    }
    public void setComplaintDesc(String complaintDesc) {
        this.complaintDesc = complaintDesc;
    }
    public String getInvestigation() {
        return this.investigation;
    }
    public void setInvestigation(String investigation) {
        this.investigation = investigation;
    }
    public String getDoneAdvice() {
        return this.doneAdvice;
    }
    public void setDoneAdvice(String doneAdvice) {
        this.doneAdvice = doneAdvice;
    }
    public String getComplainantSignature() {
        return this.complainantSignature;
    }
    public void setComplainantSignature(String complainantSignature) {
        this.complainantSignature = complainantSignature;
    }
    public String getReceiverSignature() {
        return this.receiverSignature;
    }
    public void setReceiverSignature(String receiverSignature) {
        this.receiverSignature = receiverSignature;
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
}
