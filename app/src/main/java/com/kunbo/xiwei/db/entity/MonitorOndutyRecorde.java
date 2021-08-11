package com.kunbo.xiwei.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunbo.xiwei.modle.CarInOutEntity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 班长值班记录
 */
@Entity
public class MonitorOndutyRecorde {
    @Id(autoincrement = true)
    private Long id;
    /*角色id*/
    private String userId;
    /*用户id*/
    private String personId;
    /*站id*/
    private String staionId;
    /*值班时间段*/
    private String timeStamp;
    /*班长*/
    private String monitorName;
    /*日期*/
    private String date;
    /*星期*/
    private String weekday;
    /*班次*/
    private String team;
    /*班次id*/
    private String teamId;
    /*班级*/
    private String teamClass;
    /*外勤*/
    private String outPeople;
    /*外勤id*/
    private String outPeopleId;
    /*天气*/
    private String weather;
    /*收费额*/
    private String totalFee;
    /*回收卡*/
    private String backCardNum;
    /*发卡量*/
    private String outCardNum;

    /*入口车道*/
    @Convert(/**指定转换器 **/converter = CarInOutConverent.class,/**指定数据库中的列字段**/columnType = String.class)
    private List<CarInOutEntity> carInList;

    /*出口车道*/
    @Convert(converter = CarInOutConverent.class, columnType = String.class)
    private List<CarInOutEntity> carOutList;

    /*班前要求*/
    private String beforeOnduty;
    /*当班工作*/
    private String duringOnduty;
    /*班后小结*/
    private String afterOnduty;
    /*值班人备注*/
    private String ondutyRemark;

    /*接班人*/
    private String receivePeople;
    /*接班人id*/
    private String receivePeopleId;
    /*车道备用金交接情况*/
    private String moneyExchange;
    /*车道物品交接情况*/
    private String goodsExchange;
    /*车道设备运行情况*/
    private String machineRun;
    /*亭内卫生情况*/
    private String inOutDirty;
    /*服务台物品情况*/
    private String serviceGoods;
    /*其他*/
    private String elseExchange;
    /*接班人备注*/
    private String receiveRemark;
    /*值班站长*/
    private String OndutyAgentName;
    /*值班站长id*/
    private String ondutyAgentId;

    /*规范上岗情况*/
    private String standardOn;
    /*交接班程序规范*/
    private String standardExchange;
    /*文明服务、工作纪律*/
    private String workRecord;
    /*票卡款一致情况*/
    private String numSame;
    /*亭内及广场卫生情况*/
    private String inAndOutDirty;
    /*检查人备注*/
    private String checkRemark;

    /*交班班长*/
    private String exchangeSignature;
    /*接班班长*/
    private String recieveSignature;
    /*值班站长*/
    private String ondutySignature;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    @Generated(hash = 234967816)
    public MonitorOndutyRecorde(Long id, String userId, String personId, String staionId, String timeStamp,
            String monitorName, String date, String weekday, String team, String teamId, String teamClass, String outPeople,
            String outPeopleId, String weather, String totalFee, String backCardNum, String outCardNum,
            List<CarInOutEntity> carInList, List<CarInOutEntity> carOutList, String beforeOnduty, String duringOnduty,
            String afterOnduty, String ondutyRemark, String receivePeople, String receivePeopleId, String moneyExchange,
            String goodsExchange, String machineRun, String inOutDirty, String serviceGoods, String elseExchange,
            String receiveRemark, String OndutyAgentName, String ondutyAgentId, String standardOn, String standardExchange,
            String workRecord, String numSame, String inAndOutDirty, String checkRemark, String exchangeSignature,
            String recieveSignature, String ondutySignature, boolean isSubmit, boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.timeStamp = timeStamp;
        this.monitorName = monitorName;
        this.date = date;
        this.weekday = weekday;
        this.team = team;
        this.teamId = teamId;
        this.teamClass = teamClass;
        this.outPeople = outPeople;
        this.outPeopleId = outPeopleId;
        this.weather = weather;
        this.totalFee = totalFee;
        this.backCardNum = backCardNum;
        this.outCardNum = outCardNum;
        this.carInList = carInList;
        this.carOutList = carOutList;
        this.beforeOnduty = beforeOnduty;
        this.duringOnduty = duringOnduty;
        this.afterOnduty = afterOnduty;
        this.ondutyRemark = ondutyRemark;
        this.receivePeople = receivePeople;
        this.receivePeopleId = receivePeopleId;
        this.moneyExchange = moneyExchange;
        this.goodsExchange = goodsExchange;
        this.machineRun = machineRun;
        this.inOutDirty = inOutDirty;
        this.serviceGoods = serviceGoods;
        this.elseExchange = elseExchange;
        this.receiveRemark = receiveRemark;
        this.OndutyAgentName = OndutyAgentName;
        this.ondutyAgentId = ondutyAgentId;
        this.standardOn = standardOn;
        this.standardExchange = standardExchange;
        this.workRecord = workRecord;
        this.numSame = numSame;
        this.inAndOutDirty = inAndOutDirty;
        this.checkRemark = checkRemark;
        this.exchangeSignature = exchangeSignature;
        this.recieveSignature = recieveSignature;
        this.ondutySignature = ondutySignature;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
    }


    @Generated(hash = 748382037)
    public MonitorOndutyRecorde() {
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


    public String getMonitorName() {
        return this.monitorName;
    }


    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }


    public String getDate() {
        return this.date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getWeekday() {
        return this.weekday;
    }


    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }


    public String getTeam() {
        return this.team;
    }


    public void setTeam(String team) {
        this.team = team;
    }


