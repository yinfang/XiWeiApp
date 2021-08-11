package com.kunbo.xiwei.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.db.entity.CivilizedServiceForm;
import com.kunbo.xiwei.db.entity.ComplaintForm;
import com.kunbo.xiwei.db.entity.InspectionForm;
import com.kunbo.xiwei.db.entity.MonitorOndutyRecorde;
import com.kunbo.xiwei.db.entity.OverHighCarIn;
import com.kunbo.xiwei.db.entity.OverHighCarOut;
import com.kunbo.xiwei.db.entity.SecurityCheckRecorde;
import com.kunbo.xiwei.db.entity.SpecialForm;
import com.kunbo.xiwei.db.entity.StationOndutyRecorde;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.CarInOutEntity;
import com.kunbo.xiwei.modle.SecurityItem;
import com.kunbo.xiwei.modle.StationTeamEntity;
import com.kunbo.xiwei.modle.User;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.OkHttpUtil;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.zyf.net.api.ApiSubscribe.uploadCivilizedService;
import static com.zyf.net.api.ApiSubscribe.uploadComplaintForm;
import static com.zyf.net.api.ApiSubscribe.uploadInspectionForm;
import static com.zyf.net.api.ApiSubscribe.uploadMonitorRecorde;
import static com.zyf.net.api.ApiSubscribe.uploadOverHighCarIn;
import static com.zyf.net.api.ApiSubscribe.uploadOverHighCarOut;
import static com.zyf.net.api.ApiSubscribe.uploadPhotos;
import static com.zyf.net.api.ApiSubscribe.uploadSecurityCheck;
import static com.zyf.net.api.ApiSubscribe.uploadSpecailForm;
import static com.zyf.net.api.ApiSubscribe.uploadStationRecorde;
import static com.zyf.net.api.ApiSubscribe.uploadStationTeams;

/**
 * 定时提交数据
 */
public class SubmitDataService extends Service implements OnSuccessAndFaultListener {
    private List<StationOndutyRecorde> tmpStationOndutyRecorde = new ArrayList<>();//站长值班记录
    private List<MonitorOndutyRecorde> tmpMonitorOndutyRecorde = new ArrayList<>();//班长值班记录
    private List<SpecialForm> tmpSpecialRecorde = new ArrayList<>();//特情记录
    private List<CivilizedServiceForm> tmpCivilizedRecorde = new ArrayList<>();//文明服务记录
    private List<OverHighCarIn> tmpOverCarinRecorde = new ArrayList<>();//治超入口查验记录
    private List<OverHighCarOut> tmpOverCaroutRecorde = new ArrayList<>();//治超出口查验记录
    private List<SecurityCheckRecorde> tmpSecurityCheckRecorde = new ArrayList<>();//安全巡检记录
    private List<ComplaintForm> tmpComplaintRecorde = new ArrayList<>();//投诉管理记录
    private List<InspectionForm> tmpInspectionRecorde = new ArrayList<>();//稽查管理记录
    private static final String TAG = "Service";
    //15分钟执行一次
    private final int AFTER_SECONDS_TIME = 60 * 15;
    private StationOndutyRecorde recordStationOnduty = null;
    private MonitorOndutyRecorde recordMonitorOnduty = null;
    private SpecialForm recordSpecial = null;
    private CivilizedServiceForm recordCivilized = null;
    private OverHighCarIn recordOverIn = null;
    private OverHighCarOut recordOverOut = null;
    private ComplaintForm recordComplaint = null;
    private SecurityCheckRecorde recordSecurityCheck = null;
    private InspectionForm recordInspection = null;
    private boolean hasDone0, hasDone1, hasDone2, hasDone3, hasDone4, hasDone5, hasDone6, hasDone7, hasDone8;//本次上传是否完成

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hasDone0 = false;
        hasDone1 = false;
        hasDone2 = false;
        hasDone3 = false;
        hasDone4 = false;
        hasDone5 = false;
        hasDone6 = false;
        hasDone7 = false;
        hasDone8 = false;

