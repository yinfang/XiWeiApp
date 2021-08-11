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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.db.entity.CivilizedTypes;
import com.kunbo.xiwei.db.entity.ComplaintForm;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.zyf.domain.C;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.uploadComplaintForm;

/**
 * 投诉管理
 */
public class ComplaintManageActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    private List<String> froms = new ArrayList<>();//投诉来源
    private List<String> fromIds = new ArrayList<>();//投诉来源id
    private List<String> types = new ArrayList<>();//投诉类型
    private List<String> typeIds = new ArrayList<>();//投诉类型id
    @AutoRestore
    int width;
    @AutoRestore
    String fromId, typeId;
    private CustomDatePickerDialog mTimerPicker;
    ComplaintForm complaintForm = new ComplaintForm();
    private boolean isEditSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_manage);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        show(R.id.refresh_ll);
        hide(R.id.step_next_ll);
        show(R.id.submit);
        show(R.id.save);
        initView();
    }

    private void initView() {
        if (!AppUtil.isTablet(this)) {
            width = UI.getScreenWidth(this) / 4;
        } else {
            width = UI.getScreenWidth(this) / 8;
        }
        setEText(R.id.et_recive_time, C.df_yMdHm.format(new Date()));
        //TODO 查询数据库，更新UI
        List<ComplaintForm> recordes = DaoUtils.getComplaintFormInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                complaintForm = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
            setEText(R.id.et_receiver, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
        }
        setStep(this, new String[]{"基本信息"}, true);
        getMembersData();
    }

    private void setData() {
        setEText(R.id.et_complaint_from, complaintForm.getComplainantFrom());
        setEText(R.id.et_complaint_type, complaintForm.getComplainantType());
        setEText(R.id.et_complainant, complaintForm.getComplainantSignature());
        setEText(R.id.et_recive_time, complaintForm.getDate());
        setEText(R.id.et_ticket_no, complaintForm.getBillNo());
        setEText(R.id.et_car_no, complaintForm.getCarNo());
        setEText(R.id.et_mobile, complaintForm.getMobileNo());
        setEText(R.id.et_complaint_content, complaintForm.getComplaintDesc());
        setEText(R.id.et_check_desc, complaintForm.getInvestigation());
        setEText(R.id.et_if_solve, complaintForm.getDoneAdvice());
        fromId = complaintForm.getComplainantFromId();
        typeId = complaintForm.getComplainantTypeId();
    }

    @OnClick({R.id.refresh_ll, R.id.et_complaint_from, R.id.et_complaint_type, R.id.et_recive_time, R.id.et_if_solve, R.id.save, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_ll://刷新数据
                notifyDataChanged();
                break;
            case R.id.et_recive_time:
                initTimerPicker();
                mTimerPicker.show(view, "", C.df_yMdHm.format(new Date()));
                break;
            case R.id.et_complaint_from://投诉来演
                showListDialog(view, froms.toArray(new String[0]));
                break;
            case R.id.et_complaint_type://投诉类型
                showListDialog(view, types.toArray(new String[0]));
                break;
            case R.id.et_if_solve://处理结果
                showListDialog(view, getResources().getStringArray(R.array.is_solve));
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
    public void listSelected(View view, int index) {
        super.listSelected(view, index);
        switch (view.getId()) {
            case R.id.et_complaint_from://投诉来源
                setEText(view.getId(), froms.get(index));
                fromId = fromIds.get(index);
                break;
            case R.id.et_complaint_type://投诉类型
                setEText(view.getId(), types.get(index));
                typeId = typeIds.get(index);
                break;
            case R.id.et_if_solve://能否处理
                setEText(view.getId(), getResources().getStringArray(R.array.is_solve)[index]);
                break;
        }
    }

    private void saveAndSubmitData(boolean submit) {
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getComplaintFormInstance().updateData(complaintForm);
            } else {
                isEditSave = true;
                DaoUtils.getComplaintFormInstance().insertData(complaintForm);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                complaintForm.setIsSubmit(true);
                complaintForm.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (!AppUtil.isNetworkConnected(this)) {
                                if (isEditSave)
                                    DaoUtils.getComplaintFormInstance().updateData(complaintForm);
                                else
                                    DaoUtils.getComplaintFormInstance().insertData(complaintForm);
                                complaintForm = new ComplaintForm();
                                isEditSave = false;
                                clearEdit();
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                MyRow request = new MyRow();
                                request.put("stationId", complaintForm.getStaionId());
                                request.put("complaintFrom", complaintForm.getComplainantFrom());
                                request.put("complaintsTypeId", complaintForm.getComplainantTypeId());
                                request.put("complainant", complaintForm.getComplainantSignature());
                                request.put("receiver", complaintForm.getName());
                                String result = complaintForm.getDoneAdvice();
                                if (result.contains("已"))
                                    request.put("complainHandlerPersonId", complaintForm.getPersonId());
                                String[] dateTime = complaintForm.getDate().split(" ");
                                request.put("complainDate", dateTime[0]);
                                request.put("complainTime", dateTime[1]);
                                request.put("ticketNumber", complaintForm.getBillNo());
                                request.put("licensePlateNumber", complaintForm.getCarNo());
                                request.put("contactInformation", complaintForm.getMobileNo());
                                request.put("complaintContent", complaintForm.getComplaintDesc());
                                request.put("investigationSituation", complaintForm.getInvestigation());
                                request.put("isSolve", complaintForm.getDoneAdvice());
                                uploadComplaintForm(request, new OnSuccessAndFaultSub(this, "uploadComplaintForm", "正在提交数据...", this));
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    public void saveData() {
        if (isEditSave)
            complaintForm.setId(complaintForm.getId());
        else
            complaintForm.setId(null);
        complaintForm.setIsSubmit(false);
        complaintForm.setIsComplete(false);
        complaintForm.setStaionId(User.getInstance().getStationId());
        complaintForm.setPersonId(User.getInstance().getPersonId());
        complaintForm.setName(User.getInstance().getName());
        complaintForm.setComplainantFrom(getEText(R.id.et_complaint_from));
        complaintForm.setComplainantType(getEText(R.id.et_complaint_type));
        complaintForm.setComplainantSignature(getEText(R.id.et_complainant));
        complaintForm.setComplainantFromId(fromId);
        complaintForm.setComplainantTypeId(typeId);
        complaintForm.setDate(getEText(R.id.et_recive_time));
        complaintForm.setBillNo(getEText(R.id.et_ticket_no));
        complaintForm.setCarNo(getEText(R.id.et_car_no));
        complaintForm.setMobileNo(getEText(R.id.et_mobile));
        complaintForm.setComplaintDesc(getEText(R.id.et_complaint_content));
        complaintForm.setInvestigation(getEText(R.id.et_check_desc));
        complaintForm.setDoneAdvice(getEText(R.id.et_if_solve));
    }

    private boolean checkNull() {
        if (!Utils.checkMobile(getEText(R.id.et_mobile))) {
            UI.showToast(this, "请输入正确的手机号码！");
            return true;
        }
        if (!Utils.isCarNum(getEText(R.id.et_car_no))) {
            UI.showToast(this, "请输入正确的车牌号！");
            return true;
        }
        if (Utils.checkNullString(complaintForm.getComplainantFrom()) ||
                Utils.checkNullString(complaintForm.getComplainantType()) ||
                Utils.checkNullString(complaintForm.getDate()) ||
                Utils.checkNullString(complaintForm.getBillNo()) ||
                Utils.checkNullString(complaintForm.getComplaintDesc()) ||
                Utils.checkNullString(complaintForm.getDoneAdvice())) {
            UI.showToast(this, "请完善投诉信息！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        //数据提交后清空数据库
        DaoUtils.getComplaintFormInstance().deleteById(complaintForm.getId());
        UI.showToast(this, "投诉管理记录已提交成功！");
        isEditSave = false;
        clearEdit();
    }

    private void clearEdit() {
        setEText(R.id.et_complaint_from, "");
        setEText(R.id.et_complaint_type, "");
        setEText(R.id.et_complainant, "");
        setEText(R.id.et_ticket_no, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_mobile, "");
        setEText(R.id.et_complaint_content, "");
        setEText(R.id.et_check_desc, "");
        setEText(R.id.et_if_solve, "");
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker() {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, new CustomDatePickerDialog.Callback() {
            @Override
            public void onTimeSelected(View v, long timestamp) {
                setEText(v.getId(), C.df_yMdHm.format(new Date(timestamp)));
                mTimerPicker.dismissDialog();
            }
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时分
        mTimerPicker.setCanShowDateTime(true, true, true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    public void getMembersData() {
        List<CivilizedTypes> fromList = DaoUtils.getCivilizedTypesInstance().loadTypeData(BC.COMPLAINT_FROM);//投诉来源
        froms.clear();
        fromIds.clear();
        if (fromList != null && fromList.size() > 0) {
            for (CivilizedTypes ro : fromList) {
                froms.add(ro.getName());
                fromIds.add(ro.getId().toString());
            }
        }
        List<CivilizedTypes> typeList = DaoUtils.getCivilizedTypesInstance().loadTypeData(BC.COMPLAINT_TYPE);//投诉类型
        types.clear();
        typeIds.clear();
        if (typeList != null && typeList.size() > 0) {
            for (CivilizedTypes ro : typeList) {
                types.add(ro.getName());
                typeIds.add(ro.getId().toString());
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
