package com.kunbo.xiwei.db.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

/**
 * 站持证超限
 * 运输车辆出口查验登记表
 */
@Entity
public class OverHighCarOut {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*查验班次id*/
    private String checkTeamId;
    /*时间戳*/
    private String timeStamp;
    /*事由*/
    private String reason;
    /*查验班次*/
    private String checkTeam;
    /*车牌号*/
    private String carNo;
    /*通行证有效期 */
    private String passNo;
    /*车辆行驶证档案号*/
    private String fileNo;
    /*车辆营运证号*/
    private String runNo;
    /*车主姓名*/
    private String ownerName;
    /*单号*/
    private String billNo;
    /*几何尺寸*/
    private String dimens;
    /*车货总重*/
    private String allWeight;
    /*入口重量*/
    private String inWeight;
    /*入口站名称*/
    private String inStation;
    /*入口站id*/
    private String inStationId;
    /*通行证有效期 */
    private String validDate;
    /* 出站时间*/
    private String outDate;
    /*发证单位*/
    private String passCompany;
    /*出口检测重量*/
    private String outWeight;
    /*货物名称*/
    private String goodsName;
    /*轴荷分布*/
    private String axleLoad;
    /*出站尺寸（米） */
    private String outDimens;
    /*有无许可证*/
    private boolean hasCertificate;
    /*车、证是否相符*/
    private boolean isEqual;
    /*是否监护*/
    private boolean isCare;
    /*监护人员*/
    private String carePerson;
    /*缴费金额*/
    private String totalFee;
    /*是否减征*/
    private boolean isReduce;
    /*减征前金额*/
    private String beforeFee;
    /*减征后金额*/
    private String afterFee;
    /*处理结果*/
    private String result;
    /*查验人员签名*/
    private String signatureChecker;
    /*收费员(工号)签名*/
    private String signatureFeeMan;
    /*当班班长签名*/
    private String signatureMonitor;
    /*图片 本地保存图片绝对路径*/
    @Convert(converter = OverHighCarIn.StringConverent.class, columnType = String.class)
    private List<String> images;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    @Generated(hash = 441493971)
    public OverHighCarOut(Long id, String userId, String personId, String staionId,
            String checkTeamId, String timeStamp, String reason, String checkTeam,
            String carNo, String passNo, String fileNo, String runNo, String ownerName,
            String billNo, String dimens, String allWeight, String inWeight, String inStation,
            String inStationId, String validDate, String outDate, String passCompany,
            String outWeight, String goodsName, String axleLoad, String outDimens,
            boolean hasCertificate, boolean isEqual, boolean isCare, String carePerson,
            String totalFee, boolean isReduce, String beforeFee, String afterFee,
            String result, String signatureChecker, String signatureFeeMan,
            String signatureMonitor, List<String> images, boolean isSubmit,
            boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.checkTeamId = checkTeamId;
        this.timeStamp = timeStamp;
        this.reason = reason;
        this.checkTeam = checkTeam;
        this.carNo = carNo;
        this.passNo = passNo;
        this.fileNo = fileNo;
        this.runNo = runNo;
        this.ownerName = ownerName;
        this.billNo = billNo;
        this.dimens = dimens;
        this.allWeight = allWeight;
        this.inWeight = inWeight;
        this.inStation = inStation;
        this.inStationId = inStationId;
        this.validDate = validDate;
        this.outDate = outDate;
        this.passCompany = passCompany;
        this.outWeight = outWeight;
        this.goodsName = goodsName;
        this.axleLoad = axleLoad;
        this.outDimens = outDimens;
        this.hasCertificate = hasCertificate;
        this.isEqual = isEqual;
        this.isCare = isCare;
        this.carePerson = carePerson;
        this.totalFee = totalFee;
        this.isReduce = isReduce;
        this.beforeFee = beforeFee;
        this.afterFee = afterFee;
        this.result = result;
        this.signatureChecker = signatureChecker;
        this.signatureFeeMan = signatureFeeMan;
        this.signatureMonitor = signatureMonitor;
        this.images = images;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }
    @Generated(hash = 2092419977)
    public OverHighCarOut() {
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
    public String getCarNo() {
        return this.carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getPassNo() {
        return this.passNo;
    }
    public void setPassNo(String passNo) {
        this.passNo = passNo;
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
    public String getOwnerName() {
        return this.ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getBillNo() {
        return this.billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getDimens() {
        return this.dimens;
    }
    public void setDimens(String dimens) {
        this.dimens = dimens;
    }
    public String getAllWeight() {
        return this.allWeight;
    }
    public void setAllWeight(String allWeight) {
        this.allWeight = allWeight;
    }
    public String getInWeight() {
        return this.inWeight;
    }
    public void setInWeight(String inWeight) {
        this.inWeight = inWeight;
    }
    public String getInStation() {
        return this.inStation;
    }
    public void setInStation(String inStation) {
        this.inStation = inStation;
    }
    public String getInStationId() {
        return this.inStationId;
    }
    public void setInStationId(String inStationId) {
        this.inStationId = inStationId;
    }
    public String getValidDate() {
        return this.validDate;
    }
    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
    public String getOutDate() {
        return this.outDate;
    }
    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }
    public String getPassCompany() {
        return this.passCompany;
    }
    public void setPassCompany(String passCompany) {
        this.passCompany = passCompany;
    }
    public String getOutWeight() {
        return this.outWeight;
    }
    public void setOutWeight(String outWeight) {
        this.outWeight = outWeight;
    }
    public String getGoodsName() {
        return this.goodsName;
    }
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getAxleLoad() {
        return this.axleLoad;
    }
    public void setAxleLoad(String axleLoad) {
        this.axleLoad = axleLoad;
    }
    public String getOutDimens() {
        return this.outDimens;
    }
    public void setOutDimens(String outDimens) {
        this.outDimens = outDimens;
    }
    public boolean getHasCertificate() {
        return this.hasCertificate;
    }
    public void setHasCertificate(boolean hasCertificate) {
        this.hasCertificate = hasCertificate;
    }
    public boolean getIsEqual() {
        return this.isEqual;
    }
    public void setIsEqual(boolean isEqual) {
        this.isEqual = isEqual;
    }
    public boolean getIsCare() {
        return this.isCare;
    }
    public void setIsCare(boolean isCare) {
        this.isCare = isCare;
    }
    public String getCarePerson() {
        return this.carePerson;
    }
    public void setCarePerson(String carePerson) {
        this.carePerson = carePerson;
    }
    public String getTotalFee() {
        return this.totalFee;
    }
    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
    public boolean getIsReduce() {
        return this.isReduce;
    }
    public void setIsReduce(boolean isReduce) {
        this.isReduce = isReduce;
    }
    public String getBeforeFee() {
        return this.beforeFee;
    }
    public void setBeforeFee(String beforeFee) {
        this.beforeFee = beforeFee;
    }
    public String getAfterFee() {
        return this.afterFee;
    }
    public void setAfterFee(String afterFee) {
        this.afterFee = afterFee;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getSignatureChecker() {
        return this.signatureChecker;
    }
    public void setSignatureChecker(String signatureChecker) {
        this.signatureChecker = signatureChecker;
    }
    public String getSignatureFeeMan() {
        return this.signatureFeeMan;
    }
    public void setSignatureFeeMan(String signatureFeeMan) {
        this.signatureFeeMan = signatureFeeMan;
    }
    public String getSignatureMonitor() {
        return this.signatureMonitor;
    }
    public void setSignatureMonitor(String signatureMonitor) {
        this.signatureMonitor = signatureMonitor;
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
    public String getCheckTeamId() {
        return this.checkTeamId;
    }
    public void setCheckTeamId(String checkTeamId) {
        this.checkTeamId = checkTeamId;
    }

}