        startPost();
        /*new Thread(() -> {
            if (BC.CAN_UPLAOD && AppUtil.isNetworkConnected(getApplicationContext()))
                startPost();
        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int minute = AFTER_SECONDS_TIME * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + minute;
        Intent i = new Intent(this, SubmitDataService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        assert manager != null;
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);*/
        return super.onStartCommand(intent, flags, startId);
    }

    private void startPost() {
        sendCast(true);
        Log.d(TAG, "———————-开始查询数据库——————.");
        //站长值班记录数据
        List<StationOndutyRecorde> records = DaoUtils.getStationOndutyInstance().loadAllData();
        if (records != null && records.size() > 0) {
            tmpStationOndutyRecorde.clear();
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).getIsComplete() && records.get(i).getIsSubmit()) {
                    //如果数据完整且点击了提交按钮
                    tmpStationOndutyRecorde.add(records.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的站长值班数据——————" + tmpStationOndutyRecorde.size());
        }
        //班长值班记录数据
        List<MonitorOndutyRecorde> records1 = DaoUtils.getMonitorOndutyInstance().loadAllData();
        if (records1 != null && records1.size() > 0) {
            tmpMonitorOndutyRecorde.clear();
            for (int i = 0; i < records1.size(); i++) {
                if (records1.get(i).getIsComplete() && records1.get(i).getIsSubmit()) {
                    tmpMonitorOndutyRecorde.add(records1.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的班长值班数据——————" + tmpMonitorOndutyRecorde.size());
        }
        //特情记录数据
        List<SpecialForm> records2 = DaoUtils.getSpecialDoneInstance().loadAllData();
        if (records2 != null && records2.size() > 0) {
            tmpSpecialRecorde.clear();
            for (int i = 0; i < records2.size(); i++) {
                if (records2.get(i).getIsComplete() && records2.get(i).getIsSubmit()) {
                    tmpSpecialRecorde.add(records2.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的特情数据——————" + tmpSpecialRecorde.size());
        }
        //文明服务数据
        List<CivilizedServiceForm> records3 = DaoUtils.getCivilizedServiceInstance().loadAllData();
        if (records3 != null && records3.size() > 0) {
            tmpCivilizedRecorde.clear();
            for (int i = 0; i < records3.size(); i++) {
                if (records3.get(i).getIsComplete() && records3.get(i).getIsSubmit()) {
                    tmpCivilizedRecorde.add(records3.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的文明服务数据——————" + tmpCivilizedRecorde.size());
        }
        //治超入口查验数据
        List<OverHighCarIn> records4 = DaoUtils.getOverHighInInstance().loadAllData();
        if (records4 != null && records4.size() > 0) {
            tmpOverCarinRecorde.clear();
            for (int i = 0; i < records4.size(); i++) {
                if (records4.get(i).getIsComplete() && records4.get(i).getIsSubmit()) {
                    tmpOverCarinRecorde.add(records4.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的治超入口查验数据——————" + tmpOverCarinRecorde.size());
        }
        //治超出口查验数据
        List<OverHighCarOut> records5 = DaoUtils.getOverHighOutInstance().loadAllData();
        if (records5 != null && records5.size() > 0) {
            tmpOverCaroutRecorde.clear();
            for (int i = 0; i < records5.size(); i++) {
                if (records5.get(i).getIsComplete() && records5.get(i).getIsSubmit()) {
                    tmpOverCaroutRecorde.add(records5.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的治超出口查验数据——————" + tmpOverCaroutRecorde.size());
        }
        //安全巡检数据
        List<SecurityCheckRecorde> records6 = DaoUtils.getSecurityCheckInstance().loadAllData();
        if (records6 != null && records6.size() > 0) {
            tmpSecurityCheckRecorde.clear();
            tmpSecurityCheckRecorde.addAll(records6);
            Log.d(TAG, "——————有可提交的安全巡检数据——————" + tmpSecurityCheckRecorde.size());
        }
        //投诉管理数据
        List<ComplaintForm> records7 = DaoUtils.getComplaintFormInstance().loadAllData();
        if (records7 != null && records7.size() > 0) {
            tmpComplaintRecorde.clear();
            for (int i = 0; i < records7.size(); i++) {
                if (records7.get(i).getIsComplete() && records7.get(i).getIsSubmit()) {
                    tmpComplaintRecorde.add(records7.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的投诉管理数据——————" + tmpComplaintRecorde.size());
        }
        //稽查管理数据
        List<InspectionForm> records8 = DaoUtils.getInspectionFormInstance().loadAllData();
        if (records8 != null && records8.size() > 0) {
            tmpInspectionRecorde.clear();
            for (int i = 0; i < records8.size(); i++) {
                if (records8.get(i).getIsComplete() && records8.get(i).getIsSubmit()) {
                    tmpInspectionRecorde.add(records8.get(i));
                }
            }
            Log.d(TAG, "——————有可提交的稽查数据——————" + tmpInspectionRecorde.size());
        }

        doUpload();
    }

    private void doUpload() {
        if (tmpStationOndutyRecorde.size() > 0) {
            recordStationOnduty = tmpStationOndutyRecorde.get(tmpStationOndutyRecorde.size() - 1);
            if (recordStationOnduty != null)
                submit(recordStationOnduty);
        } else if (tmpStationOndutyRecorde.size() == 0) {
            hasDone0 = true;
        }

        if (hasDone0 && tmpMonitorOndutyRecorde.size() > 0) {
            recordMonitorOnduty = tmpMonitorOndutyRecorde.get(tmpMonitorOndutyRecorde.size() - 1);
            if (recordMonitorOnduty != null)
                submit(recordMonitorOnduty);
        } else if (tmpMonitorOndutyRecorde.size() == 0) {
            hasDone1 = true;
        }

        if (hasDone0 && hasDone1 && tmpSpecialRecorde.size() > 0) {
            recordSpecial = tmpSpecialRecorde.get(tmpSpecialRecorde.size() - 1);
            if (recordSpecial != null)
                submit(recordSpecial);
        } else if (tmpSpecialRecorde.size() == 0) {
            hasDone2 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && tmpCivilizedRecorde.size() > 0) {
            recordCivilized = tmpCivilizedRecorde.get(tmpCivilizedRecorde.size() - 1);
            if (recordCivilized != null)
                submit(recordCivilized);
        } else if (tmpCivilizedRecorde.size() == 0) {
            hasDone3 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && hasDone3 && tmpOverCarinRecorde.size() > 0) {
            recordOverIn = tmpOverCarinRecorde.get(tmpOverCarinRecorde.size() - 1);
            if (recordOverIn != null)
                submit(recordOverIn);
        } else if (tmpOverCarinRecorde.size() == 0) {
            hasDone4 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && hasDone3 && hasDone4 && tmpOverCaroutRecorde.size() > 0) {
            recordOverOut = tmpOverCaroutRecorde.get(tmpOverCaroutRecorde.size() - 1);
            if (recordOverOut != null)
                submit(recordOverOut);
        } else if (tmpOverCaroutRecorde.size() == 0) {
            hasDone5 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && hasDone3 && hasDone4 && hasDone5 && tmpComplaintRecorde.size() > 0) {
            recordComplaint = tmpComplaintRecorde.get(tmpComplaintRecorde.size() - 1);
            if (recordComplaint != null)
                submit(recordComplaint);
        } else if (tmpComplaintRecorde.size() == 0) {
            hasDone6 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && hasDone3 && hasDone4 && hasDone5 && hasDone6 && tmpSecurityCheckRecorde.size() > 0) {
            recordSecurityCheck = tmpSecurityCheckRecorde.get(tmpSecurityCheckRecorde.size() - 1);
            if (recordSecurityCheck != null)
                submit(recordSecurityCheck);
        } else if (tmpSecurityCheckRecorde.size() == 0) {
            hasDone7 = true;
        }

        if (hasDone0 && hasDone1 && hasDone2 && hasDone3 && hasDone4 && hasDone5 && hasDone6 && hasDone7 && tmpInspectionRecorde.size() > 0) {
            recordInspection = tmpInspectionRecorde.get(tmpInspectionRecorde.size() - 1);
            if (recordInspection != null)
                submit(recordInspection);
        } else if (tmpInspectionRecorde.size() == 0) {
            hasDone8 = true;
        }
    }

    /**
     * 数据提交
     *
     * @param record
     */
    private void submit(Object record) {
        MyRow request;
        String[] clazz = record.getClass().getName().split("\\.");
        switch (clazz[clazz.length - 1]) {
            case "StationOndutyRecorde"://站长值班记录
                request = new MyRow();
                request.put("userId", User.getInstance().getUserId());
                request.put("personId", User.getInstance().getPersonId());
                request.put("stationId", User.getInstance().getStationId());
                request.put("timeStamp", recordStationOnduty.getTimeStamp());
                request.put("dutyDate", recordStationOnduty.getDate());
                request.put("weekDay", recordStationOnduty.getWeekday());
                request.put("weather", recordStationOnduty.getWeather());
                request.put("visitNo", recordStationOnduty.getRecordeNo());
                request.put("stationHeaderName", recordStationOnduty.getStationName());
                request.put("ticketName", recordStationOnduty.getTicketName());
                request.put("ticketId", recordStationOnduty.getTicketId());
                request.put("totalFee", recordStationOnduty.getTotalFee());
                request.put("checkNum", recordStationOnduty.getCheckNum());
                request.put("returnNum", recordStationOnduty.getReturnNum());
                request.put("carNum", recordStationOnduty.getCarNum());
                request.put("exemptionAmountInAdvance", recordStationOnduty.getExemptionAmount());
                request.put("inletFlow", recordStationOnduty.getInFlow());
                request.put("exitFlow", recordStationOnduty.getOutFlow());
                request.put("notifyCheck", recordStationOnduty.getNotifyCheck());
                request.put("implementState", recordStationOnduty.getImplementState());

                request.put("beforeOnduty", recordStationOnduty.getBeforeOnduty());
                /*StringBuffer sb = new StringBuffer();
                int size = tmpStationOndutyRecorde.getOndutyList().size();
                for (int i = 0; i < size; i++) {
                    MyRow row = tmpStationOndutyRecorde.getOndutyList().get(i);
                    if (i == size - 1) {
                        if (Utils.checkNullString(row.getString("remark"))) {
                            sb.append(row.getString("content"));
                        } else {
                            sb.append(row.getString("content") + "\n" + "异常描述：" + row.getString("remark"));
                        }
                        break;
                    }
                    sb.append(row.getString("content") + ((row.getBoolean("normal") ? "(正常)" : "(异常)") + "\n"));
                }*/
                request.put("duringOnduty", recordStationOnduty.getDuringOnduty());
                request.put("afterOnduty", recordStationOnduty.getAfterOnduty());

                request.put("backlog", recordStationOnduty.getTodo());
                request.put("remark", recordStationOnduty.getRemark());
                request.put("shiftExchangeSignatureBase64", recordStationOnduty.getExchangeSignature());
                request.put("succeedSignatureBase64", recordStationOnduty.getRecieveSignature());

                uploadStationRecorde(request, new OnSuccessAndFaultSub(this, "uploadStationRecorde", getApplicationContext(), false));
                break;
            case "MonitorOndutyRecorde"://班长值班记录
                request = new MyRow();
                request.put("teamId", User.getInstance().getTeamId());
                request.put("personId", User.getInstance().getPersonId());
                request.put("stationId", User.getInstance().getStationId());
                request.put("timeStamp", recordMonitorOnduty.getTimeStamp());
                request.put("dutyDate", recordMonitorOnduty.getDate());
                request.put("weekDay", recordMonitorOnduty.getWeekday());
                request.put("weather", recordMonitorOnduty.getWeather());
                request.put("visitNo", "000");
                request.put("shiftType", recordMonitorOnduty.getTeam());//班次
                request.put("draftedBy", recordMonitorOnduty.getTotalFee());//收费额
                request.put("cardRecycleNum", recordMonitorOnduty.getBackCardNum());
                request.put("cardCreditNum", recordMonitorOnduty.getOutCardNum());
                request.put("outworkerId", recordMonitorOnduty.getOutPeopleId());

                request.put("beforeOnduty", recordMonitorOnduty.getBeforeOnduty());
                request.put("duringOnduty", recordMonitorOnduty.getDuringOnduty());
                request.put("afterOnduty", recordMonitorOnduty.getAfterOnduty());
                request.put("remark", recordMonitorOnduty.getOndutyRemark());
                request.put("shiftExchangeSignatureBase64", recordMonitorOnduty.getExchangeSignature());

                MyData laneData = new MyData();
                for (CarInOutEntity entity : recordMonitorOnduty.getCarInList()) {
                    MyRow laneRow = new MyRow();
                    laneRow.put("laneId", entity.getLaneId());
                    //laneRow.put("timeRange", entity.getTimeRange());
                    laneRow.put("staffList", entity.getFeeMans());
                    laneData.add(laneRow);
                }
                for (CarInOutEntity entity : recordMonitorOnduty.getCarOutList()) {
                    MyRow laneRow = new MyRow();
                    laneRow.put("laneId", entity.getLaneId());
                    //laneRow.put("timeRange", entity.getTimeRange());
                    laneRow.put("staffList", entity.getFeeMans());
                    laneData.add(laneRow);
                }
                request.put("assemble", laneData);
                request.put("succeedMonitorId", recordMonitorOnduty.getReceivePeopleId());
                request.put("dutyStationId", recordMonitorOnduty.getOndutyAgentId());

                MyRow receiveRow = new MyRow();
                receiveRow.put("succeedImprest", recordMonitorOnduty.getMoneyExchange());
                receiveRow.put("succeedLaneItems", recordMonitorOnduty.getGoodsExchange());
                receiveRow.put("succeedLaneEquipment", recordMonitorOnduty.getMachineRun());
                receiveRow.put("succeedSanitation", recordMonitorOnduty.getInOutDirty());
                receiveRow.put("succeedServiceProduct", recordMonitorOnduty.getServiceGoods());
                receiveRow.put("succeedElse", recordMonitorOnduty.getElseExchange());
                receiveRow.put("remark", recordMonitorOnduty.getReceiveRemark());
                receiveRow.put("succeedSignatureBase64", recordMonitorOnduty.getRecieveSignature());
                request.put("succession", receiveRow);

                MyRow ondutyRow = new MyRow();
                ondutyRow.put("standardMountGuard", recordMonitorOnduty.getStandardOn());
                ondutyRow.put("standardShiftProcedure", recordMonitorOnduty.getStandardExchange());
                ondutyRow.put("civilServiceWork", recordMonitorOnduty.getWorkRecord());
                ondutyRow.put("isRidesWithFit", recordMonitorOnduty.getNumSame());
                ondutyRow.put("kioskAndSquareHealth", recordMonitorOnduty.getInAndOutDirty());
                ondutyRow.put("remark", recordMonitorOnduty.getCheckRemark());
                ondutyRow.put("siteOfficeBase64", recordMonitorOnduty.getOndutySignature());
                request.put("siteExamine", ondutyRow);

                uploadMonitorRecorde(request, new OnSuccessAndFaultSub(this, "uploadMonitorRecorde", getApplicationContext(), false));
                break;
            case "SpecialForm"://特情处理
                if (recordSpecial.getPhotos() != null && recordSpecial.getPhotos().size() > 0) {
                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    //放入图片
                    for (String photo : recordSpecial.getPhotos()) {
                        File file = new File(photo);
                        if (file.exists()) {
                            RequestBody requestBody = OkHttpUtil.toRequestBodyOfImage(file);
                            imgRequest.addFormDataPart("mFile", file.getName(), requestBody);
                        }
                    }
                    uploadPhotos(imgRequest, new OnSuccessAndFaultSub(this, "uploadSpecialPhotos", getApplicationContext(), false));
                } else {
                    uploadSpecialData("");
                }

                break;
            case "CivilizedServiceForm"://文明服务
                request = new MyRow();
                request.put("stationId", User.getInstance().getStationId());
                request.put("serviceDate", recordCivilized.getDate());
                request.put("serviceTime", recordCivilized.getTime());
                request.put("quantity", recordCivilized.getNum());
                request.put("licensePlateNumber", recordCivilized.getCarNo());
                request.put("serviceClassify", recordCivilized.getServiceTypeId());
                request.put("useItems", recordCivilized.getUseGoods());
                request.put("remark", recordCivilized.getRemark());
                request.put("fieldSignatureBase64", recordCivilized.getOutSingnature());
                uploadCivilizedService(request, new OnSuccessAndFaultSub(this, "uploadCivilizedService", getApplicationContext(), false));
                break;
            case "OverHighCarIn"://治超入口查验记录
                if (recordOverIn.getImages() != null && recordOverIn.getImages().size() > 0) {
                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    //放入图片
                    for (String photo : recordOverIn.getImages()) {
                        File file = new File(photo);
                        if (file.exists()) {
                            RequestBody requestBody = OkHttpUtil.toRequestBodyOfImage(file);
                            imgRequest.addFormDataPart("mFile", file.getName(), requestBody);
                        }
                    }
                    uploadPhotos(imgRequest, new OnSuccessAndFaultSub(this, "uploadOverCarInPhotos", getApplicationContext(), false));
                } else {
                    uploadOverCarInData("");
                }
                break;
            case "OverHighCarOut"://治超出口查验记录
                if (recordOverOut.getImages() != null && recordOverOut.getImages().size() > 0) {
                    //放入图片
                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (String photo : recordOverOut.getImages()) {
                        File file = new File(photo);
                        if (file.exists()) {
                            RequestBody requestBody = OkHttpUtil.toRequestBodyOfImage(file);
                            imgRequest.addFormDataPart("mFile", file.getName(), requestBody);
                        }
                    }
                    uploadPhotos(imgRequest, new OnSuccessAndFaultSub(this, "uploadOverCarOutPhotos", getApplicationContext(), false));
                } else {
                    uploadOverCarOutData("");
                }
                break;
            case "SecurityCheckRecorde"://安全巡检记录
                MyData data = new MyData();
                for (SecurityItem entity : recordSecurityCheck.getCheckList()) {
                    if (entity.isComplete() && entity.isSubmit()) {//选中的完整巡检项
                        MyRow row = new MyRow();
                        row.put("stationId", User.getInstance().getStationId());
                        row.put("configInfoId", entity.getId());
                        String[] dateTime = entity.getExamineTime().split(" ");
                        row.put("examineDate", dateTime[0]);
                        row.put("examineTime", dateTime[1]);
                        row.put("examinePersonId", entity.getCheckerId());
                        row.put("examineTitles", entity.getState());
                        row.put("remark", entity.getRemark());
                        data.add(row);
                    }
                }
                uploadSecurityCheck(data, new OnSuccessAndFaultSub(this, "uploadSecurityCheck", getApplicationContext(), false));
                break;
            case "ComplaintForm"://投诉管理记录
                request = new MyRow();
                request.put("stationId", User.getInstance().getStationId());
                request.put("complaintFrom", recordComplaint.getComplainantFrom());
                request.put("complaintsTypeId", recordComplaint.getComplainantTypeId());
                request.put("complainant", recordComplaint.getComplainantSignature());
                request.put("receiver", recordComplaint.getName());
                String result = recordComplaint.getDoneAdvice();
                if (result.contains("已"))
                    request.put("complainHandlerPersonId", recordComplaint.getPersonId());
                String[] dateT = recordComplaint.getDate().split(" ");
                request.put("complainDate", dateT[0]);
                request.put("complainTime", dateT[1]);
                request.put("ticketNumber", recordComplaint.getBillNo());
                request.put("licensePlateNumber", recordComplaint.getCarNo());
                request.put("contactInformation", recordComplaint.getMobileNo());
                request.put("complaintContent", recordComplaint.getComplaintDesc());
                request.put("investigationSituation", recordComplaint.getInvestigation());
                request.put("isSolve", recordComplaint.getDoneAdvice());
                uploadComplaintForm(request, new OnSuccessAndFaultSub(this, "uploadComplaintForm", getApplicationContext(), false));
                break;
            case "InspectionForm"://稽查管理记录
                request = new MyRow();
                request.put("stationId", User.getInstance().getStationId());
                request.put("auditDepartment", recordInspection.getInspection());
                request.put("chechedCompany", recordInspection.getInspected());
                String[] dateTime = recordInspection.getDateTime().split(" ");
                request.put("auditDate", dateTime[0]);
                request.put("auditTimeStamp", recordInspection.getDateTime());
                request.put("auditDetails", recordInspection.getContent());
                request.put("problemAbstract", recordInspection.getMainQes());
                request.put("result", recordInspection.getResult());
                request.put("party", recordInspection.getPartySignature());
                request.put("leader", recordInspection.getLeaderSignature());
                request.put("auditor", recordInspection.getInspectorSignature());
                request.put("remark", recordInspection.getRemark());
                request.put("inputSource", "移动端");
                uploadInspectionForm(request, new OnSuccessAndFaultSub(this, "uploadInspectionForm", getApplicationContext(), false));
                break;
        }
    }

    private void uploadSpecialData(String imgIds) {
        MyRow request = new MyRow();
        request.put("stationId", User.getInstance().getStationId());
        request.put("secretDate", recordSpecial.getDate());
        request.put("secretTime", recordSpecial.getTime());
        request.put("secretTeamId", User.getInstance().getTeamId());
        request.put("secretTeamType", recordSpecial.getClassType());
        request.put("secretLaneId", recordSpecial.getLaneId());
        request.put("secretPlateNumber", recordSpecial.getCarNo());
        request.put("secretVehicleTypeId", recordSpecial.getCarTypeId());
        request.put("secretClassifyId", recordSpecial.getTypeId());
        request.put("secretExplain", recordSpecial.getDetail());
        request.put("secretPersonId", recordSpecial.getPersonId());
        if (recordSpecial.getIsSolve().contains("无法")) {
            request.put("secretHandlerPersonId", recordSpecial.getSiteAgentId());
            request.put("secretHandlerPersonName", recordSpecial.getSiteAgent());
        } else {
            request.put("secretHandlerPersonId", recordSpecial.getPersonId());
            request.put("secretHandlerPersonName", recordSpecial.getName());
        }
        request.put("secretTollId", recordSpecial.getFeemanId());
        request.put("secretTollCard", recordSpecial.getJobNo());
        request.put("secretTicketId", "");
        request.put("secretOnDutySiteAgentId", recordSpecial.getSiteAgentId());
        request.put("secretIsNotSolve", recordSpecial.getIsSolve());
        request.put("remark", recordSpecial.getRemark());
        request.put("tollSignatureBase64", recordSpecial.getSignatureFeeman());
        request.put("monitorSignatureBase64", recordSpecial.getSignatureMonitor());
        request.put("imgIds", imgIds);
        uploadSpecailForm(request, new OnSuccessAndFaultSub(this, "uploadSpecailForm", getApplicationContext(), false));
    }

    private void uploadOverCarInData(String imgIds) {
        MyRow request = new MyRow();
        request.put("imgIds", imgIds);
        request.put("userId", User.getInstance().getUserId());
        request.put("personId", User.getInstance().getPersonId());
        request.put("stationId", User.getInstance().getStationId());
        request.put("serialNumber", "");
        request.put("thingMatter", recordOverIn.getReason());
        request.put("checkTeamId", recordOverIn.getCheckTeamId());
        request.put("basicCarLicencePlate", recordOverIn.getCarNo());
        request.put("basicCarMovingFilesNumber", recordOverIn.getFileNo());
        request.put("basicCarOwnerName", recordOverIn.getCarOwnerName());
        request.put("basicCarTradingCard", recordOverIn.getRunNo());

        request.put("partyName", recordOverIn.getName());
        request.put("partyVehicleRelation", recordOverIn.getRelation());
        request.put("partyCompanyName", recordOverIn.getCompany());
        request.put("partyCompanyLegalPerson", recordOverIn.getCompanyLegalPeople());
        String[] dateTime = recordOverIn.getCheckDate().split(" ");
        request.put("siteExamineDate", dateTime[0]);
        request.put("siteExamineTime", dateTime[1]);
        request.put("sitePassValidity", recordOverIn.getValidDate());
        request.put("sitePassageNumber", recordOverIn.getPassNo());
        request.put("siteIssueCompany", recordOverIn.getPassCompany());
        request.put("siteTestWeight", recordOverIn.getWeight());
        request.put("siteLoadSize", recordOverIn.getDimens());
        request.put("siteGoodsName", recordOverIn.getGoodsName());
        request.put("siteCarrierCompany", recordOverIn.getTransportCompany());
        request.put("siteIsMatch", recordOverIn.getIsEqual() ? "1" : "0");
        request.put("siteVerificationMode", recordOverIn.getCheckWay());
        request.put("siteIsTutelage", recordOverIn.getIscare() ? "1" : "0");
        request.put("siteGuardian", recordOverIn.getCarePerson());
        request.put("tellMatters", recordOverIn.getRules());
        request.put("siteOtherCondition", recordOverIn.getOther());
        request.put("informedDate", recordOverIn.getInformedDate());
        request.put("identifiedNumber", recordOverIn.getCertificateNo());

        request.put("nunciatusSignatureBase64", recordOverIn.getSignatureInformed());
        request.put("inspectoSignatureBase64", recordOverIn.getSignatureChecker());
        request.put("monitorSignatureBase64", recordOverIn.getSignatureMonitor());
        uploadOverHighCarIn(request, new OnSuccessAndFaultSub(this, "uploadOverHighCarIn", getApplicationContext(), false));
    }

    public void uploadOverCarOutData(String imgIds) {
        MyRow request = new MyRow();
        request.put("imgIds", imgIds);
        request.put("serialNumber", "");
        request.put("stationId", User.getInstance().getStationId());
        request.put("thingMatter", recordOverOut.getReason());
        request.put("checkTeamId", recordOverOut.getCheckTeamId());
        request.put("basicCarLicencePlate", recordOverOut.getCarNo());
        request.put("basicCarMovingFilesNumber", recordOverOut.getFileNo());
        request.put("basicCarOwnerName", recordOverOut.getOwnerName());
        request.put("basicCarTradingCard", recordOverOut.getRunNo());
        request.put("msgOddNumber", recordOverOut.getBillNo());
        request.put("msgGeometricDimension", recordOverOut.getDimens());
        request.put("msgTotalWeight", recordOverOut.getAllWeight());
        request.put("msgEntrWeight", recordOverOut.getInWeight());

        request.put("siteEntrStationName", recordOverOut.getInStation());
        request.put("siteEntrStationId", recordOverOut.getInStationId());
        request.put("sitePassValidity", recordOverOut.getValidDate());
        request.put("siteIssueCompany", recordOverOut.getPassCompany());
        request.put("sitePassageNumber", recordOverOut.getPassNo());
        String[] dateTime = recordOverOut.getOutDate().split(" ");
        request.put("siteExitDate", dateTime[0]);
        request.put("siteExitTime", dateTime[1]);
        request.put("siteExitTestWeight", recordOverOut.getOutWeight());
        request.put("siteGoodsName", recordOverOut.getGoodsName());
        request.put("siteAxleAllocation", recordOverOut.getAxleLoad());
        request.put("siteExitSize", recordOverOut.getOutDimens());
        request.put("siteIsLicence", recordOverOut.getHasCertificate() ? "1" : "0");
        request.put("siteIsMatch", recordOverOut.getIsEqual() ? "1" : "0");
        request.put("siteIsTutelage", recordOverOut.getIsCare() ? "1" : "0");
        request.put("siteGuardian", recordOverOut.getCarePerson());
        if (!recordOverOut.getIsReduce())
            request.put("sitePaymentMonty", recordOverOut.getTotalFee());
        else
            request.put("sitePaymentMonty", recordOverOut.getAfterFee());
        request.put("resultCode", recordOverOut.getResult());

        request.put("tollSignatureBase64", recordOverOut.getSignatureFeeMan());
        request.put("inspectoSignatureBase64", recordOverOut.getSignatureChecker());
        request.put("monitorSignatureBase64", recordOverOut.getSignatureMonitor());
        uploadOverHighCarOut(request, new OnSuccessAndFaultSub(this, "uploadOverHighCarOut", getApplicationContext(), false));
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "uploadStationRecorde"://站长值班记录
                MyRow row = (MyRow) result.obj;
                MyData request = new MyData();
                for (StationTeamEntity entity : recordStationOnduty.getTeamList()) {
                    MyRow ro = new MyRow();
                    ro.put("siteManagerId", row.getString("id"));
                    ro.put("monitorId", entity.getMonitorId());
                    ro.put("teamId", entity.getTeamId());
                    ro.put("groupType", entity.getGroupType());
                    ro.put("timeRange", entity.getTimeRange());
                    ro.put("number", entity.getNumber());
                    request.add(ro);
                }
                uploadStationTeams(request, new OnSuccessAndFaultSub(this, "uploadStationTeams", getApplicationContext(), false));
                break;
            case "uploadSpecialPhotos"://上传特情图片数据
                uploadSpecialData(result.obj.toString());
                break;
            case "uploadOverCarInPhotos"://上传治超入口查验图片数据
                uploadOverCarInData(result.obj.toString());
                break;
            case "uploadOverCarOutPhotos"://上传治超出口查验图片数据
                uploadOverCarOutData(result.obj.toString());
                break;
            case "uploadStationTeams"://站长值班记录班组信息
            case "uploadMonitorRecorde"://班长值班记录
            case "uploadSpecailForm"://上传特情数据
            case "uploadCivilizedService"://文明服务
            case "uploadOverHighCarIn"://上传治超入口查验数据
            case "uploadOverHighCarOut"://上传治超出口查验数据
            case "uploadSecurityCheck"://上传安全巡检数据
            case "uploadComplaintForm"://上传投诉管理数据
            case "uploadInspectionForm"://上传稽查管理数据
                removeTempListdata(method);
                break;
        }
    }

    @Override
    public void onFault(Result result, String method) {
        sendCast(false);
        if (result.msg.contains("Unauthorized")) {//登陆过期
            UI.showToast(getApplicationContext(), "登陆过期");
            BC.CAN_UPLAOD = false;
        } else {
            removeTempListdata(method);
            UI.showToast(getApplicationContext(), result.msg);
        }
    }

    /**
     * 删除本地数据库数据
     *
     * @param method
     */
    private void removeTempListdata(String method) {
        Long id;
        switch (method) {
            case "uploadStationTeams"://站长值班记录班组信息
                id = recordStationOnduty.getId();
                if (DaoUtils.getStationOndutyInstance().deleteById(id)) {
                    tmpStationOndutyRecorde.remove(tmpStationOndutyRecorde.size() - 1); //删除临时数据
                    UI.showToast(getApplicationContext(), "站长值班记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadMonitorRecorde"://班长值班记录
                id = recordMonitorOnduty.getId();
                if (DaoUtils.getMonitorOndutyInstance().deleteById(id)) {
                    tmpMonitorOndutyRecorde.remove(tmpMonitorOndutyRecorde.size() - 1);//删除临时数据
                    UI.showToast(getApplicationContext(), "班长值班记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadSpecailForm"://上传特情数据
                id = recordSpecial.getId();
                if (DaoUtils.getSpecialDoneInstance().deleteById(id)) {
                    tmpSpecialRecorde.remove(tmpSpecialRecorde.size() - 1);//删除临时数据
                    UI.showToast(getApplicationContext(), "特情处理记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadCivilizedService"://文明服务
                id = recordCivilized.getId();
                if (DaoUtils.getCivilizedServiceInstance().deleteById(id)) {
                    tmpCivilizedRecorde.remove(tmpCivilizedRecorde.size() - 1);//临时数据删除处理
                    UI.showToast(getApplicationContext(), "文明服务记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadOverHighCarIn"://上传治超入口查验数据
                id = recordOverIn.getId();
                if (DaoUtils.getOverHighInInstance().deleteById(id)) {
                    tmpOverCarinRecorde.remove(tmpOverCarinRecorde.size() - 1);//临时数据删除处理
                    UI.showToast(getApplicationContext(), "治超入口查验记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadOverHighCarOut"://上传治超出口查验数据
                id = recordOverOut.getId();
                if (DaoUtils.getOverHighOutInstance().deleteById(id)) {
                    tmpOverCaroutRecorde.remove(tmpOverCaroutRecorde.size() - 1);//临时数据删除处理
                    UI.showToast(getApplicationContext(), "治超出口查验记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadSecurityCheck"://上传安全巡检数据
                id = recordSecurityCheck.getId();
                if (DaoUtils.getSecurityCheckInstance().deleteById(id)) {
                    tmpSecurityCheckRecorde.remove(tmpSecurityCheckRecorde.size() - 1);//临时数据删除处理
                    UI.showToast(getApplicationContext(), "安全巡检记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadComplaintForm"://上传投诉管理数据
                id = recordComplaint.getId();
                if (DaoUtils.getComplaintFormInstance().deleteById(id)) {
                    tmpComplaintRecorde.remove(tmpComplaintRecorde.size() - 1); //临时数据删除处理
                    UI.showToast(getApplicationContext(), "投诉管理记录已提交成功！");
                    doNext();
                }
                break;
            case "uploadInspectionForm"://上传稽查管理数据
                id = recordInspection.getId();
                if (DaoUtils.getInspectionFormInstance().deleteById(id)) {
                    tmpInspectionRecorde.remove(tmpInspectionRecorde.size() - 1);//临时数据删除处理
                    UI.showToast(getApplicationContext(), "稽查管理记录已提交成功！");
                    doNext();
                }
                break;
        }

    }

    public void doNext() {
        sendCast(true);
        doUpload();
        if (hasDone8) {
            this.onDestroy();
        }
    }

    public void sendCast(boolean isUpload) {
        //发送广播
        Intent intent = new Intent();
        //设置intent的动作为com.example.broadcast，可以任意定义
        intent.putExtra("isStart", isUpload);
        intent.setAction("com.kunbo.xiwei.uploatdata");
        Log.d(TAG, "——————发送广播——————");
        //发送无序广播
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
