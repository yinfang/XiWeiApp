/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunbo.xiwei.activity.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.HistoryActivity;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.adapter.OndutyCarInRoadAdapter;
import com.kunbo.xiwei.adapter.OndutyCarOutRoadAdapter;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.MonitorOndutyRecorde;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.CarInOutEntity;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.util.image.ImageUtil;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.uploadMonitorRecorde;
import static com.zyf.xiweiapp.R.id.et_time_range;

/**
 * 班长排班表
 */
public class ClassOndutyActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.carin_list)
    RecyclerView carInList;
    @BindView(R.id.carout_list)
    RecyclerView carOutList;
    @BindView(R.id.img_exchange_signature)
    ImageView exchangeImg;
    @BindView(R.id.img_recieve_signature)
    ImageView recieveImg;
    @BindView(R.id.img_onduty_signature)
    ImageView ondutyImg;
    @BindView(R.id.et_weather)
    TextView tvWeather;
    private OndutyCarInRoadAdapter carInAdapter;
    private OndutyCarOutRoadAdapter carOutAdapter;
    @AutoRestore
    List<CarInOutEntity> carInDatas = new ArrayList<>();//入口车道数据
    @AutoRestore
    List<CarInOutEntity> carOutDatas = new ArrayList<>();//出口车道数据
    private List<String> feeMans = new ArrayList<>();//出入口车道收费员
    private List<String> feeManIds = new ArrayList<>();//出入口车道选择的收费员id
    private List<String> carInRoads = new ArrayList<>();//入口车道
    private List<String> carOutRoads = new ArrayList<>();//出口车道
    private List<String> carInRoadIds = new ArrayList<>();//入口车道id
    private List<String> carOutRoadIds = new ArrayList<>();//出口车道id
    private List<String> monitors = new ArrayList<>();//班长
    private List<String> monitorIds = new ArrayList<>();//班长id
    private List<String> agents = new ArrayList<>();//站长
    private List<String> agentIds = new ArrayList<>();//站长id
    boolean[] selected = new boolean[feeMans.size()]; //出入口车道收费员是否选中临时记录
    @AutoRestore
    boolean isCarInLane, isCarInFeeMan, isCarInTimeRange;//是否是入口车道选择收费员/时间段
    @AutoRestore
    int tempPos;
    @AutoRestore
    String outPeopleId, receiveMonitorId, ondutyAgentId;
    @AutoRestore
    int timeFullCount = 0;
    @AutoRestore
    int width;
    MyRow signatureRow = new MyRow();//签名数据
    String[] teams = {"白班", "夜班"};//班次
    private CustomDatePickerDialog mTimerPicker;
    MonitorOndutyRecorde monitorOndutyRecorde = new MonitorOndutyRecorde();
    private boolean isEditSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_onduty);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        show(R.id.refresh_ll);
        show(R.id.go_history);
        show(R.id.onduty_ll);
        initView();
    }

    private void initView() {
        if (!AppUtil.isTablet(this))
            width = UI.getScreenWidth(this) / 4;
        else
            width = UI.getScreenWidth(this) / 8;

        if (!TextUtils.isEmpty(User.getInstance().getSignature())) {
            signatureRow.put("signatureExchangeBt", ImageUtil.encodeUrlToBase64(User.getInstance().getSignature()));
            exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(exchangeImg, User.getInstance().getSignature());
        }
        //TODO 查询数据库，更新UI
        List<MonitorOndutyRecorde> recordes = DaoUtils.getMonitorOndutyInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                monitorOndutyRecorde = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        if (carInDatas.size() == 0) {
            carInDatas.add(new CarInOutEntity());
        }
        if (carOutDatas.size() == 0) {
            carOutDatas.add(new CarInOutEntity());
        }
        carInAdapter = new OndutyCarInRoadAdapter(this, carInDatas);
        carOutAdapter = new OndutyCarOutRoadAdapter(this, carOutDatas);
        carInList.setAdapter(carInAdapter);
        carOutList.setAdapter(carOutAdapter);
        carInList.setNestedScrollingEnabled(false);
        carOutList.setNestedScrollingEnabled(false);
        getMembersData();
        setEText(R.id.et_date, C.df_yMd.format(new Date()));
        setEText(R.id.et_weekday, Utils.getWeekOfDate(new Date()));
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.et_class, User.getInstance().getTeamName());
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
        }
        //getWeather(tvWeather);
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        setStep(this, new String[]{"基本信息", "工作情况", "接班记录", "站检查记录"}, true);
        initTimerPicker();
    }

    private void setData() {
        setEText(R.id.et_team, monitorOndutyRecorde.getTeam());
        setEText(R.id.et_class, monitorOndutyRecorde.getTeamClass());
        setEText(et_time_range, monitorOndutyRecorde.getTimeStamp());
        setEText(R.id.et_out_people, monitorOndutyRecorde.getOutPeople());
        setEText(R.id.et_total_fee, monitorOndutyRecorde.getTotalFee());
        setEText(R.id.et_back_card, monitorOndutyRecorde.getBackCardNum());
        setEText(R.id.et_output_card, monitorOndutyRecorde.getOutCardNum());

        if (monitorOndutyRecorde.getCarInList() != null) {
            carInDatas = monitorOndutyRecorde.getCarInList();
        }
        if (monitorOndutyRecorde.getCarOutList() != null) {
            carOutDatas = monitorOndutyRecorde.getCarOutList();
        }
        setEText(R.id.et_class_require, monitorOndutyRecorde.getBeforeOnduty());
        setEText(R.id.et_work_content, monitorOndutyRecorde.getDuringOnduty());
        setEText(R.id.et_class_sumup, monitorOndutyRecorde.getAfterOnduty());
        setEText(R.id.et_onduty_remark, monitorOndutyRecorde.getOndutyRemark());

        setEText(R.id.et_receive_people, monitorOndutyRecorde.getReceivePeople());
        setEText(R.id.et_money_exchange, monitorOndutyRecorde.getMoneyExchange());
        setEText(R.id.et_goods_exchange, monitorOndutyRecorde.getGoodsExchange());
        setEText(R.id.et_machine_run, monitorOndutyRecorde.getMachineRun());
        setEText(R.id.et_inout_dirty, monitorOndutyRecorde.getInOutDirty());
        setEText(R.id.et_service_goods, monitorOndutyRecorde.getServiceGoods());
        setEText(R.id.et_else, monitorOndutyRecorde.getElseExchange());
        setEText(R.id.et_receive_remark, monitorOndutyRecorde.getReceiveRemark());

        setEText(R.id.et_standard_on, monitorOndutyRecorde.getStandardOn());
        setEText(R.id.et_standard_exchange, monitorOndutyRecorde.getStandardExchange());
        setEText(R.id.et_work_record, monitorOndutyRecorde.getWorkRecord());
        setEText(R.id.et_num_same, monitorOndutyRecorde.getNumSame());
        setEText(R.id.et_inandout_dirty, monitorOndutyRecorde.getInAndOutDirty());
        setEText(R.id.et_check_remark, monitorOndutyRecorde.getCheckRemark());
        setEText(R.id.et_onduty_agent, monitorOndutyRecorde.getOndutyAgentName());

        if (!Utils.checkNullString(monitorOndutyRecorde.getExchangeSignature())) {
            signatureRow.put("signatureExchangeBt", monitorOndutyRecorde.getExchangeSignature());
            exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(exchangeImg, ImageUtil.base64ToBitmap(monitorOndutyRecorde.getExchangeSignature()));
        }
        if (!Utils.checkNullString(monitorOndutyRecorde.getRecieveSignature())) {
            signatureRow.put("signatureRecieveBt", monitorOndutyRecorde.getRecieveSignature());
            recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(recieveImg, ImageUtil.base64ToBitmap(monitorOndutyRecorde.getRecieveSignature()));
        }
        if (!Utils.checkNullString(monitorOndutyRecorde.getOndutySignature())) {
            signatureRow.put("signatureOndutyBt", monitorOndutyRecorde.getOndutySignature());
            ondutyImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(ondutyImg, ImageUtil.base64ToBitmap(monitorOndutyRecorde.getOndutySignature()));
        }
        outPeopleId = monitorOndutyRecorde.getOutPeopleId();
        receiveMonitorId = monitorOndutyRecorde.getReceivePeopleId();
        ondutyAgentId = monitorOndutyRecorde.getOndutyAgentId();
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker() {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            if (Utils.checkNullString(getEText(v.getId())))
                timeFullCount = 0;
            timeFullCount++;
            switch (v.getId()) {
                case R.id.et_time_range:
                    if (timeFullCount < 2) {
                        setEText(v.getId(), C.df_yMdH.format(new Date(timestamp)));
                        mTimerPicker.show(v, "请选择结束时间", C.df_yMdH.format(new Date()));
                    } else {
                        String startT = getEText(v.getId());
                        setEText(v.getId(), startT + " - " + C.df_yMdH.format(new Date(timestamp)));
                        timeFullCount = 0;
                        mTimerPicker.dismissDialog();
                    }
                    break;
            }
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时分
        mTimerPicker.setCanShowDateTime(true, true, false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    @OnClick({R.id.refresh_ll, R.id.onduty_ll, R.id.et_weather, R.id.et_team, R.id.et_time_range, R.id.et_out_people, R.id.et_receive_people,
            R.id.et_onduty_agent, R.id.et_standard_on, R.id.et_standard_exchange, R.id.et_work_record, R.id.et_num_same, R.id.et_inandout_dirty, R.id.go_history,
            R.id.img_exchange_signature, R.id.img_recieve_signature, R.id.img_onduty_signature, R.id.save, R.id.submit})
    public void onClick(View view) {
        super.onClick(view);
        Intent i = new Intent(this, SignatureActivity.class);
        switch (view.getId()) {
            case R.id.refresh_ll://刷新数据
                notifyDataChanged();
                break;
            case R.id.onduty_ll://值班表
                Intent in = new Intent(this, OndutyFormActivity.class);
                startActivity(in);
                break;
            case R.id.et_out_people:
                showMyListDialog(view, feeMans.toArray(new String[0]));
                break;
            case R.id.et_team:
                showListDialog(view, teams);
                break;
            case R.id.et_time_range:
                mTimerPicker.show(view, "请选择开始时间", C.df_yMdHm.format(new Date()));
                break;
            case R.id.et_receive_people://接班人
                showListDialog(view, monitors.toArray(new String[0]));
                break;
            case R.id.et_onduty_agent://值班站长
                showListDialog(view, agents.toArray(new String[0]));
                break;
            case R.id.et_standard_on://规范上岗情况
            case R.id.et_standard_exchange://交接班程序规范
            case R.id.et_work_record://文明服务、工作记录
            case R.id.et_num_same://票卡数一致
            case R.id.et_inandout_dirty://亭内及广场卫生
                showListDialog(view, getResources().getStringArray(R.array.prority_list));
                break;
            case R.id.go_history://历史记录
                Bundle extras = new Bundle();
                extras.putString("from", "classOnduty");
                openIntent(HistoryActivity.class, "班长值班历史记录", extras);
                break;
            case R.id.et_weather://天气
                showListDialog(view, getResources().getStringArray(R.array.weather_list));
                break;
            case R.id.img_exchange_signature:
                startActivityForResult(i, 177);
                break;
            case R.id.img_recieve_signature:
                startActivityForResult(i, 178);
                break;
            case R.id.img_onduty_signature:
                startActivityForResult(i, 179);
                break;
            case R.id.save://保存
                saveAndSubmitData(false);
                break;
            case R.id.submit://提交
                saveAndSubmitData(true);
                break;
        }
    }

    private void saveAndSubmitData(boolean submit) {
        savaData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getMonitorOndutyInstance().updateData(monitorOndutyRecorde);
            } else {
                isEditSave = true;
                DaoUtils.getMonitorOndutyInstance().insertData(monitorOndutyRecorde);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                monitorOndutyRecorde.setIsSubmit(true);
                monitorOndutyRecorde.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getMonitorOndutyInstance().updateData(monitorOndutyRecorde);
                            else
                                DaoUtils.getMonitorOndutyInstance().insertData(monitorOndutyRecorde);

                            if (!AppUtil.isNetworkConnected(this)) {
                                monitorOndutyRecorde = new MonitorOndutyRecorde();
                                clearEdit();
                                isEditSave = false;
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                MyRow request = new MyRow();
                                request.put("teamId", monitorOndutyRecorde.getTeamId());
                                request.put("personId", monitorOndutyRecorde.getPersonId());
                                request.put("stationId", monitorOndutyRecorde.getStaionId());
                                request.put("timeStamp", monitorOndutyRecorde.getTimeStamp());
                                request.put("dutyDate", monitorOndutyRecorde.getDate());
                                request.put("weekDay", monitorOndutyRecorde.getWeekday());
                                request.put("weather", monitorOndutyRecorde.getWeather());
                                request.put("visitNo", "000");
                                request.put("shiftType", monitorOndutyRecorde.getTeam());//班次
                                request.put("draftedBy", monitorOndutyRecorde.getTotalFee());//收费额
                                request.put("cardRecycleNum", monitorOndutyRecorde.getBackCardNum());
                                request.put("cardCreditNum", monitorOndutyRecorde.getOutCardNum());
                                request.put("outworkerId", monitorOndutyRecorde.getOutPeopleId());

                                request.put("beforeOnduty", monitorOndutyRecorde.getBeforeOnduty());
                                request.put("duringOnduty", monitorOndutyRecorde.getDuringOnduty());
                                request.put("afterOnduty", monitorOndutyRecorde.getAfterOnduty());
                                request.put("remark", monitorOndutyRecorde.getOndutyRemark());
                                request.put("shiftExchangeSignatureBase64", monitorOndutyRecorde.getExchangeSignature());

                                MyData laneData = new MyData();
                                for (CarInOutEntity entity : monitorOndutyRecorde.getCarInList()) {
                                    MyRow laneRow = new MyRow();
                                    laneRow.put("laneId", entity.getLaneId());
                                    //laneRow.put("timeRange", entity.getTimeRange());
                                    laneRow.put("staffList", entity.getFeeMans());
                                    laneData.add(laneRow);
                                }
                                for (CarInOutEntity entity : monitorOndutyRecorde.getCarOutList()) {
                                    MyRow laneRow = new MyRow();
                                    laneRow.put("laneId", entity.getLaneId());
                                    //laneRow.put("timeRange", entity.getTimeRange());
                                    laneRow.put("staffList", entity.getFeeMans());
                                    laneData.add(laneRow);
                                }
                                request.put("assemble", laneData);

                                request.put("succeedMonitorId", monitorOndutyRecorde.getReceivePeopleId());
                                request.put("dutyStationId", monitorOndutyRecorde.getOndutyAgentId());
                                MyRow receiveRow = new MyRow();
                                receiveRow.put("succeedImprest", monitorOndutyRecorde.getMoneyExchange());
                                receiveRow.put("succeedLaneItems", monitorOndutyRecorde.getGoodsExchange());
                                receiveRow.put("succeedLaneEquipment", monitorOndutyRecorde.getMachineRun());
                                receiveRow.put("succeedSanitation", monitorOndutyRecorde.getInOutDirty());
                                receiveRow.put("succeedServiceProduct", monitorOndutyRecorde.getServiceGoods());
                                receiveRow.put("succeedElse", monitorOndutyRecorde.getElseExchange());
                                receiveRow.put("remark", monitorOndutyRecorde.getReceiveRemark());
                                receiveRow.put("succeedSignatureBase64", monitorOndutyRecorde.getRecieveSignature());
                                request.put("succession", receiveRow);

                                MyRow ondutyRow = new MyRow();
                                ondutyRow.put("standardMountGuard", monitorOndutyRecorde.getStandardOn());
                                ondutyRow.put("standardShiftProcedure", monitorOndutyRecorde.getStandardExchange());
                                ondutyRow.put("civilServiceWork", monitorOndutyRecorde.getWorkRecord());
                                ondutyRow.put("isRidesWithFit", monitorOndutyRecorde.getNumSame());
                                ondutyRow.put("kioskAndSquareHealth", monitorOndutyRecorde.getInAndOutDirty());
                                ondutyRow.put("remark", monitorOndutyRecorde.getCheckRemark());
                                ondutyRow.put("siteOfficeBase64", monitorOndutyRecorde.getOndutySignature());
                                request.put("siteExamine", ondutyRow);

                                uploadMonitorRecorde(request, new OnSuccessAndFaultSub(this, "uploadMonitorRecorde", "正在提交数据...", this));
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    private boolean checkNull() {
        if (Utils.checkNullString(monitorOndutyRecorde.getTimeStamp()) ||
                Utils.checkNullString(monitorOndutyRecorde.getWeather()) ||
                Utils.checkNullString(monitorOndutyRecorde.getTeam()) ||
                Utils.checkNullString(monitorOndutyRecorde.getTeamClass()) ||
                Utils.checkNullString(monitorOndutyRecorde.getOutPeople()) ||
                Utils.checkNullString(monitorOndutyRecorde.getTotalFee()) ||
                Utils.checkNullString(monitorOndutyRecorde.getBackCardNum()) ||
                Utils.checkNullString(monitorOndutyRecorde.getOutCardNum()) ||
                Utils.checkNullString(monitorOndutyRecorde.getBeforeOnduty()) ||
                Utils.checkNullString(monitorOndutyRecorde.getDuringOnduty()) ||
                Utils.checkNullString(monitorOndutyRecorde.getAfterOnduty())) {
            UI.showToast(this, "请完善当班记录信息！");
            return true;
        }

        if (carInDatas != null && carInDatas.size() > 0) {
            for (int i = carInDatas.size() - 1; i >= 0; i--) {
                CarInOutEntity entity = carInDatas.get(i);
                if (Utils.checkNullString(entity.getLaneId()) &&
                        Utils.checkNullString(entity.getLane()) &&
                        Utils.checkNullString(entity.getFeeMans()) &&
                        Utils.checkNullString(entity.getFeeManIds())) {
                    //Utils.checkNullString(entity.getTimeRange())) {
                    carInDatas.remove(i);
                }
            }
            carInAdapter.notifyDataSetChanged();
        }
        monitorOndutyRecorde.setCarInList(carInDatas);
        if (carInDatas != null && carInDatas.size() > 0) {
            for (int i = 0; i < carInDatas.size(); i++) {
                CarInOutEntity entity = carInDatas.get(i);
                if (Utils.checkNullString(entity.getLaneId()) ||
                        Utils.checkNullString(entity.getLane()) ||
                        Utils.checkNullString(entity.getFeeMans()) ||
                        Utils.checkNullString(entity.getFeeManIds())) {
                    //Utils.checkNullString(entity.getTimeRange())) {
                    UI.showToast(this, "请填写完整的入口车道信息！");
                    return true;
                }
            }
        }
        if (carInDatas == null || carInDatas.size() == 0) {
            UI.showToast(this, "请填写入口车道信息！");
            return true;
        }
        if (carOutDatas != null && carOutDatas.size() > 0) {
            for (int i = carOutDatas.size() - 1; i >= 0; i--) {
                CarInOutEntity entity = carOutDatas.get(i);
                if (Utils.checkNullString(entity.getLaneId()) &&
                        Utils.checkNullString(entity.getLane()) &&
                        Utils.checkNullString(entity.getFeeMans()) &&
                        Utils.checkNullString(entity.getFeeManIds())) {
                    //Utils.checkNullString(entity.getTimeRange())) {
                    carOutDatas.remove(i);
                }
            }
            carOutAdapter.notifyDataSetChanged();
        }
        monitorOndutyRecorde.setCarOutList(carOutDatas);
        if (carOutDatas != null && carOutDatas.size() > 0) {
            for (int i = 0; i < carOutDatas.size(); i++) {
                CarInOutEntity entity = carOutDatas.get(i);
                if (Utils.checkNullString(entity.getLaneId()) ||
                        Utils.checkNullString(entity.getLane()) ||
                        Utils.checkNullString(entity.getFeeMans()) ||
                        Utils.checkNullString(entity.getFeeManIds())) {
                    //Utils.checkNullString(entity.getTimeRange())) {
                    UI.showToast(this, "请填写完整的出口车道信息！");
                    return true;
                }
            }
        }
        if (carOutDatas == null || carOutDatas.size() == 0) {
            UI.showToast(this, "请填写出口车道信息！");
            return true;
        }

        if (Utils.checkNullString(monitorOndutyRecorde.getReceivePeople()) ||
                Utils.checkNullString(monitorOndutyRecorde.getMoneyExchange()) ||
                Utils.checkNullString(monitorOndutyRecorde.getGoodsExchange()) ||
                Utils.checkNullString(monitorOndutyRecorde.getMachineRun()) ||
                Utils.checkNullString(monitorOndutyRecorde.getInOutDirty()) ||
                Utils.checkNullString(monitorOndutyRecorde.getServiceGoods())) {
            UI.showToast(this, "请完善接班记录信息！");
            return true;
        }

        if (Utils.checkNullString(monitorOndutyRecorde.getExchangeSignature())) {
            UI.showToast(this, "交班班长未签名！");
            return true;
        }
        if (Utils.checkNullString(monitorOndutyRecorde.getRecieveSignature())) {
            UI.showToast(this, "接班班长未签名！");
            return true;
        }
        if (Utils.checkNullString(monitorOndutyRecorde.getOndutySignature())) {
            UI.showToast(this, "值班站长未签名！");
            return true;
        }
        return false;
    }

    private void savaData() {
        if (isEditSave)
            monitorOndutyRecorde.setId(monitorOndutyRecorde.getId());
        else
            monitorOndutyRecorde.setId(null);
        monitorOndutyRecorde.setIsSubmit(false);
        monitorOndutyRecorde.setIsComplete(false);
        monitorOndutyRecorde.setPersonId(User.getInstance().getPersonId());
        monitorOndutyRecorde.setStaionId(User.getInstance().getStationId());
        monitorOndutyRecorde.setDate(getEText(R.id.et_date));
        monitorOndutyRecorde.setWeekday(getEText(R.id.et_weekday));
        monitorOndutyRecorde.setWeather(getEText(R.id.et_weather));

        monitorOndutyRecorde.setTeam(getEText(R.id.et_team));
        monitorOndutyRecorde.setTeamId(User.getInstance().getTeamId());
        monitorOndutyRecorde.setTeamClass(getEText(R.id.et_class));
        monitorOndutyRecorde.setTimeStamp(getEText(et_time_range));
        monitorOndutyRecorde.setTotalFee(getEText(R.id.et_total_fee));
        monitorOndutyRecorde.setBackCardNum(getEText(R.id.et_back_card));
        monitorOndutyRecorde.setOutCardNum(getEText(R.id.et_output_card));
        monitorOndutyRecorde.setOutPeople(getEText(R.id.et_out_people));
        monitorOndutyRecorde.setOutPeopleId(outPeopleId);

        monitorOndutyRecorde.setCarInList(carInDatas);
        monitorOndutyRecorde.setCarOutList(carOutDatas);

        monitorOndutyRecorde.setBeforeOnduty(getEText(R.id.et_class_require));
        monitorOndutyRecorde.setDuringOnduty(getEText(R.id.et_work_content));
        monitorOndutyRecorde.setAfterOnduty(getEText(R.id.et_class_sumup));
        monitorOndutyRecorde.setOndutyRemark(getEText(R.id.et_onduty_remark));

        monitorOndutyRecorde.setReceivePeople(getEText(R.id.et_receive_people));
        monitorOndutyRecorde.setReceivePeopleId(receiveMonitorId);
        monitorOndutyRecorde.setMoneyExchange(getEText(R.id.et_money_exchange));
        monitorOndutyRecorde.setGoodsExchange(getEText(R.id.et_goods_exchange));
        monitorOndutyRecorde.setMachineRun(getEText(R.id.et_machine_run));
        monitorOndutyRecorde.setInOutDirty(getEText(R.id.et_inout_dirty));
        monitorOndutyRecorde.setServiceGoods(getEText(R.id.et_service_goods));
        monitorOndutyRecorde.setElseExchange(getEText(R.id.et_else));
        monitorOndutyRecorde.setReceiveRemark(getEText(R.id.et_receive_remark));

        monitorOndutyRecorde.setStandardOn(getEText(R.id.et_standard_on));
        monitorOndutyRecorde.setStandardExchange(getEText(R.id.et_standard_exchange));
        monitorOndutyRecorde.setWorkRecord(getEText(R.id.et_work_record));
        monitorOndutyRecorde.setNumSame(getEText(R.id.et_num_same));
        monitorOndutyRecorde.setInAndOutDirty(getEText(R.id.et_inandout_dirty));
        monitorOndutyRecorde.setCheckRemark(getEText(R.id.et_check_remark));
        monitorOndutyRecorde.setOndutyAgentName(getEText(R.id.et_onduty_agent));
        monitorOndutyRecorde.setOndutyAgentId(ondutyAgentId);

        monitorOndutyRecorde.setExchangeSignature(signatureRow.getString("signatureExchangeBt"));
        monitorOndutyRecorde.setRecieveSignature(signatureRow.getString("signatureRecieveBt"));
        monitorOndutyRecorde.setOndutySignature(signatureRow.getString("signatureOndutyBt"));
    }

    @Override
    public void onSuccess(Result result, String method) {
        //数据提交后清空数据库
        DaoUtils.getMonitorOndutyInstance().deleteById(monitorOndutyRecorde.getId());
        UI.showToast(this, "班长值班记录已提交成功！");
        isEditSave = false;
        clearEdit();
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    private void clearEdit() {
        setEText(R.id.et_team, "");
        setEText(et_time_range, "");
        setEText(R.id.et_out_people, "");
        setEText(R.id.et_total_fee, "");
        setEText(R.id.et_back_card, "");
        setEText(R.id.et_output_card, "");

        setEText(R.id.et_class_require, "");
        setEText(R.id.et_work_content, "");
        setEText(R.id.et_class_sumup, "");
        setEText(R.id.et_onduty_remark, "");

        setEText(R.id.et_receive_people, "");
        setEText(R.id.et_money_exchange, "");
        setEText(R.id.et_goods_exchange, "");
        setEText(R.id.et_machine_run, "");
        setEText(R.id.et_inout_dirty, "");
        setEText(R.id.et_service_goods, "");
        setEText(R.id.et_else, "");
        setEText(R.id.et_receive_remark, "");

        setEText(R.id.et_standard_on, getResources().getStringArray(R.array.prority_list)[0]);
        setEText(R.id.et_standard_exchange, getResources().getStringArray(R.array.prority_list)[0]);
        setEText(R.id.et_work_record, getResources().getStringArray(R.array.prority_list)[0]);
        setEText(R.id.et_num_same, getResources().getStringArray(R.array.prority_list)[0]);
        setEText(R.id.et_inandout_dirty, getResources().getStringArray(R.array.prority_list)[0]);
        setEText(R.id.et_check_remark, "");
        setEText(R.id.et_onduty_agent, "");

        setImage(exchangeImg, R.drawable.selector_solid_gray);
        setImage(recieveImg, R.drawable.selector_solid_gray);
        setImage(ondutyImg, R.drawable.selector_solid_gray);

        signatureRow.clear();
        carInDatas.clear();
        carInDatas.add(new CarInOutEntity());
        carInAdapter.notifyDataSetChanged();

        carOutDatas.clear();
        carOutDatas.add(new CarInOutEntity());
        carOutAdapter.notifyDataSetChanged();
    }

    /**
     * 出入口车道 选择车道
     */
    public void selectLane(View view, int position, Boolean isCarIn) {
        isCarInLane = isCarIn;
        tempPos = position;
        if (isCarInLane) {
            showListDialog(view, carInRoads.toArray(new String[0]));
        } else {
            showListDialog(view, carOutRoads.toArray(new String[0]));
        }
    }

    /**
     * 出入口车道 选择收费员
     */
    public void selectFeeMans(ViewHolder holder, String names, int position, Boolean isCarIn) {
        isCarInFeeMan = isCarIn;
        tempPos = position;
        showMListDialog(holder.itemView, feeMans.toArray(new String[0]), TextUtils.isEmpty(names) ? new String[]{} : names.split(","));
    }

    /**
     * 添加入口车道
     */
    public void addCarInLl() {
        carInDatas.add(new CarInOutEntity());
        carInAdapter.notifyDataSetChanged();
    }

    /**
     * 添加出口车道
     */
    public void addCarOutLl() {
        carOutDatas.add(new CarInOutEntity());
        carOutAdapter.notifyDataSetChanged();
    }

    /**
     * 删除入口车道
     */
    public void removeCarInLl(int position) {
        carInDatas.remove(position);
        carInAdapter.notifyItemRemoved(position);
        carInAdapter.notifyItemRangeChanged(position, carInDatas.size() - position);
    }

    /**
     * 删除出口车道
     */
    public void removeCarOutLl(int position) {
        carOutDatas.remove(position);
        carOutAdapter.notifyItemRemoved(position);
        carOutAdapter.notifyItemRangeChanged(position, carOutDatas.size() - position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("path");
            switch (requestCode) {
                case 177:
                    signatureRow.put("signatureExchangeBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(exchangeImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 178:
                    signatureRow.put("signatureRecieveBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(recieveImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 179:
                    signatureRow.put("signatureOndutyBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    ondutyImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(ondutyImg, ImageUtil.getBitmapFormPath(path));
                    break;
            }
        }
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.et_weather://天气
                setEText(view.getId(), getResources().getStringArray(R.array.weather_list)[index]);
                setImage(R.id.iv_weather, UI.getId(this, "ic_weather" + index, "mipmap"));
                break;
            case R.id.et_out_people:
                setEText(view.getId(), feeMans.get(index));
                outPeopleId = feeManIds.get(index);
                break;
            case R.id.et_team:
                setEText(view.getId(), teams[index]);
                break;
            case R.id.et_receive_people:
                setEText(view.getId(), monitors.get(index));
                receiveMonitorId = monitorIds.get(index);
                break;
            case R.id.et_onduty_agent:
                setEText(view.getId(), agents.get(index));
                ondutyAgentId = agentIds.get(index);
                break;
            case R.id.et_num:
                if (isCarInLane) {//入口车道选择
                    carInDatas.get(tempPos).setLane(carInRoads.get(index));
                    carInDatas.get(tempPos).setLaneId(carInRoadIds.get(index));
                    carInAdapter.notifyDataSetChanged();
                } else {
                    carOutDatas.get(tempPos).setLane(carOutRoads.get(index));
                    carOutDatas.get(tempPos).setLaneId(carOutRoadIds.get(index));
                    carOutAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.et_standard_on://规范上岗情况
            case R.id.et_standard_exchange://交接班程序规范
            case R.id.et_work_record://文明服务、工作记录
            case R.id.et_num_same://票卡数一致
            case R.id.et_inandout_dirty://亭内及广场卫生
                setEText(view.getId(), getResources().getStringArray(R.array.prority_list)[index]);
                break;
        }
    }

    @Override
    public void mlistSelected(View view, boolean[] selecte) {
        selected = selecte;
        StringBuffer sb = new StringBuffer();
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < feeMans.size(); i++) {
            if (selecte[i]) {
                sb.append(feeMans.get(i)).append(",");
                sb1.append(feeManIds.get(i)).append(",");
            }
        }
        if (sb.length() != 0 & sb1.length() != 0) {
            String names = sb.substring(0, sb.length() - 1);
            String ids = sb1.substring(0, sb1.length() - 1);
            if (isCarInFeeMan) {//入口车道收费员选择
                carInDatas.get(tempPos).setFeeMans(names);
                carInDatas.get(tempPos).setFeeManIds(ids);
                carInAdapter.notifyDataSetChanged();
            } else {
                carOutDatas.get(tempPos).setFeeMans(names);
                carOutDatas.get(tempPos).setFeeManIds(ids);
                carOutAdapter.notifyDataSetChanged();
            }
        }
    }

    public void getMembersData() {
        List<StationEmployee> list = DaoUtils.getStationEmployeeInstance().loadAllData();//站所有人员
        feeMans.clear();
        feeManIds.clear();
        if (list != null && list.size() > 0) {
            for (StationEmployee ro : list) {
                feeMans.add(ro.getName());
                feeManIds.add(ro.getId().toString());
            }
            selected = new boolean[feeMans.size()];
        }
        List<StationEmployee> list1 = DaoUtils.getStationEmployeeInstance().loadTypeData(BC.MONITOR_KEY, "");
        monitors.clear();
        monitorIds.clear();
        if (list1 != null && list1.size() > 0) {
            for (StationEmployee ro : list1) {
                monitors.add(ro.getName());
                monitorIds.add(ro.getId().toString());
            }
        }
        List<StationEmployee> list2 = DaoUtils.getStationEmployeeInstance().loadTypeData(BC.AGENT_KEY, BC.AGENT_SUB_KEY);//站长
        agents.clear();
        agentIds.clear();
        if (list1 != null && list2.size() > 0) {
            for (StationEmployee ro : list2) {
                agents.add(ro.getName());
                agentIds.add(ro.getId().toString());
            }
        }
        List<LaneTypes> inLanes = DaoUtils.getLaneTypesInstance().loadTypeData(BC.LANE_IN);
        carInRoads.clear();
        carInRoadIds.clear();
        if (inLanes != null && inLanes.size() > 0) {
            for (LaneTypes ro : inLanes) {
                carInRoads.add(ro.getName());
                carInRoadIds.add(ro.getId().toString());
            }
        }
        List<LaneTypes> outLanes = DaoUtils.getLaneTypesInstance().loadTypeData(BC.LANE_OUT);
        carOutRoads.clear();
        carOutRoadIds.clear();
        if (outLanes != null && outLanes.size() > 0) {
            for (LaneTypes ro : outLanes) {
                carOutRoads.add(ro.getName());
                carOutRoadIds.add(ro.getId().toString());
            }
        }
    }

    public void notifyDataChanged() {
        SyncAndSettingUtils dialog = new SyncAndSettingUtils(this);
        dialog.sycnData(this, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!BC.HAS_DONE) {
                    if (Thread.activeCount() == 1)//其他线程执行完毕
                        break;
                }
                getMembersData();
            }
        }).start();
    }
}
