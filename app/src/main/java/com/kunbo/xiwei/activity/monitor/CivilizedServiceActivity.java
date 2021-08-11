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

import androidx.annotation.Nullable;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.db.entity.CivilizedServiceForm;
import com.kunbo.xiwei.db.entity.CivilizedTypes;
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
import com.zyf.util.image.ImageUtil;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.uploadCivilizedService;

/**
 * 文明服务
 */
public class CivilizedServiceActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.img_out_people)
    ImageView signatureImg;
    MyRow signatureRow = new MyRow();//签名数据
    List<String> types = new ArrayList<>();//服务类型
    List<String> typeIds = new ArrayList<>();//服务类型id
    String typeId = "";
    @AutoRestore
    int width;
    private CustomDatePickerDialog mTimerPicker;
    CivilizedServiceForm civilizedForm = new CivilizedServiceForm();
    private boolean isEditSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilized_service);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        show(R.id.refresh_ll);
        hide(R.id.step_next_ll);
        show(R.id.submit);
        show(R.id.save);
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
        }
        if (!AppUtil.isTablet(this)) {
            width = UI.getScreenWidth(this) / 4;
        } else {
            width = UI.getScreenWidth(this) / 8;
        }
        setEText(R.id.et_date, C.df_yMd.format(new Date()));
        setEText(R.id.et_time, C.df_Hm.format(new Date()));

        //TODO 查询数据库，更新UI
        List<CivilizedServiceForm> recordes = DaoUtils.getCivilizedServiceInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                civilizedForm = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        setStep(this, new String[]{"基本信息"}, true);
        getMembersData();
    }

    private void setData() {
        setEText(R.id.et_date, civilizedForm.getDate());
        setEText(R.id.et_time, civilizedForm.getTime());
        setEText(R.id.et_num, civilizedForm.getNum());
        if (!Utils.checkNullString(civilizedForm.getOutSingnature())) {
            signatureRow.put("signatureBt", civilizedForm.getOutSingnature());
            signatureImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureImg, ImageUtil.base64ToBitmap(civilizedForm.getOutSingnature()));
        }
        setEText(R.id.et_car_no, civilizedForm.getCarNo());
        setEText(R.id.et_type, civilizedForm.getServiceType());
        setEText(R.id.et_using_goods, civilizedForm.getUseGoods());
        setEText(R.id.et_remark, civilizedForm.getRemark());
        typeId = civilizedForm.getServiceTypeId();
    }

    @OnClick({R.id.refresh_ll, R.id.et_date, R.id.et_time, R.id.et_type, R.id.save, R.id.submit, R.id.img_out_people})
    public void onClick(View view) {
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
            case R.id.et_type://服务类型
                showListDialog(view, types.toArray(new String[0]));
                break;
            case R.id.img_out_people://外勤签字
                Intent i = new Intent(this, SignatureActivity.class);
                startActivityForResult(i, 1088);
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
        setEText(view.getId(), types.get(index));
        typeId = typeIds.get(index);
    }

    private void saveAndSubmitData(boolean submit) {
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getCivilizedServiceInstance().updateData(civilizedForm);
            } else {
                isEditSave = true;
                DaoUtils.getCivilizedServiceInstance().insertData(civilizedForm);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                civilizedForm.setIsSubmit(true);
                civilizedForm.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getCivilizedServiceInstance().updateData(civilizedForm);
                            else
                                DaoUtils.getCivilizedServiceInstance().insertData(civilizedForm);

                            if (!AppUtil.isNetworkConnected(this)) {
                                civilizedForm = new CivilizedServiceForm();
                                isEditSave = false;
                                clearEdit();
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                MyRow request = new MyRow();
                                request.put("stationId", civilizedForm.getStaionId());
                                request.put("serviceDate", civilizedForm.getDate());
                                request.put("serviceTime", civilizedForm.getTime());
                                request.put("quantity", civilizedForm.getNum());
                                request.put("licensePlateNumber", civilizedForm.getCarNo());
                                request.put("serviceClassify", civilizedForm.getServiceTypeId());
                                request.put("useItems", civilizedForm.getUseGoods());
                                request.put("remark", civilizedForm.getRemark());
                                request.put("fieldSignatureBase64", civilizedForm.getOutSingnature());
                                uploadCivilizedService(request, new OnSuccessAndFaultSub(this, "uploadCivilizedService", "正在提交数据...", this));
                            }
                        }))
                        .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
            }
        }
    }

    public void saveData() {
        if (isEditSave)
            civilizedForm.setId(civilizedForm.getId());
        else
            civilizedForm.setId(null);
        civilizedForm.setIsSubmit(false);
        civilizedForm.setIsComplete(false);
        civilizedForm.setStaionId(User.getInstance().getStationId());
        civilizedForm.setDate(getEText(R.id.et_date));
        civilizedForm.setTime(getEText(R.id.et_time));
        civilizedForm.setNum(getEText(R.id.et_num));
        civilizedForm.setCarNo(getEText(R.id.et_car_no));
        civilizedForm.setServiceType(getEText(R.id.et_type));
        civilizedForm.setServiceTypeId(typeId);
        civilizedForm.setUseGoods(getEText(R.id.et_using_goods));
        civilizedForm.setRemark(getEText(R.id.et_remark));
        civilizedForm.setOutSingnature(signatureRow.getString("signatureBt"));
    }

    private boolean checkNull() {
        if (!Utils.isCarNum(civilizedForm.getCarNo())) {
            UI.showToast(this, "请输入正确的车牌号！");
            return true;
        }
        if (Utils.checkNullString(civilizedForm.getDate()) ||
                Utils.checkNullString(civilizedForm.getTime()) ||
                Utils.checkNullString(civilizedForm.getNum()) ||
                Utils.checkNullString(civilizedForm.getUseGoods())) {
            UI.showToast(this, "请完善文明服务信息！");
            return true;
        }
        if (Utils.checkNullString(civilizedForm.getOutSingnature())) {
            UI.showToast(this, "外勤未签名！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "uploadCivilizedService":
                //数据提交后清空数据库
                DaoUtils.getCivilizedServiceInstance().deleteById(civilizedForm.getId());
                UI.showToast(getApplicationContext(), "文明服务记录已提交成功！");
                isEditSave = false;
                clearEdit();
                break;
        }
    }

    private void clearEdit() {
        setEText(R.id.et_date, C.df_yMd.format(new Date()));
        setEText(R.id.et_time, C.df_Hm.format(new Date()));
        setEText(R.id.et_num, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_type, "");
        setEText(R.id.et_using_goods, "");
        setEText(R.id.et_remark, "");
        setImage(signatureImg, R.drawable.selector_solid_gray);
        signatureRow.clear();
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("path");
            switch (requestCode) {
                case 1088:
                    signatureRow.put("signatureBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    signatureImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                    setImage(signatureImg, ImageUtil.getBitmapFormPath(path));
                    break;
            }
        }
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showYear, boolean showHour, boolean showMinute) {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, new CustomDatePickerDialog.Callback() {
            @Override
            public void onTimeSelected(View v, long timestamp) {
                switch (v.getId()) {
                    case R.id.et_date:
                        setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
                        break;
                    case R.id.et_time:
                        setEText(v.getId(), C.df_Hm.format(new Date(timestamp)));
                        break;
                }
                mTimerPicker.dismissDialog();
            }
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

    public void getMembersData() {
        List<CivilizedTypes> civilies = DaoUtils.getCivilizedTypesInstance().loadTypeData(BC.SERVICE_TYPE);//便民服务类型
        types.clear();
        typeIds.clear();
        if (civilies != null && civilies.size() > 0) {
            for (CivilizedTypes ro : civilies) {
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

