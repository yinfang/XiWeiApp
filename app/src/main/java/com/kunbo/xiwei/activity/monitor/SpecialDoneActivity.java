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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.HistoryActivity;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.adapter.SpecialPhotoAdapter;
import com.kunbo.xiwei.db.entity.CivilizedTypes;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.SpecialForm;
import com.kunbo.xiwei.db.entity.SpecialTypes;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.zyf.device.LargePicDispalyActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.OkHttpUtil;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.util.image.ImageUtil;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.xiweiapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.zyf.net.api.ApiSubscribe.uploadPhotos;
import static com.zyf.net.api.ApiSubscribe.uploadSpecailForm;

/**
 * 特情处理
 */
public class SpecialDoneActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    private SpecialPhotoAdapter adapter;
    @BindView(R.id.recycler_photo)
    RecyclerView recyclerPhoto;
    @BindView(R.id.img_feeman)
    ImageView signatureFeeman;
    @BindView(R.id.img_monitor)
    ImageView signatureMonitor;
    MyRow signatureRow = new MyRow();//签名数据
    List<String> specailPhotos = new ArrayList<>();//图片数据
    private List<String> lanes = new ArrayList<>();//车道
    private List<String> laneIds = new ArrayList<>();//车道id
    private List<String> types = new ArrayList<>();//特情类型
    private List<String> typeIds = new ArrayList<>();//特情类型id
    private List<String> carTypes = new ArrayList<>();//车型类型
    private List<String> carTypeIds = new ArrayList<>();//车型类型id
    List<String> feemans = new ArrayList<>();//收费员
    List<String> feemanIds = new ArrayList<>();//收费员id
    List<String> agents = new ArrayList<>();//站长
    List<String> agentIds = new ArrayList<>();//站长id
    @AutoRestore
    String typeId = "", laneId = "", carTypeId = "", feemanId = "", agentId = "";
    @AutoRestore
    static int MAX_PIC_NUMBER = 4;
    @AutoRestore
    int tempposition, width;
    String[] teams = {"白班", "夜班"};//班次
    private CustomDatePickerDialog mTimerPicker;
    SpecialForm specialRecorde = new SpecialForm();
    private boolean isEditSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_done);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        show(R.id.refresh_ll);
        show(R.id.go_history);
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
            setEText(R.id.et_team, User.getInstance().getTeamName());
        }
        if (!AppUtil.isTablet(this))
            width = UI.getScreenWidth(this) / 4;
        else
            width = UI.getScreenWidth(this) / 8;

        if (!TextUtils.isEmpty(User.getInstance().getSignature())) {
            signatureRow.put("signatureMonitor", ImageUtil.encodeUrlToBase64(User.getInstance().getSignature()));
            signatureMonitor.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitor, User.getInstance().getSignature());
        }
        setEText(R.id.et_date, C.df_yMd.format(new Date()));
        setEText(R.id.et_time, C.df_Hm.format(new Date()));
        //TODO 查询数据库，更新UI
        List<SpecialForm> recordes = DaoUtils.getSpecialDoneInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                specialRecorde = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }

        if (specailPhotos.size() < MAX_PIC_NUMBER) {
            specailPhotos.add("");
        }
        adapter = new SpecialPhotoAdapter(this, R.layout.item_photo, specailPhotos);
        RecyclerView.LayoutManager gridLy;
        if (!AppUtil.isTablet(this)) {//手机
            gridLy = new GridLayoutManager(this, 4);
        } else {//平板
            gridLy = new GridLayoutManager(this, 6);
        }
        recyclerPhoto.setLayoutManager(gridLy);
        recyclerPhoto.setAdapter(adapter);
        recyclerPhoto.setNestedScrollingEnabled(false);

        setStep(this, new String[]{"基本信息", "情况说明"}, true);
        getMembersData();
    }

    private void setData() {
        setEText(R.id.et_date, specialRecorde.getDate());
        setEText(R.id.et_time, specialRecorde.getTime());
        setEText(R.id.et_class, specialRecorde.getClassType());
        setEText(R.id.et_feeman, specialRecorde.getFeeman());
        setEText(R.id.et_job_no, specialRecorde.getJobNo());
        if (!Utils.checkNullString(specialRecorde.getSignatureFeeman())) {
            signatureRow.put("signatureFeeman", specialRecorde.getSignatureFeeman());
            signatureFeeman.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureFeeman, ImageUtil.base64ToBitmap(specialRecorde.getSignatureFeeman()));
        }
        if (!Utils.checkNullString(specialRecorde.getSignatureMonitor())) {
            signatureRow.put("signatureMonitor", specialRecorde.getSignatureMonitor());
            signatureMonitor.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitor, ImageUtil.base64ToBitmap(specialRecorde.getSignatureMonitor()));
        }
        setEText(R.id.et_car_road, specialRecorde.getLane());
        setEText(R.id.et_car_type, specialRecorde.getCarType());
        setEText(R.id.et_car_no, specialRecorde.getCarNo());
        setEText(R.id.et_specail_type, specialRecorde.getType());
        setEText(R.id.et_special_desc, specialRecorde.getDetail());
        if (specialRecorde.getPhotos() != null && specialRecorde.getPhotos().size() > 0) {
            specailPhotos = specialRecorde.getPhotos();
        }
        setEText(R.id.et_remark, specialRecorde.getRemark());
        setEText(R.id.et_if_solve, specialRecorde.getIsSolve());
        if (specialRecorde.getIsSolve().contains("无法")) {
            show(R.id.agent_ll);
            setEText(R.id.et_agent, specialRecorde.getSiteAgent());
        } else {
            hide(R.id.agent_ll);
        }
        typeId = specialRecorde.getTypeId();
        laneId = specialRecorde.getLaneId();
        feemanId = specialRecorde.getFeemanId();
        carTypeId = specialRecorde.getCarTypeId();
        agentId = specialRecorde.getSiteAgentId();
    }

    @OnClick({R.id.refresh_ll, R.id.et_date, R.id.et_time, R.id.et_class, R.id.et_feeman, R.id.et_car_road, R.id.et_specail_type, R.id.et_car_type,
            R.id.go_history, R.id.et_if_solve, R.id.et_agent, R.id.save, R.id.submit, R.id.img_feeman, R.id.img_monitor})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.refresh_ll://刷新数据
                notifyDataChanged();
                break;
            case R.id.et_date:
                initTimerPicker(true, false, false);
                mTimerPicker.show(view, "", C.df_yMd.format(new Date()));
                break;
            case R.id.et_time:
                initTimerPicker(false, true, true);
                mTimerPicker.show(view, "", C.df_Hm.format(new Date()));
                break;
            case R.id.et_class:
                showListDialog(view, teams);
                break;
            case R.id.et_car_road://车道
                showListDialog(view, lanes.toArray(new String[0]));
                break;
            case R.id.et_feeman://收费员
                showMyListDialog(view, feemans.toArray(new String[0]));
                break;
            case R.id.et_car_type://车型
                showListDialog(view, carTypes.toArray(new String[0]));
                break;
            case R.id.et_specail_type://特情类型
                showListDialog(view, types.toArray(new String[0]));
                break;
            case R.id.et_if_solve://能否解决
                showListDialog(view, getResources().getStringArray(R.array.is_solve));
                break;
            case R.id.et_agent:
                showListDialog(view, agents.toArray(new String[0]));
                break;
            case R.id.go_history://历史记录
                Bundle extras = new Bundle();
                extras.putString("from", "special");
                openIntent(HistoryActivity.class, "特情处理历史记录", extras);
                break;
            case R.id.img_feeman://收费员签字
                Intent i = new Intent(this, SignatureActivity.class);
                startActivityForResult(i, 1077);
                break;
            case R.id.img_monitor://班长签字
                Intent ii = new Intent(this, SignatureActivity.class);
                startActivityForResult(ii, 1078);
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
                case 1077:
                    signatureRow.put("signatureFeeman", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    signatureFeeman.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(signatureFeeman, ImageUtil.getBitmapFormPath(path));
                    break;
                case 1078:
                    signatureRow.put("signatureMonitor", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    signatureMonitor.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(signatureMonitor, ImageUtil.getBitmapFormPath(path));
                    break;
            }
        }
    }

    private void saveAndSubmitData(boolean submit) {
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getSpecialDoneInstance().updateData(specialRecorde);
            } else {
                isEditSave = true;
                DaoUtils.getSpecialDoneInstance().insertData(specialRecorde);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                specialRecorde.setIsSubmit(true);
                specialRecorde.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getSpecialDoneInstance().updateData(specialRecorde);
                            else
                                DaoUtils.getSpecialDoneInstance().insertData(specialRecorde);

                            if (!AppUtil.isNetworkConnected(this)) {
                                specialRecorde = new SpecialForm();
                                clearEdit();
                                isEditSave = false;
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                if (specialRecorde.getPhotos() != null && specialRecorde.getPhotos().size() > 0) {
                                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                                    //放入图片
                                    for (String photo : specialRecorde.getPhotos()) {
                                        File file = new File(photo);
                                        if (file.exists()) {
                                            RequestBody requestBody = OkHttpUtil.toRequestBodyOfImage(file);
                                            imgRequest.addFormDataPart("mFile", file.getName(), requestBody);
                                        }
                                    }
                                    uploadPhotos(imgRequest, new OnSuccessAndFaultSub(this, "uploadPhotos", "正在提交图片...", this));
                                } else {
                                    uploadData("");
                                }
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    private void uploadData(String imgIds) {
        MyRow request = new MyRow();
        request.put("stationId", specialRecorde.getStaionId());
        request.put("secretDate", specialRecorde.getDate());
        request.put("secretTime", specialRecorde.getTime());
        request.put("secretTeamId", specialRecorde.getTeamId());
        request.put("secretTeamType", specialRecorde.getClassType());
        request.put("secretLaneId", specialRecorde.getLaneId());
        request.put("secretPlateNumber", specialRecorde.getCarNo());
        request.put("secretVehicleTypeId", specialRecorde.getCarTypeId());
        request.put("secretClassifyId", specialRecorde.getTypeId());
        request.put("secretExplain", specialRecorde.getDetail());
        request.put("secretPersonId", specialRecorde.getPersonId());
        if (specialRecorde.getIsSolve().contains("无法")) {
            request.put("secretHandlerPersonId", specialRecorde.getSiteAgentId());
            request.put("secretHandlerPersonName", specialRecorde.getSiteAgent());
        } else {
            request.put("secretHandlerPersonId", specialRecorde.getPersonId());
            request.put("secretHandlerPersonName", specialRecorde.getName());
        }
        request.put("secretTollId", specialRecorde.getFeemanId());
        request.put("secretTollCard", specialRecorde.getJobNo());
        request.put("secretTicketId", "");
        request.put("secretOnDutySiteAgentId", specialRecorde.getSiteAgentId());
        request.put("secretIsNotSolve", specialRecorde.getIsSolve());
        request.put("remark", specialRecorde.getRemark());
        request.put("tollSignatureBase64", specialRecorde.getSignatureFeeman());
        request.put("monitorSignatureBase64", specialRecorde.getSignatureMonitor());
        request.put("imgIds", imgIds);
        uploadSpecailForm(request, new OnSuccessAndFaultSub(this, "uploadSpecailForm", "正在提交数据...", this));
    }

    public void saveData() {
        if (isEditSave)
            specialRecorde.setId(specialRecorde.getId());
        else
            specialRecorde.setId(null);
        specialRecorde.setIsSubmit(false);
        specialRecorde.setIsComplete(false);
        specialRecorde.setPersonId(User.getInstance().getPersonId());
        specialRecorde.setStaionId(User.getInstance().getStationId());
        specialRecorde.setTeamId(User.getInstance().getTeamId());
        specialRecorde.setName(User.getInstance().getName());
        specialRecorde.setDate(getEText(R.id.et_date));
        specialRecorde.setTime(getEText(R.id.et_time));
        specialRecorde.setClassType(getEText(R.id.et_class));
        specialRecorde.setFeeman(getEText(R.id.et_feeman));
        specialRecorde.setFeemanId(feemanId);
        specialRecorde.setJobNo(getEText(R.id.et_job_no));
        specialRecorde.setSignatureFeeman(signatureRow.getString("signatureFeeman"));
        specialRecorde.setSignatureMonitor(signatureRow.getString("signatureMonitor"));
        specialRecorde.setLane(getEText(R.id.et_car_road));
        specialRecorde.setLaneId(laneId);
        specialRecorde.setCarType(getEText(R.id.et_car_type));
        specialRecorde.setCarTypeId(carTypeId);
        specialRecorde.setCarNo(getEText(R.id.et_car_no));
        specialRecorde.setType(getEText(R.id.et_specail_type));
        specialRecorde.setTypeId(typeId);
        specialRecorde.setDetail(getEText(R.id.et_special_desc));
        List<String> ll = new ArrayList<>();
        if (specailPhotos.size() > 0) {
            for (String s : specailPhotos) {
                if (!Utils.checkNullString(s))
                    ll.add(s);
            }
        }
        specialRecorde.setPhotos(ll);
        specialRecorde.setRemark(getEText(R.id.et_remark));
        specialRecorde.setIsSolve(getEText(R.id.et_if_solve));
        if (getEText(R.id.et_if_solve).contains("无法")) {
            specialRecorde.setSiteAgent(getEText(R.id.et_agent));
            specialRecorde.setSiteAgentId(agentId);
        }
    }

    private boolean checkNull() {
        if (!Utils.isCarNum(getEText(R.id.et_car_no))) {
            UI.showToast(this, "请输入正确的车牌号！");
            return true;
        }
        if (Utils.checkNullString(specialRecorde.getDate()) ||
                Utils.checkNullString(specialRecorde.getTime()) ||
                Utils.checkNullString(specialRecorde.getClassType()) ||
                Utils.checkNullString(specialRecorde.getFeeman()) ||
                Utils.checkNullString(specialRecorde.getJobNo()) ||
                Utils.checkNullString(specialRecorde.getLane()) ||
                Utils.checkNullString(specialRecorde.getCarType()) ||
                Utils.checkNullString(specialRecorde.getType()) ||
                Utils.checkNullString(specialRecorde.getDetail())) {
            UI.showToast(this, "请完善特情信息！");
            return true;
        }

        if (specialRecorde.getIsSolve().contains("无法") && Utils.checkNullString(specialRecorde.getSiteAgent())) {//不能处理
            UI.showToast(this, "无法处理的特情请选择当班站长处理！");
            return true;
        }
        if (Utils.checkNullString(specialRecorde.getSignatureFeeman())) {
            UI.showToast(this, "收费员未签名！");
            return true;
        }
        if (Utils.checkNullString(specialRecorde.getSignatureMonitor())) {
            UI.showToast(this, "班长未签名！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "uploadPhotos":
                uploadData(result.obj.toString());
                break;
            case "uploadSpecailForm":
                //数据提交后清空数据库
                DaoUtils.getSpecialDoneInstance().deleteById(specialRecorde.getId());
                UI.showToast(getApplicationContext(), "特情处理记录已提交成功！");
                isEditSave = false;
                clearEdit();
                break;
        }
    }

    private void clearEdit() {
        setEText(R.id.et_car_road, "");
        setEText(R.id.et_car_type, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_class, "");
        setEText(R.id.et_job_no, "");
        setEText(R.id.et_feeman, "");
        setEText(R.id.et_if_solve, "");
        setEText(R.id.et_agent, "");
        hide(R.id.agent_ll);
        setEText(R.id.et_remark, "");
        setEText(R.id.et_specail_type, "");
        setEText(R.id.et_special_desc, "");
        setImage(signatureFeeman, R.drawable.selector_solid_gray);
        setImage(signatureMonitor, R.drawable.selector_solid_gray);
        signatureRow.clear();
        specailPhotos.clear();
        specailPhotos.add("");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        tempposition = position;
        if (TextUtils.isEmpty(specailPhotos.get(position))) {
            getPicture(view, new String[]{"相机", "相册"}, null, null, false);
        } else {
            Bundle bundle = new Bundle();
            MyData datas = new MyData();
            for (String ss : specailPhotos) {
                MyRow ro = new MyRow();
                if (!TextUtils.isEmpty(ss)) {
                    ro.put("path", ss);
                    datas.add(ro);
                }
            }
            bundle.putSerializable("datas", datas);
            bundle.putInt("position", position);
            openIntent(LargePicDispalyActivity.class, "查看大图", bundle);
        }
    }

    /**
     * 移除图片
     *
     * @param position
     */
    public void removePhoto(int position) {
        new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认删除图片？").setContentFontSize(15)
                .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                    specailPhotos.remove(position);
                    boolean isAdd = true;
                    for (String s : specailPhotos) {
                        if (Utils.checkNullString(s))
                            isAdd = false;
                    }
                    if (isAdd) {
                        specailPhotos.add("");
                    }
                    adapter.notifyDataSetChanged();
                    helloView.hide();
                }))
                .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
    }

    @Override
    protected void doUploadImage(String tmpfile, Bitmap small) {
        super.doUploadImage(tmpfile, small);
        specailPhotos.set(tempposition, tmpfile);
        if (specailPhotos.size() < MAX_PIC_NUMBER) {
            specailPhotos.add("");
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showYear, boolean showHour, boolean showMinute) {
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            switch (v.getId()) {
                case R.id.et_date:
                    setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
                    break;
                case R.id.et_time:
                    setEText(v.getId(), C.df_Hm.format(new Date(timestamp)));
                    break;
            }
            mTimerPicker.dismissDialog();
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时分
        mTimerPicker.setCanShowDateTime(showYear, showHour, showMinute);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.et_class:
                setEText(view.getId(), teams[index]);
                break;
            case R.id.et_feeman://收费员
                setEText(view.getId(), feemans.get(index));
                feemanId = feemanIds.get(index);
                break;
            case R.id.et_if_solve://能否处理
                setEText(view.getId(), getResources().getStringArray(R.array.is_solve)[index]);
                if (index != 0)
                    show(R.id.agent_ll);
                else
                    hide(R.id.agent_ll);
                break;
            case R.id.et_agent://站长
                setEText(view.getId(), agents.get(index));
                agentId = agentIds.get(index);
                break;
            case R.id.et_car_road://车道
                setEText(view.getId(), lanes.get(index));
                laneId = laneIds.get(index);
                break;
            case R.id.et_car_type://车型
                setEText(view.getId(), carTypes.get(index));
                carTypeId = carTypeIds.get(index);
                break;
            case R.id.et_specail_type://特情类型
                setEText(view.getId(), types.get(index));
                typeId = typeIds.get(index);
                break;
        }
    }

    public void getMembersData() {
        List<StationEmployee> list = DaoUtils.getStationEmployeeInstance().loadAllData();//站所有人员
        feemans.clear();
        feemanIds.clear();
        if (list != null && list.size() > 0) {
            for (StationEmployee ro : list) {
                feemans.add(ro.getName());
                feemanIds.add(ro.getId().toString());
            }
        }
        List<StationEmployee> list1 = DaoUtils.getStationEmployeeInstance().loadTypeData(BC.AGENT_KEY, BC.AGENT_SUB_KEY);//站长
        agents.clear();
        agentIds.clear();
        if (list1 != null && list1.size() > 0) {
            for (StationEmployee ro : list1) {
                agents.add(ro.getName());
                agentIds.add(ro.getId().toString());
            }
        }
        List<LaneTypes> inLanes = DaoUtils.getLaneTypesInstance().loadAllData();//车道信息
        lanes.clear();
        laneIds.clear();
        if (inLanes != null && inLanes.size() > 0) {
            for (LaneTypes ro : inLanes) {
                lanes.add(ro.getName());
                laneIds.add(ro.getId().toString());
            }
        }
        List<SpecialTypes> specials = DaoUtils.getSpecialTypesInstance().loadAllData();//特情类型
        types.clear();
        typeIds.clear();
        if (specials != null && specials.size() > 0) {
            for (SpecialTypes ro : specials) {
                types.add(ro.getName());
                typeIds.add(ro.getId().toString());
            }
        }
        List<CivilizedTypes> civilies = DaoUtils.getCivilizedTypesInstance().loadTypeData(BC.CAR_TYPE);//车型类型
        carTypes.clear();
        carTypeIds.clear();
        if (civilies != null && civilies.size() > 0) {
            for (CivilizedTypes ro : civilies) {
                carTypes.add(ro.getName());
                carTypeIds.add(ro.getId().toString());
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
