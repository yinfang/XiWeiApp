package com.kunbo.xiwei.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 持证超限运输车辆入口查验登记表
 */
@Entity
public class OverHighCarIn {
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
    /*事由*/
    private String reason;
    /*查验班次*/
    private String checkTeam;
    /*查验班次*/
    private String checkTeamId;
    /*车牌号*/
    private String carNo;
    /*车主名称*/
    private String carOwnerName;
    /*车辆行驶证档案号*/
    private String fileNo;
    /*车辆营运证号*/
    private String runNo;
    /*当事人姓名*/
    private String name;
    /*与上述车辆关系*/
    private String relation;
    /*单位名称*/
    private String company;
    /*单位法定代表人*/
    private String companyLegalPeople;
    /*查验时间 */
    private String checkDate;
    /*通行证有效期 */
    private String validDate;
    /* 通行证号*/
    private String passNo;
    /*发证单位*/
    private String passCompany;
    /*检测重量（吨）*/
    private String weight;
    /*载后尺寸（米）*/
    private String dimens;
    /*货物名称*/
    private String goodsName;
    /*承运单位*/
    private String transportCompany;
    /*车、证是否相符 */
    private boolean isEqual;
    /*验证方式*/
    private String checkWay;
    /*是否监护*/
    private boolean iscare;
    /*监护人员*/
    private String carePerson;
    /*其他情况*/
    private String other;
    /*告知事项*/
    private String rules;
    /*被告知人日期*/
    private String informedDate;
    /*被告知人签名*/
    private String signatureInformed;
    /*查验人员签名*/
    private String signatureChecker;
    /*当班班长签名*/
    private String signatureMonitor;
    /*证件号*/
    private String certificateNo;
    /*图片 本地保存图片绝对路径*/
    @Convert(converter = StringConverent.class, columnType = String.class)
    private List<String> images;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    @Generated(hash = 1721830185)
    public OverHighCarIn(Long id, String userId, String personId, String staionId,
            String timeStamp, String reason, String checkTeam, String checkTeamId,
            String carNo, String carOwnerName, String fileNo, String runNo, String name,
            String relation, String company, String companyLegalPeople, String checkDate,
            String validDate, String passNo, String passCompany, String weight, String dimens,
            String goodsName, String transportCompany, boolean isEqual, String checkWay,
            boolean iscare, String carePerson, String other, String rules,
            String informedDate, String signatureInformed, String signatureChecker,
            String signatureMonitor, String certificateNo, List<String> images,
            boolean isSubmit, boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.timeStamp = timeStamp;
        this.reason = reason;
        this.checkTeam = checkTeam;
        this.checkTeamId = checkTeamId;
        this.carNo = carNo;
        this.carOwnerName = carOwnerName;
        this.fileNo = fileNo;
        this.runNo = runNo;
        this.name = name;
        this.relation = relation;
        this.company = company;
        this.companyLegalPeople = companyLegalPeople;
        this.checkDate = checkDate;
        this.validDate = validDate;
        this.passNo = passNo;
        this.passCompany = passCompany;
        this.weight = weight;
        this.dimens = dimens;
        this.goodsName = goodsName;
        this.transportCompany = transportCompany;
        this.isEqual = isEqual;
        this.checkWay = checkWay;
        this.iscare = iscare;
        this.carePerson = carePerson;
        this.other = other;
        this.rules = rules;
        this.informedDate = informedDate;
        this.signatureInformed = signatureInformed;
        this.signatureChecker = signatureChecker;
        this.signatureMonitor = signatureMonitor;
        this.certificateNo = certificateNo;
        this.images = images;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }


    @Generated(hash = 689160519)
    public OverHighCarIn() {
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


    public String getReason() {
        return this.reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getCheckTeam() {
        return this.checkTeam;
    }


    public void setCheckTeam(String checkTeam) {
        this.checkTeam = checkTeam;
    }


    public String getCheckTeamId() {
        return this.checkTeamId;
    }


    public void setCheckTeamId(String checkTeamId) {
        this.checkTeamId = checkTeamId;
    }


    public String getCarNo() {
        return this.carNo;
    }


    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }


    public String getCarOwnerName() {
        return this.carOwnerName;
    }


    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }


    public String getFileNo() {
        return this.fileNo;
    }


    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }


    public String getRunNo() {
        return this.runNo;
    }


    public void setRunNo(String runNo) {
        this.runNo = runNo;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getRelation() {
        return this.relation;
    }


    public void setRelation(String relation) {
        this.relation = relation;
    }


    public String getCompany() {
        return this.company;
    }


    public void setCompany(String company) {
        this.company = company;
    }


    public String getCompanyLegalPeople() {
        return this.companyLegalPeople;
    }


    public void setCompanyLegalPeople(String companyLegalPeople) {
        this.companyLegalPeople = companyLegalPeople;
    }


    public String getCheckDate() {
        return this.checkDate;
    }


    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }


    public String getValidDate() {
        return this.validDate;
    }


    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }


    public String getPassNo() {
        return this.passNo;
    }


    public void setPassNo(String passNo) {
        this.passNo = passNo;
    }


    public String getPassCompany() {
        return this.passCompany;
    }


    public void setPassCompany(String passCompany) {
        this.passCompany = passCompany;
    }


    public String getWeight() {
        return this.weight;
    }


    public void setWeight(String weight) {
        this.weight = weight;
    }


    public String getDimens() {
        return this.dimens;
    }


    public void setDimens(String dimens) {
        this.dimens = dimens;
    }


    public String getGoodsName() {
        return this.goodsName;
    }


    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getTransportCompany() {
        return this.transportCompany;
    }


    public void setTransportCompany(String transportCompany) {
        this.transportCompany = transportCompany;
    }


    public boolean getIsEqual() {
        return this.isEqual;
    }


    public void setIsEqual(boolean isEqual) {
        this.isEqual = isEqual;
    }


    public String getCheckWay() {
        return this.checkWay;
    }


    public void setCheckWay(String checkWay) {
        this.checkWay = checkWay;
    }


    public boolean getIscare() {
        return this.iscare;
    }


    public void setIscare(boolean iscare) {
        this.iscare = iscare;
    }


    public String getCarePerson() {
        return this.carePerson;
    }


    public void setCarePerson(String carePerson) {
        this.carePerson = carePerson;
    }


    public String getOther() {
        return this.other;
    }


    public void setOther(String other) {
        this.other = other;
    }


    public String getRules() {
        return this.rules;
    }


    public void setRules(String rules) {
        this.rules = rules;
    }


    public String getInformedDate() {
        return this.informedDate;
    }


    public void setInformedDate(String informedDate) {
        this.informedDate = informedDate;
    }


    public String getSignatureInformed() {
        return this.signatureInformed;
    }


    public void setSignatureInformed(String signatureInformed) {
        this.signatureInformed = signatureInformed;
    }


    public String getSignatureChecker() {
        return this.signatureChecker;
    }


    public void setSignatureChecker(String signatureChecker) {
        this.signatureChecker = signatureChecker;
    }


    public String getSignatureMonitor() {
        return this.signatureMonitor;
    }


    public void setSignatureMonitor(String signatureMonitor) {
        this.signatureMonitor = signatureMonitor;
    }


    public String getCertificateNo() {
        return this.certificateNo;
    }


    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }


    public List<String> getImages() {
        return this.images;
    }


    public void setImages(List<String> images) {
        this.images = images;
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

    
    public static class StringConverent implements PropertyConverter<List<String>, String> {
        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }
}
