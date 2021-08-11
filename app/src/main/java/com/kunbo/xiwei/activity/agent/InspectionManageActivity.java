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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.adapter.InspectionTypeAdapter;
import com.kunbo.xiwei.db.entity.InspectionForm;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
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
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.xiweiapp.R;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.uploadInspectionForm;

/**
 * 稽查管理
 */
public class InspectionManageActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.type_list)
    RecyclerView typeList;
    private InspectionTypeAdapter adapter;
    @AutoRestore
    int timeFullCount = 0;
    private CustomDatePickerDialog mTimerPicker;
    InspectionForm inspectionForm = new InspectionForm();
    private boolean isEditSave = false;
    private MyData typeData = new MyData();//稽查类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_manage);
        ButterKnife.bind(this);
        //show(R.id.top_submit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hide(R.id.step_next_ll);
        show(R.id.submit);
        show(R.id.save);
        initView();
    }

    private void initView() {
        String[] types = getResources().getStringArray(R.array.inpection_type);
        for (int i = 0; i < types.length; i++) {
            MyRow ro = new MyRow();
            ro.put("pos", i);
            ro.put("type", types[i]);
            ro.put("selected", false);
            typeData.add(ro);
        }

        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
            setEText(R.id.et_inspection, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
        }
        setStep(this, new String[]{"基本信息"}, true);
        //TODO 查询数据库，更新UI
        List<InspectionForm> recordes = DaoUtils.getInspectionFormInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                inspectionForm = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        adapter = new InspectionTypeAdapter(this, typeData);
        typeList.setAdapter(adapter);
    }

    private void setData() {
        setEText(R.id.et_inspection, inspectionForm.getInspection());
        setEText(R.id.et_inspected, inspectionForm.getInspected());
        setEText(R.id.et_inspection_time, inspectionForm.getDateTime());
        setEText(R.id.et_inspection_content, inspectionForm.getContent());
        setEText(R.id.et_main_question, inspectionForm.getMainQes());
        setEText(R.id.et_party_signature, inspectionForm.getPartySignature());
        setEText(R.id.et_result, inspectionForm.getResult());
        setEText(R.id.et_leader_signature, inspectionForm.getLeaderSignature());
        setEText(R.id.et_inspector_signature, inspectionForm.getInspectorSignature());
        //setEText(R.id.et_remark, inspectionForm.getRemark());
    }

    @OnClick({R.id.et_inspection_time, R.id.save, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_inspection_time:
                initTimerPicker();
                mTimerPicker.show(view, "", C.df_yMdHm.format(new Date()));
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
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getInspectionFormInstance().updateData(inspectionForm);
            } else {
                isEditSave = true;
                DaoUtils.getInspectionFormInstance().insertData(inspectionForm);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                inspectionForm.setIsSubmit(true);
                inspectionForm.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getInspectionFormInstance().updateData(inspectionForm);
                            else
                                DaoUtils.getInspectionFormInstance().insertData(inspectionForm);

                            if (!AppUtil.isNetworkConnected(this)) {
                                inspectionForm = new InspectionForm();
                                isEditSave = false;
                                clearEdit();
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                MyRow request = new MyRow();
                                request.put("stationId", inspectionForm.getStaionId());
                                request.put("auditDepartment", inspectionForm.getInspection());
                                request.put("chechedCompany", inspectionForm.getInspected());
                                String[] dateTime = inspectionForm.getDateTime().split(" ");
                                request.put("auditDate", dateTime[0]);
                                request.put("auditTimeStamp", inspectionForm.getDateTime());
                                request.put("auditDetails", inspectionForm.getContent());
                                request.put("problemAbstract", inspectionForm.getMainQes());
                                request.put("result", inspectionForm.getResult());
                                request.put("party", inspectionForm.getPartySignature());
                                request.put("leader", inspectionForm.getLeaderSignature());
                                request.put("auditor", inspectionForm.getInspectorSignature());
                                request.put("type", inspectionForm.getRemark());
                                request.put("inputSource", "移动端");
                                uploadInspectionForm(request, new OnSuccessAndFaultSub(this, "uploadInspectionForm", "正在提交数据...", this));
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    public void saveData() {
        if (isEditSave)
            inspectionForm.setId(inspectionForm.getId());
        else
            inspectionForm.setId(null);
        inspectionForm.setStaionId(User.getInstance().getStationId());
        inspectionForm.setInspection(getEText(R.id.et_inspection));
        inspectionForm.setInspected(getEText(R.id.et_inspected));
        inspectionForm.setDateTime(getEText(R.id.et_inspection_time));
        inspectionForm.setContent(getEText(R.id.et_inspection_content));
        inspectionForm.setMainQes(getEText(R.id.et_main_question));
        inspectionForm.setResult(getEText(R.id.et_result));
        inspectionForm.setPartySignature(getEText(R.id.et_party_signature));
        inspectionForm.setLeaderSignature(getEText(R.id.et_leader_signature));
        inspectionForm.setInspectorSignature(getEText(R.id.et_inspector_signature));
        StringBuilder bf = new StringBuilder();
        if (typeData.size() > 0) {
            for (MyRow ro : typeData) {
                if (ro.getBoolean("selected"))
                    bf.append(ro.getInt("pos")).append(",");
            }
        }
        inspectionForm.setRemark(bf.toString());
    }

    private boolean checkNull() {
        if (Utils.checkNullString(inspectionForm.getInspection()) ||
                Utils.checkNullString(inspectionForm.getInspected()) ||
                Utils.checkNullString(inspectionForm.getDateTime()) ||
                Utils.checkNullString(inspectionForm.getContent()) ||
                Utils.checkNullString(inspectionForm.getMainQes()) ||
                Utils.checkNullString(inspectionForm.getResult()) ||
                Utils.checkNullString(inspectionForm.getPartySignature()) ||
                Utils.checkNullString(inspectionForm.getLeaderSignature()) ||
                Utils.checkNullString(inspectionForm.getInspectorSignature())) {
            UI.showToast(this, "请完善稽查信息！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        //数据提交后清空数据库
        DaoUtils.getInspectionFormInstance().deleteById(inspectionForm.getId());
        UI.showToast(this, "稽查管理记录已提交成功！");
        isEditSave = false;
        clearEdit();
    }

    private void clearEdit() {
        setEText(R.id.et_inspection, User.getInstance().getStationName());
        setEText(R.id.et_inspected, "");
        setEText(R.id.et_inspection_time, "");
        setEText(R.id.et_inspection_content, "");
        setEText(R.id.et_main_question, "");
        setEText(R.id.et_party_signature, "");
        setEText(R.id.et_result, "");
        setEText(R.id.et_leader_signature, "");
        setEText(R.id.et_inspector_signature, "");
        //setEText(R.id.et_remark, "");
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
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            if (Utils.checkNullString(getEText(v.getId())))
                timeFullCount = 0;
            timeFullCount++;
            if (timeFullCount < 2) {
                setEText(v.getId(), C.df_yMdHm.format(new Date(timestamp)));
                mTimerPicker.show(v, "请选择结束时间", C.df_yMdHm.format(new Date()));
            } else {
                String start = getEText(v.getId());
                setEText(v.getId(), start + " - " + C.df_yMdHm.format(new Date(timestamp)));
                mTimerPicker.dismissDialog();
                timeFullCount = 0;
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

    public void onItemClick(boolean checked, int position) {
        typeData.get(position).put("selected", !checked);
        adapter.notifyDataSetChanged();
    }
}
