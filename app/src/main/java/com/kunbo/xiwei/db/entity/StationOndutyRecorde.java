package com.kunbo.xiwei.db.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunbo.xiwei.modle.StationTeamEntity;
import com.zyf.model.MyData;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 站长值班记录
 */
@Entity
public class StationOndutyRecorde {
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
    /*站长*/
    private String stationName;
    /*票证*/
    private String ticketName;
    /*票证id*/
    private String ticketId;
    /*日期*/
    private String date;
    /*星期*/
    private String weekday;
    /*天气*/
    private String weather;
    /*记录编号*/
    private String recordeNo;
    /*班次*/
    @Convert(/**指定转换器 **/converter = TeamConvert.class,/**指定数据库中的列字段**/columnType = String.class)
    private List<StationTeamEntity> teamList;
    /*征费额*/
    private String totalFee;
    /*车流量*/
    private String carNum;
    /*检测量*/
    private String checkNum;
    /*劝返量*/
    private String returnNum;
    /*预免金额*/
    private String exemptionAmount;
    /*入口客流量*/
    private String inFlow;
    /*出口客流量*/
    private String outFlow;

    /*上级通知检查内容*/
    private String notifyCheck;
    /*落实处理情况*/
    private String implementState;

    /*班前要求*/
    private String beforeOnduty;
    /*当班期间*/
    @Convert(/**指定转换器 **/converter = OndutyConvert.class,/**指定数据库中的列字段**/columnType = String.class)
    private MyData ondutyList;
    /*班后小结*/
    private String afterOnduty;
    /*当班期间*/
    private String duringOnduty;
    /*待办事项*/
    private String todo;
    private String remark;
    /*交班站长*/
    private String exchangeSignature;
    /*接班站长*/
    private String recieveSignature;
    /*是否提交*/
    private boolean isSubmit;
    /*是否完整*/
    private boolean isComplete;

    public StationOndutyRecorde() {
    }


    @Generated(hash = 1443707458)
    public StationOndutyRecorde(Long id, String userId, String personId, String staionId,
            String timeStamp, String stationName, String ticketName, String ticketId, String date,
            String weekday, String weather, String recordeNo, List<StationTeamEntity> teamList,
            String totalFee, String carNum, String checkNum, String returnNum, String exemptionAmount,
            String inFlow, String outFlow, String notifyCheck, String implementState,
            String beforeOnduty, MyData ondutyList, String afterOnduty, String duringOnduty,
            String todo, String remark, String exchangeSignature, String recieveSignature,
            boolean isSubmit, boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.personId = personId;
        this.staionId = staionId;
        this.timeStamp = timeStamp;
        this.stationName = stationName;
        this.ticketName = ticketName;
        this.ticketId = ticketId;
        this.date = date;
        this.weekday = weekday;
        this.weather = weather;
        this.recordeNo = recordeNo;
        this.teamList = teamList;
        this.totalFee = totalFee;
        this.carNum = carNum;
        this.checkNum = checkNum;
        this.returnNum = returnNum;
        this.exemptionAmount = exemptionAmount;
        this.inFlow = inFlow;
        this.outFlow = outFlow;
        this.notifyCheck = notifyCheck;
        this.implementState = implementState;
        this.beforeOnduty = beforeOnduty;
        this.ondutyList = ondutyList;
        this.afterOnduty = afterOnduty;
        this.duringOnduty = duringOnduty;
        this.todo = todo;
        this.remark = remark;
        this.exchangeSignature = exchangeSignature;
        this.recieveSignature = recieveSignature;
        this.isSubmit = isSubmit;
        this.isComplete = isComplete;
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

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTicketName() {
        return this.ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketId() {
        return this.ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
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

    public String getWeather() {
        return this.weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getRecordeNo() {
        return this.recordeNo;
    }

    public void setRecordeNo(String recordeNo) {
        this.recordeNo = recordeNo;
    }

    public List<StationTeamEntity> getTeamList() {
        return this.teamList;
    }

    public void setTeamList(List<StationTeamEntity> teamList) {
        this.teamList = teamList;
    }

    public String getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getCarNum() {
        return this.carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCheckNum() {
        return this.checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getReturnNum() {
        return this.returnNum;
    }

    public void setReturnNum(String returnNum) {
        this.returnNum = returnNum;
    }

    public String getExemptionAmount() {
        return this.exemptionAmount;
    }

    public void setExemptionAmount(String exemptionAmount) {
        this.exemptionAmount = exemptionAmount;
    }

    public String getInFlow() {
        return this.inFlow;
    }

    public void setInFlow(String inFlow) {
        this.inFlow = inFlow;
    }

    public String getOutFlow() {
        return this.outFlow;
    }

    public void setOutFlow(String outFlow) {
        this.outFlow = outFlow;
    }

    public String getNotifyCheck() {
        return this.notifyCheck;
    }

    public void setNotifyCheck(String notifyCheck) {
        this.notifyCheck = notifyCheck;
    }

    public String getImplementState() {
        return this.implementState;
    }

    public void setImplementState(String implementState) {
        this.implementState = implementState;
    }

    public String getBeforeOnduty() {
        return this.beforeOnduty;
    }

    public void setBeforeOnduty(String beforeOnduty) {
        this.beforeOnduty = beforeOnduty;
    }

    public MyData getOndutyList() {
        return this.ondutyList;
    }

    public void setOndutyList(MyData ondutyList) {
        this.ondutyList = ondutyList;
    }

    public String getAfterOnduty() {
        return this.afterOnduty;
    }

    public void setAfterOnduty(String afterOnduty) {
        this.afterOnduty = afterOnduty;
    }

    public String getTodo() {
        return this.todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public String getDuringOnduty() {
        return this.duringOnduty;
    }


    public void setDuringOnduty(String duringOnduty) {
        this.duringOnduty = duringOnduty;
    }


    public static class TeamConvert implements PropertyConverter<List<StationTeamEntity>, String> {
        @Override
        public List<StationTeamEntity> convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<ArrayList<StationTeamEntity>>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(List<StationTeamEntity> entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }

    public static class OndutyConvert implements PropertyConverter<MyData, String> {
        @Override
        public MyData convertToEntityProperty(String databaseValue) {
            Type type = new TypeToken<MyData>() {
            }.getType();
            return new Gson().fromJson(databaseValue, type);
        }

        @Override
        public String convertToDatabaseValue(MyData entityProperty) {
            return new Gson().toJson(entityProperty);
        }
    }
}
