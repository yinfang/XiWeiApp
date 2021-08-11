package com.kunbo.xiwei.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 特请记录表
 */
@Entity
public class SpecialForm {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*teamId*/
    private String teamId;
    /*用户名*/
    private String name;
    /*时间戳*/
    private String timeStamp;
    /*特情类型
     * U 型、无卡坏卡、闯站、黑名单、超时、大车小标、计重更改、车型不符*/
    private String type;
    /*特情id*/
    private String typeId;
    /*日期*/
    private String date;
    /*时间*/
    private String time;
    /*班次*/
    private String classType;
    /*收费员*/
    private String feeman;
    /*收费员id*/
    private String feemanId;
    /*收费员工号*/
    private String jobNo;
    /*车道id*/
    private String lane;
    /*车道*/
    private String laneId;
    /*车牌号*/
    private String carNo;
    /*车型*/
    private String carType;
    /*车型id*/
    private String carTypeId;
    /*情况说明*/
    private String detail;
    /*拍照*/
    @Convert(converter = PhotoConverter.class, columnType = String.class)
    private List<String> photos;
    /*备注*/
    private String remark;
    /*能否处理*/
    private String isSolve;
    /*指定处理的站长*/
    private String siteAgent;
    /*指定处理的站长id*/
    private String siteAgentId;
    /*收费员签名*/
    private String signatureFeeman;
    /*班长签名*/
    private String signatureMonitor;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;
    @Generated(hash = 1241218494)
    public SpecialForm(Long id, String userId, String personId, String staionId,
            String teamId, String name, String timeStamp, String type, String typeId,
            String date, String time, String classType, String feeman, String feemanId,
            String jobNo, String lane, String laneId, String carNo, String carType,
            String carTypeId, String detail, List<String> photos, String remark,
            String isSolve, String siteAgent, String siteAgentId, String signatureFeeman,
            String signatureMonitor, boolean isSubmit, boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.teamId = teamId;
        this.name = name;
        this.timeStamp = timeStamp;
        this.type = type;
        this.typeId = typeId;
        this.date = date;
        this.time = time;
        this.classType = classType;
        this.feeman = feeman;
        this.feemanId = feemanId;
        this.jobNo = jobNo;
        this.lane = lane;
        this.laneId = laneId;
        this.carNo = carNo;
        this.carType = carType;
        this.carTypeId = carTypeId;
        this.detail = detail;
        this.photos = photos;
        this.remark = remark;
        this.isSolve = isSolve;
        this.siteAgent = siteAgent;
        this.siteAgentId = siteAgentId;
        this.signatureFeeman = signatureFeeman;
        this.signatureMonitor = signatureMonitor;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }


    @Generated(hash = 1464121071)
    public SpecialForm() {
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


    public String getType() {
        return this.type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getTypeId() {
        return this.typeId;
    }


    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }


    public String getDate() {
        return this.date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return this.time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getClassType() {
        return this.classType;
    }


    public void setClassType(String classType) {
        this.classType = classType;
    }


    public String getFeeman() {
        return this.feeman;
    }


    public void setFeeman(String feeman) {
        this.feeman = feeman;
    }


    public String getFeemanId() {
        return this.feemanId;
    }


    public void setFeemanId(String feemanId) {
        this.feemanId = feemanId;
    }


    public String getJobNo() {
        return this.jobNo;
    }


    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }


    public String getLane() {
        return this.lane;
    }


    public void setLane(String lane) {
        this.lane = lane;
    }


    public String getLaneId() {
        return this.laneId;
    }


    public void setLaneId(String laneId) {
        this.laneId = laneId;
    }


    public String getCarNo() {
        return this.carNo;
    }


    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }


    public String getCarType() {
        return this.carType;
    }


    public void setCarType(String carType) {
        this.carType = carType;
    }


    public String getCarTypeId() {
        return this.carTypeId;
    }


    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }


    public String getDetail() {
        return this.detail;
    }


    public void setDetail(String detail) {
        this.detail = detail;
    }


    public List<String> getPhotos() {
        return this.photos;
    }


    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }


    public String getRemark() {
        return this.remark;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getIsSolve() {
        return this.isSolve;
    }


    public void setIsSolve(String isSolve) {
        this.isSolve = isSolve;
    }


    public String getSiteAgent() {
        return this.siteAgent;
    }


    public void setSiteAgent(String siteAgent) {
        this.siteAgent = siteAgent;
    }


    public String getSiteAgentId() {
        return this.siteAgentId;
    }


    public void setSiteAgentId(String siteAgentId) {
        this.siteAgentId = siteAgentId;
    }


    public String getSignatureFeeman() {
        return this.signatureFeeman;
    }


    public void setSignatureFeeman(String signatureFeeman) {
        this.signatureFeeman = signatureFeeman;
    }


    public String getSignatureMonitor() {
        return this.signatureMonitor;
    }


    public void setSignatureMonitor(String signatureMonitor) {
        this.signatureMonitor = signatureMonitor;
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


    public String getTeamId() {
        return this.teamId;
    }


    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public static class PhotoConverter implements PropertyConverter<List<String>, String> {
        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            String dbString = new Gson().toJson(entityProperty);
            return dbString;
        }
    }
}