    public String getTeamId() {
        return this.teamId;
    }


    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }


    public String getTeamClass() {
        return this.teamClass;
    }


    public void setTeamClass(String teamClass) {
        this.teamClass = teamClass;
    }


    public String getOutPeople() {
        return this.outPeople;
    }


    public void setOutPeople(String outPeople) {
        this.outPeople = outPeople;
    }


    public String getOutPeopleId() {
        return this.outPeopleId;
    }


    public void setOutPeopleId(String outPeopleId) {
        this.outPeopleId = outPeopleId;
    }


    public String getWeather() {
        return this.weather;
    }


    public void setWeather(String weather) {
        this.weather = weather;
    }


    public String getTotalFee() {
        return this.totalFee;
    }


    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }


    public String getBackCardNum() {
        return this.backCardNum;
    }


    public void setBackCardNum(String backCardNum) {
        this.backCardNum = backCardNum;
    }


    public String getOutCardNum() {
        return this.outCardNum;
    }


    public void setOutCardNum(String outCardNum) {
        this.outCardNum = outCardNum;
    }


    public List<CarInOutEntity> getCarInList() {
        return this.carInList;
    }


    public void setCarInList(List<CarInOutEntity> carInList) {
        this.carInList = carInList;
    }


    public List<CarInOutEntity> getCarOutList() {
        return this.carOutList;
    }


    public void setCarOutList(List<CarInOutEntity> carOutList) {
        this.carOutList = carOutList;
    }


    public String getBeforeOnduty() {
        return this.beforeOnduty;
    }


    public void setBeforeOnduty(String beforeOnduty) {
        this.beforeOnduty = beforeOnduty;
    }


    public String getDuringOnduty() {
        return this.duringOnduty;
    }


    public void setDuringOnduty(String duringOnduty) {
        this.duringOnduty = duringOnduty;
    }


    public String getAfterOnduty() {
        return this.afterOnduty;
    }


    public void setAfterOnduty(String afterOnduty) {
        this.afterOnduty = afterOnduty;
    }


    public String getOndutyRemark() {
        return this.ondutyRemark;
    }


    public void setOndutyRemark(String ondutyRemark) {
        this.ondutyRemark = ondutyRemark;
    }


    public String getReceivePeople() {
        return this.receivePeople;
    }


    public void setReceivePeople(String receivePeople) {
        this.receivePeople = receivePeople;
    }


    public String getReceivePeopleId() {
        return this.receivePeopleId;
    }


    public void setReceivePeopleId(String receivePeopleId) {
        this.receivePeopleId = receivePeopleId;
    }


    public String getMoneyExchange() {
        return this.moneyExchange;
    }


    public void setMoneyExchange(String moneyExchange) {
        this.moneyExchange = moneyExchange;
    }


    public String getGoodsExchange() {
        return this.goodsExchange;
    }


    public void setGoodsExchange(String goodsExchange) {
        this.goodsExchange = goodsExchange;
    }


    public String getMachineRun() {
        return this.machineRun;
    }


    public void setMachineRun(String machineRun) {
        this.machineRun = machineRun;
    }


    public String getInOutDirty() {
        return this.inOutDirty;
    }


    public void setInOutDirty(String inOutDirty) {
        this.inOutDirty = inOutDirty;
    }


    public String getServiceGoods() {
        return this.serviceGoods;
    }


    public void setServiceGoods(String serviceGoods) {
        this.serviceGoods = serviceGoods;
    }


    public String getElseExchange() {
        return this.elseExchange;
    }


    public void setElseExchange(String elseExchange) {
        this.elseExchange = elseExchange;
    }


    public String getReceiveRemark() {
        return this.receiveRemark;
    }


    public void setReceiveRemark(String receiveRemark) {
        this.receiveRemark = receiveRemark;
    }


    public String getStandardOn() {
        return this.standardOn;
    }


    public void setStandardOn(String standardOn) {
        this.standardOn = standardOn;
    }


    public String getStandardExchange() {
        return this.standardExchange;
    }


    public void setStandardExchange(String standardExchange) {
        this.standardExchange = standardExchange;
    }


    public String getWorkRecord() {
        return this.workRecord;
    }


    public void setWorkRecord(String workRecord) {
        this.workRecord = workRecord;
    }


    public String getNumSame() {
        return this.numSame;
    }


    public void setNumSame(String numSame) {
        this.numSame = numSame;
    }


    public String getInAndOutDirty() {
        return this.inAndOutDirty;
    }


    public void setInAndOutDirty(String inAndOutDirty) {
        this.inAndOutDirty = inAndOutDirty;
    }


    public String getCheckRemark() {
        return this.checkRemark;
    }


    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }


    public String getExchangeSignature() {
        return this.exchangeSignature;
    }


    public void setExchangeSignature(String exchangeSignature) {
        this.exchangeSignature = exchangeSignature;
    }


    public String getRecieveSignature() {
        return this.recieveSignature;
    }


    public void setRecieveSignature(String recieveSignature) {
        this.recieveSignature = recieveSignature;
    }


    public String getOndutySignature() {
        return this.ondutySignature;
    }


    public void setOndutySignature(String ondutySignature) {
        this.ondutySignature = ondutySignature;
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


    public String getOndutyAgentId() {
        return this.ondutyAgentId;
    }


    public void setOndutyAgentId(String ondutyAgentId) {
        this.ondutyAgentId = ondutyAgentId;
    }


    public String getOndutyAgentName() {
        return this.OndutyAgentName;
    }


    public void setOndutyAgentName(String OndutyAgentName) {
        this.OndutyAgentName = OndutyAgentName;
    }


    public static class CarInOutConverent implements PropertyConverter<List<CarInOutEntity>, String> {
        @Override
        public List<CarInOutEntity> convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<ArrayList<CarInOutEntity>>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(List<CarInOutEntity> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }

}
