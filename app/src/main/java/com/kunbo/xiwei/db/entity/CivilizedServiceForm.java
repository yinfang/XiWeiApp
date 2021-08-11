package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 文明服务登记表
 */
@Entity
public class CivilizedServiceForm {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*时间戳*/
    private String timeStamp;
    /*日期*/
    private String date;
    /*数量*/
    private String num;
    /*时间*/
    private String time;
    /*车牌号*/
    private String carNo;
    /*服务类型*/
    private String serviceType;
    /*服务类型*/
    private String serviceTypeId;
    /*使用物品*/
    private String useGoods;
    /*备注*/
    private String remark;
    /*外勤签字*/
    private String outSingnature;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    @Generated(hash = 767516649)
    public CivilizedServiceForm(Long id, String userId, String personId,
            String staionId, String timeStamp, String date, String num, String time,
            String carNo, String serviceType, String serviceTypeId, String useGoods,
            String remark, String outSingnature, boolean isSubmit,
            boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.timeStamp = timeStamp;
        this.date = date;
        this.num = num;
        this.time = time;
        this.carNo = carNo;
        this.serviceType = serviceType;
        this.serviceTypeId = serviceTypeId;
        this.useGoods = useGoods;
        this.remark = remark;
        this.outSingnature = outSingnature;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }
    @Generated(hash = 1912532344)
    public CivilizedServiceForm() {
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
    public String getNum() {
        return this.num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getCarNo() {
        return this.carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getServiceType() {
        return this.serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public String getServiceTypeId() {
        return this.serviceTypeId;
    }
    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    public String getUseGoods() {
        return this.useGoods;
    }
    public void setUseGoods(String useGoods) {
        this.useGoods = useGoods;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getOutSingnature() {
        return this.outSingnature;
    }
    public void setOutSingnature(String outSingnature) {
        this.outSingnature = outSingnature;
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
