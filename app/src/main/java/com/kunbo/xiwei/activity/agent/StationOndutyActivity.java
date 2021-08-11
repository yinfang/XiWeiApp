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

package com.kunbo.xiwei.activity.agent;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.HistoryActivity;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.activity.monitor.OndutyFormActivity;
import com.kunbo.xiwei.adapter.StationOnTeamAdapter;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.entity.StationOndutyRecorde;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.StationTeamEntity;
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

import static com.zyf.net.api.ApiSubscribe.uploadStationRecorde;
import static com.zyf.net.api.ApiSubscribe.uploadStationTeams;

/**
 * 站长值班
 */
public class StationOndutyActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    private StationOnTeamAdapter adapter;
    @BindView(R.id.team_list)
    RecyclerView teamList;
    @BindView(R.id.img_exchange_signature)
    ImageView exchangeImg;
    @BindView(R.id.img_recieve_signature)
    ImageView recieveImg;
    @BindView(R.id.et_weather)
    TextView tvWeather;
    @BindView(R.id.et_in_num)
    EditText inNum;
    @BindView(R.id.et_out_num)
    EditText outNum;
    @AutoRestore
    int timeFullCount = 0;
    @AutoRestore
    int width;
    @AutoRestore
    String ticketId = "";
    @AutoRestore
    int tempPos;//班次点击的item位置
    String[] teams = {"白班", "夜班"};//班次
    String[] teamIds = {"1", "2"};//班次
    MyRow signatureRow = new MyRow();//签名数据
    //MyData ondutyData = new MyData();//当班期间data
    List<StationTeamEntity> teamDatas = new ArrayList<>();//班次list
    private List<String> monitors = new ArrayList<>();//班长
    private List<String> monitorIds = new ArrayList<>();//班长id
    private List<String> tickets = new ArrayList<>();//票证
    private List<String> ticketIds = new ArrayList<>();//票证id
    private CustomDatePickerDialog mTimerPicker;
    StationOndutyRecorde stationOndutyRecorde = new StationOndutyRecorde();
    private boolean isEditSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_onduty);
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
        List<StationOndutyRecorde> recordes = DaoUtils.getStationOndutyInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                stationOndutyRecorde = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        if (teamDatas.size() == 0) {
            teamDatas.add(new StationTeamEntity());
            teamDatas.add(new StationTeamEntity());
        }
        adapter = new StationOnTeamAdapter(this, teamDatas);
        teamList.setAdapter(adapter);
        teamList.setNestedScrollingEnabled(false);

        setStep(this, new String[]{"基本信息", "工作情况", "待办事项"}, true);
        setEText(R.id.et_date, C.df_yMd.format(new Date()));
        setEText(R.id.et_weekday, Utils.getWeekOfDate(new Date()));
        //getWeather(tvWeather);
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
            setEText(R.id.et_station, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
        }
        setEditLisetner();
        getMembersData();
    }

    /**
     * 根据出入口客流量计算总车流量
     */
    private void setEditLisetner() {
        inNum.addTextChangedListener(watcher);
        outNum.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            long in = Utils.checkNullString(inNum.getText().toString()) ? 0 : Long.parseLong(inNum.getText().toString());
            long out = Utils.checkNullString(outNum.getText().toString()) ? 0 : Long.parseLong(outNum.getText().toString());
            setEText(R.id.et_car_num, String.valueOf(in + out));
        }
    };

    private void setData() {
        setEText(R.id.et_ticket, stationOndutyRecorde.getTicketName());
        if (stationOndutyRecorde.getTeamList() != null) {
            teamDatas = stationOndutyRecorde.getTeamList();
        }
        setEText(R.id.et_time_range, stationOndutyRecorde.getTimeStamp());
        setEText(R.id.et_totle_fee, stationOndutyRecorde.getTotalFee());
        setEText(R.id.et_check_num, stationOndutyRecorde.getCheckNum());
        setEText(R.id.et_return_num, stationOndutyRecorde.getReturnNum());
        setEText(R.id.et_car_num, stationOndutyRecorde.getCarNum());
        setEText(R.id.et_notify_check, stationOndutyRecorde.getNotifyCheck());
        setEText(R.id.et_implement_state, stationOndutyRecorde.getImplementState());
        setEText(R.id.et_free_money, stationOndutyRecorde.getExemptionAmount());
        setEText(R.id.et_in_num, stationOndutyRecorde.getInFlow());
        setEText(R.id.et_out_num, stationOndutyRecorde.getOutFlow());

        setEText(R.id.et_class_require, stationOndutyRecorde.getBeforeOnduty());
        setEText(R.id.et_during_onduty, stationOndutyRecorde.getDuringOnduty());
        setEText(R.id.et_class_sumup, stationOndutyRecorde.getAfterOnduty());

        setEText(R.id.et_todo, stationOndutyRecorde.getTodo());
        setEText(R.id.et_station_remark, stationOndutyRecorde.getRemark());

        String exchangeSignature = stationOndutyRecorde.getExchangeSignature();
        if (!Utils.checkNullString(exchangeSignature)) {
            signatureRow.put("signatureExchangeBt", exchangeSignature);
            exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(exchangeImg, ImageUtil.base64ToBitmap(exchangeSignature));
        }
        String recieveSignature = stationOndutyRecorde.getRecieveSignature();
        if (!Utils.checkNullString(recieveSignature)) {
            signatureRow.put("signatureRecieveBt", recieveSignature);
            recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(recieveImg, ImageUtil.base64ToBitmap(recieveSignature));
        }
        ticketId = stationOndutyRecorde.getTicketId();
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showHour) {
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
                        String start = getEText(v.getId());
                        setEText(v.getId(), start + " - " + C.df_yMdH.format(new Date(timestamp)));
                        mTimerPicker.dismissDialog();
                        timeFullCount = 0;
                    }
                    break;
                case R.id.tv_range:
                    if (timeFullCount < 2) {
                        teamDatas.get(tempPos).setTimeRange(C.df_yMdH.format(new Date(timestamp)) + " - ");
                        mTimerPicker.show(v, "请选择结束时间", C.df_yMdH.format(new Date()));
                    } else {
                        String startT = teamDatas.get(tempPos).getTimeRange();
                        teamDatas.get(tempPos).setTimeRange(startT + C.df_yMdH.format(new Date(timestamp)));
                        mTimerPicker.dismissDialog();
                        timeFullCount = 0;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时
        mTimerPicker.setCanShowDateTime(true, showHour, false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    @OnClick({R.id.refresh_ll, R.id.onduty_ll, R.id.go_history, R.id.et_weather, R.id.et_time_range, R.id.et_ticket, R.id.img_exchange_signature,
            R.id.img_recieve_signature, R.id.save, R.id.submit})
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
            case R.id.go_history://历史记录
                Bundle extras = new Bundle();
                extras.putString("from", "stationOnduty");
                openIntent(HistoryActivity.class, "站长值班历史记录", extras);
                break;
            case R.id.et_weather://天气
                showListDialog(view, getResources().getStringArray(R.array.weather_list));
                break;
            case R.id.et_time_range://站长值班时间
                initTimerPicker(true);
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(view, "请选择开始时间", C.df_yMdH.format(new Date()));
                break;
            case R.id.et_ticket://值班票证
                if (tickets != null && tickets.size() > 0)
                    showListDialog(view, tickets.toArray(new String[0]));
                break;
            case R.id.img_exchange_signature:
                startActivityForResult(i, 125);
                break;
            case R.id.img_recieve_signature:
                startActivityForResult(i, 126);
                break;
            case R.id.save://保存
                saveAndSubmitData(false);
                break;
            case R.id.submit://提交
                saveAndSubmitData(true);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("path");
            switch (requestCode) {
                case 125:
                    signatureRow.put("signatureExchangeBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(exchangeImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 126:
                    signatureRow.put("signatureRecieveBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(recieveImg, ImageUtil.getBitmapFormPath(path));
                    break;
            }
        }
    }

    private void saveAndSubmitData(boolean submit) {
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getStationOndutyInstance().updateData(stationOndutyRecorde);
            } else {
                isEditSave = true;
                DaoUtils.getStationOndutyInstance().insertData(stationOndutyRecorde);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                stationOndutyRecorde.setIsSubmit(true);
                stationOndutyRecorde.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getStationOndutyInstance().updateData(stationOndutyRecorde);
                            else
                                DaoUtils.getStationOndutyInstance().insertData(stationOndutyRecorde);

                            if (!AppUtil.isNetworkConnected(this)) {
                                StationOndutyRecorde recorde = new StationOndutyRecorde(null, stationOndutyRecorde.getUserId(), stationOndutyRecorde.getPersonId(),
                                        stationOndutyRecorde.getStaionId(), stationOndutyRecorde.getTimeStamp(), stationOndutyRecorde.getStationName(), stationOndutyRecorde.getTicketName(),
                                        stationOndutyRecorde.getTicketId(), stationOndutyRecorde.getDate(), stationOndutyRecorde.getWeekday(), stationOndutyRecorde.getWeather(),
                                        stationOndutyRecorde.getRecordeNo(), stationOndutyRecorde.getTeamList(), stationOndutyRecorde.getTotalFee(), stationOndutyRecorde.getCarNum(),
                                        stationOndutyRecorde.getCheckNum(), stationOndutyRecorde.getReturnNum(), stationOndutyRecorde.getExemptionAmount(), stationOndutyRecorde.getInFlow(),
                                        stationOndutyRecorde.getOutFlow(), stationOndutyRecorde.getNotifyCheck(), stationOndutyRecorde.getImplementState(), stationOndutyRecorde.getBeforeOnduty(),
                                        stationOndutyRecorde.getOndutyList(), stationOndutyRecorde.getAfterOnduty(), stationOndutyRecorde.getDuringOnduty(), stationOndutyRecorde.getTodo(), stationOndutyRecorde.getRemark(),
                                        stationOndutyRecorde.getExchangeSignature(), stationOndutyRecorde.getRecieveSignature(), false, true);
                                DaoUtils.getStationOndutyInstance().insertData(recorde);
                                isEditSave = false;
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                MyRow request = new MyRow();
                                request.put("userId", stationOndutyRecorde.getUserId());
                                request.put("personId", stationOndutyRecorde.getPersonId());
                                request.put("stationId", stationOndutyRecorde.getStaionId());
                                request.put("timeStamp", stationOndutyRecorde.getTimeStamp());
                                request.put("dutyDate", stationOndutyRecorde.getDate());
                                request.put("weekDay", stationOndutyRecorde.getWeekday());
                                request.put("weather", stationOndutyRecorde.getWeather());
                                request.put("visitNo", stationOndutyRecorde.getRecordeNo());
                                request.put("stationHeaderName", stationOndutyRecorde.getStationName());
                                request.put("ticketName", stationOndutyRecorde.getTicketName());
                                request.put("ticketId", stationOndutyRecorde.getTicketId());
                                request.put("totalFee", stationOndutyRecorde.getTotalFee());
                                request.put("checkNum", stationOndutyRecorde.getCheckNum());
                                request.put("returnNum", stationOndutyRecorde.getReturnNum());
                                request.put("carNum", stationOndutyRecorde.getCarNum());
                                request.put("exemptionAmountInAdvance", stationOndutyRecorde.getExemptionAmount());
                                request.put("inletFlow", stationOndutyRecorde.getInFlow());
                                request.put("exitFlow", stationOndutyRecorde.getOutFlow());
                                request.put("notifyCheck", stationOndutyRecorde.getNotifyCheck());
                                request.put("implementState", stationOndutyRecorde.getImplementState());

                                request.put("beforeOnduty", stationOndutyRecorde.getBeforeOnduty());
                                request.put("duringOnduty", stationOndutyRecorde.getDuringOnduty());
                                request.put("afterOnduty", stationOndutyRecorde.getAfterOnduty());

                                request.put("backlog", stationOndutyRecorde.getTodo());
                                request.put("remark", stationOndutyRecorde.getRemark());
                                request.put("shiftExchangeSignatureBase64", stationOndutyRecorde.getExchangeSignature());
                                request.put("succeedSignatureBase64", stationOndutyRecorde.getRecieveSignature());

                                uploadStationRecorde(request, new OnSuccessAndFaultSub(this, "uploadStationRecorde", "正在提交值班记录...", this));
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    public void saveData() {
        if (isEditSave)
            stationOndutyRecorde.setId(stationOndutyRecorde.getId());
        else
            stationOndutyRecorde.setId(null);
        stationOndutyRecorde.setIsSubmit(false);
        stationOndutyRecorde.setIsComplete(false);
        stationOndutyRecorde.setUserId(User.getInstance().getUserId());
        stationOndutyRecorde.setPersonId(User.getInstance().getPersonId());
        stationOndutyRecorde.setStaionId(User.getInstance().getStationId());
        stationOndutyRecorde.setDate(getEText(R.id.et_date));
        stationOndutyRecorde.setWeekday(getEText(R.id.et_weekday));
        stationOndutyRecorde.setWeather(getEText(R.id.et_weather));
        stationOndutyRecorde.setTimeStamp(getEText(R.id.et_time_range));
        stationOndutyRecorde.setStationName(getEText(R.id.et_station));
        stationOndutyRecorde.setTicketName(getEText(R.id.et_ticket));
        stationOndutyRecorde.setTicketId(ticketId);
        stationOndutyRecorde.setTeamList(teamDatas);

        stationOndutyRecorde.setTotalFee(getEText(R.id.et_totle_fee));
        stationOndutyRecorde.setCheckNum(getEText(R.id.et_check_num));
        stationOndutyRecorde.setReturnNum(getEText(R.id.et_return_num));
        stationOndutyRecorde.setCarNum(getEText(R.id.et_car_num));
        stationOndutyRecorde.setNotifyCheck(getEText(R.id.et_notify_check));
        stationOndutyRecorde.setImplementState(getEText(R.id.et_implement_state));
        stationOndutyRecorde.setExemptionAmount(getEText(R.id.et_free_money));
        stationOndutyRecorde.setInFlow(getEText(R.id.et_in_num));
        stationOndutyRecorde.setOutFlow(getEText(R.id.et_out_num));

        stationOndutyRecorde.setBeforeOnduty(getEText(R.id.et_class_require));
        stationOndutyRecorde.setOndutyList(new MyData());
        stationOndutyRecorde.setDuringOnduty(getEText(R.id.et_during_onduty));
        stationOndutyRecorde.setAfterOnduty(getEText(R.id.et_class_sumup));

        stationOndutyRecorde.setTodo(getEText(R.id.et_todo));
        stationOndutyRecorde.setRemark(getEText(R.id.et_station_remark));
        stationOndutyRecorde.setExchangeSignature(signatureRow.getString("signatureExchangeBt"));
        stationOndutyRecorde.setRecieveSignature(signatureRow.getString("signatureRecieveBt"));
    }

    private boolean checkNull() {
        if (Utils.checkNullString(stationOndutyRecorde.getStationName()) ||
                Utils.checkNullString(stationOndutyRecorde.getWeather()) ||
                Utils.checkNullString(stationOndutyRecorde.getTimeStamp()) ||
                Utils.checkNullString(stationOndutyRecorde.getTotalFee()) ||
                Utils.checkNullString(stationOndutyRecorde.getCheckNum()) ||
                Utils.checkNullString(stationOndutyRecorde.getReturnNum()) ||
                Utils.checkNullString(stationOndutyRecorde.getCarNum()) ||
                Utils.checkNullString(stationOndutyRecorde.getBeforeOnduty()) ||
                Utils.checkNullString(stationOndutyRecorde.getAfterOnduty()) ||
                Utils.checkNullString(stationOndutyRecorde.getNotifyCheck()) ||
                Utils.checkNullString(stationOndutyRecorde.getImplementState()) ||
                Utils.checkNullString(stationOndutyRecorde.getExemptionAmount()) ||
                Utils.checkNullString(stationOndutyRecorde.getInFlow()) ||
                Utils.checkNullString(stationOndutyRecorde.getOutFlow())) {
            UI.showToast(this, "请完善基本信息！");
            return true;
        }

        if (teamDatas != null && teamDatas.size() > 0) {
            for (int i = teamDatas.size() - 1; i >= 0; i--) {
                StationTeamEntity en = teamDatas.get(i);
                if (Utils.checkNullString(en.getMonitor()) &&
                        Utils.checkNullString(en.getNumber()) &&
                        Utils.checkNullString(en.getGroupType()) &&
                        Utils.checkNullString(en.getTimeRange())) {
                    teamDatas.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
        stationOndutyRecorde.setTeamList(teamDatas);
        if (teamDatas != null && teamDatas.size() > 0) {
            for (int i = 0; i < teamDatas.size(); i++) {
                StationTeamEntity en = teamDatas.get(i);
                if (Utils.checkNullString(en.getMonitor()) ||
                        Utils.checkNullString(en.getNumber()) ||
                        Utils.checkNullString(en.getGroupType()) ||
                        Utils.checkNullString(en.getTimeRange())) {
                    UI.showToast(this, "请填写完整的班次信息！");
                    return true;
                }
            }
        }

        if (teamDatas == null || teamDatas.size() == 0) {
            UI.showToast(this, "请填写班次信息！");
            return true;
        }
        if (Utils.checkNullString(stationOndutyRecorde.getTodo())) {
            UI.showToast(this, "请完善待办事项！");
            return true;
        }
        if (Utils.checkNullString(stationOndutyRecorde.getExchangeSignature())) {
            UI.showToast(this, "交班站长未签名！");
            return true;
        }
        if (Utils.checkNullString(stationOndutyRecorde.getRecieveSignature())) {
            UI.showToast(this, "接班站长未签名！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "uploadStationRecorde":
                MyRow row = (MyRow) result.obj;
                MyData request = new MyData();
                for (StationTeamEntity entity : stationOndutyRecorde.getTeamList()) {
                    MyRow ro = new MyRow();
                    ro.put("siteManagerId", row.getString("id"));
                    ro.put("monitorId", entity.getMonitorId());
                    ro.put("teamId", entity.getTeamId());
                    ro.put("groupType", entity.getGroupType());
                    ro.put("timeRange", entity.getTimeRange());
                    ro.put("number", entity.getNumber());
                    request.add(ro);
                }
                uploadStationTeams(request, new OnSuccessAndFaultSub(this, "uploadStationTeams", "正在提交班组信息...", this));
                break;
            case "uploadStationTeams":
                DaoUtils.getStationOndutyInstance().deleteById(stationOndutyRecorde.getId());
                //站长值班记录始终保存最新数据（除交班，注销），不清空
                StationOndutyRecorde recorde = new StationOndutyRecorde(null, stationOndutyRecorde.getUserId(), stationOndutyRecorde.getPersonId(),
                        stationOndutyRecorde.getStaionId(), stationOndutyRecorde.getTimeStamp(), stationOndutyRecorde.getStationName(), stationOndutyRecorde.getTicketName(),
                        stationOndutyRecorde.getTicketId(), stationOndutyRecorde.getDate(), stationOndutyRecorde.getWeekday(), stationOndutyRecorde.getWeather(),
                        stationOndutyRecorde.getRecordeNo(), stationOndutyRecorde.getTeamList(), stationOndutyRecorde.getTotalFee(), stationOndutyRecorde.getCarNum(),
                        stationOndutyRecorde.getCheckNum(), stationOndutyRecorde.getReturnNum(), stationOndutyRecorde.getExemptionAmount(), stationOndutyRecorde.getInFlow(),
                        stationOndutyRecorde.getOutFlow(), stationOndutyRecorde.getNotifyCheck(), stationOndutyRecorde.getImplementState(), stationOndutyRecorde.getBeforeOnduty(),
                        stationOndutyRecorde.getOndutyList(), stationOndutyRecorde.getAfterOnduty(), stationOndutyRecorde.getDuringOnduty(), stationOndutyRecorde.getTodo(), stationOndutyRecorde.getRemark(),
                        stationOndutyRecorde.getExchangeSignature(), stationOndutyRecorde.getRecieveSignature(), false, true);
                DaoUtils.getStationOndutyInstance().insertData(recorde);
                UI.showToast(getApplicationContext(), "班长值班记录已提交成功！");
                isEditSave = false;
//                clearEdit();
                break;
        }
    }

    private void clearEdit() {
        setEText(R.id.et_ticket, "");
        setEText(R.id.et_time_range, "");
        setEText(R.id.et_totle_fee, "");
        setEText(R.id.et_check_num, "");
        setEText(R.id.et_return_num, "");
        setEText(R.id.et_car_num, "");
        setEText(R.id.et_notify_check, "");
        setEText(R.id.et_implement_state, "");
        setEText(R.id.et_free_money, "");
        setEText(R.id.et_in_num, "");
        setEText(R.id.et_out_num, "");
        setEText(R.id.et_class_require, "");
        setEText(R.id.et_class_during, "");
        setEText(R.id.et_class_sumup, "");
        setEText(R.id.et_todo, "");
        setEText(R.id.et_remark, "");
        setImage(exchangeImg, R.drawable.selector_solid_gray);
        setImage(recieveImg, R.drawable.selector_solid_gray);
        signatureRow.clear();
        teamDatas.clear();
        teamDatas.add(new StationTeamEntity());
        teamDatas.add(new StationTeamEntity());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    /**
     * 选择班次
     *
     * @param holder
     * @param position
     */
    public void selectTeam(ViewHolder holder, int position) {
        tempPos = position;
        showListDialog(holder.getView(R.id.tv_team), teams);
    }

    /**
     * 选择班次时间段
     *
     * @param position
     */
    public void selectTimeRange(View view, int position) {
        tempPos = position;
        initTimerPicker(true);
        mTimerPicker.show(view, "请选择开始时间", C.df_yMdH.format(new Date()));
    }

    /**
     * 选择班次班长
     *
     * @param holder
     * @param position
     */
    public void selectMonitor(ViewHolder holder, int position) {
        tempPos = position;
        showListDialog(holder.getView(R.id.tv_monitor), monitors.toArray(new String[0]));
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.et_weather://天气
                setEText(view.getId(), getResources().getStringArray(R.array.weather_list)[index]);
                setImage(R.id.iv_weather, UI.getId(this, "ic_weather" + index, "mipmap"));
                break;
            case R.id.tv_team:
                teamDatas.get(tempPos).setGroupType(teams[index]);
                teamDatas.get(tempPos).setTeamId(teamIds[index]);
                break;
            case R.id.tv_monitor:
                teamDatas.get(tempPos).setMonitor(monitors.get(index));
                teamDatas.get(tempPos).setMonitorId(monitorIds.get(index));
                break;
            case R.id.et_ticket:
                setEText(view.getId(), tickets.get(index));
                ticketId = ticketIds.get(index);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public void addTeamLl() {
        teamDatas.add(new StationTeamEntity());
        adapter.notifyDataSetChanged();
    }

    public void removeTeamLl(int position) {
        teamDatas.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, teamDatas.size() - position);
    }

    public void getMembersData() {
        List<StationEmployee> list = DaoUtils.getStationEmployeeInstance().loadTypeData(BC.MONITOR_KEY,"");
        monitors.clear();
        monitorIds.clear();
        if (list != null && list.size() > 0) {
            for (StationEmployee ro : list) {
                monitors.add(ro.getName());
                monitorIds.add(ro.getId().toString());
            }
        }
        List<StationEmployee> list1 = DaoUtils.getStationEmployeeInstance().loadTypeData(BC.TICKET_KEY,"");
        tickets.clear();
        ticketIds.clear();
        if (list1 != null && list1.size() > 0) {
            for (StationEmployee ro : list1) {
                tickets.add(ro.getName());
                ticketIds.add(ro.getId().toString());
            }
        } else {
            hide(R.id.ticket_ll);
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
