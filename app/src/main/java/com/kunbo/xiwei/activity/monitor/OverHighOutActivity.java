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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.adapter.OverHighOutPhotoAdapter;
import com.kunbo.xiwei.adapter.OverHighOutQueryPhotoAdapter;
import com.kunbo.xiwei.db.entity.OverHighCarOut;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.QueryOverInDialog;
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
import com.zyf.view.recyclerview.ViewHolder;
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

import static com.zyf.net.api.ApiSubscribe.queryOverInList;
import static com.zyf.net.api.ApiSubscribe.uploadOverHighCarOut;
import static com.zyf.net.api.ApiSubscribe.uploadPhotos;

/**
 * 治超管理---出口超限
 */
public class OverHighOutActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    private OverHighOutPhotoAdapter adapter;
    @BindView(R.id.et_pass_no)
    EditText etPassNo;
    @BindView(R.id.recycler_photo)
    RecyclerView recyclerPhoto;
    @BindView(R.id.carin_recycler_photo)
    RecyclerView carInRecyclerPhoto;
    @BindView(R.id.care_person_ll)
    LinearLayout carePersonLl;
    @BindView(R.id.img_check_man)
    ImageView signatureCheckerImg;
    @BindView(R.id.img_fee_man)
    ImageView signatureFeeImg;
    @BindView(R.id.img_monitor)
    ImageView signatureMonitorImg;
    MyData queryImgs = new MyData();//查询入口查验图片
    List<String> overCarOutPhotos = new ArrayList<>();//出口图片数据
    MyRow signatureRow = new MyRow();//签名数据
    @AutoRestore
    int tempposition;//点击的图片位置
    @AutoRestore
    int dateFullCount = 0;
    @AutoRestore
    int width;
    @AutoRestore
    String inStationId;
    static int MAX_PIC_NUMBER = 6;
    CustomDatePickerDialog mTimerPicker;
    OverHighCarOut overOutRecorde = new OverHighCarOut();
    private boolean isEditSave = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_out);
        ButterKnife.bind(this);
        //show(R.id.top_submit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (!AppUtil.isTablet(this))
            width = UI.getScreenWidth(this) / 4;
        else
            width = UI.getScreenWidth(this) / 8;

        setEText(R.id.et_out_time, C.df_yMdHm.format(new Date()));
        if (!TextUtils.isEmpty(User.getInstance().getSignature())) {
            signatureRow.put("signatureMonitorBt", ImageUtil.encodeUrlToBase64(User.getInstance().getSignature()));
            signatureMonitorImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitorImg, User.getInstance().getSignature());
        }
        //TODO 查询数据库，更新UI
        List<OverHighCarOut> recordes = DaoUtils.getOverHighOutInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                overOutRecorde = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        if (overCarOutPhotos.size() < MAX_PIC_NUMBER) {
            overCarOutPhotos.add("");
        }
        adapter = new OverHighOutPhotoAdapter(this, R.layout.item_photo, overCarOutPhotos);
        RecyclerView.LayoutManager gridLy;
        if (!AppUtil.isTablet(this)) {//手机
            gridLy = new GridLayoutManager(this, 4);
        } else {//平板
            gridLy = new GridLayoutManager(this, 6);
        }
        recyclerPhoto.setLayoutManager(gridLy);
        recyclerPhoto.setAdapter(adapter);
        recyclerPhoto.setNestedScrollingEnabled(false);

        setStep(this, new String[]{"基本信息", "现场查验", "处理结果"}, true);
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
            setEText(R.id.et_check_team, User.getInstance().getTeamName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        etPassNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (Utils.checkNullString(etPassNo.getText().toString())) {
                    UI.showToast(this, "请输入通行证号！");
                    return false;
                }
                queryOverInList(getEText(R.id.et_car_no), getEText(R.id.et_pass_no), new OnSuccessAndFaultSub(this, "queryOverInList", "正在查询....", this));
            }
            return false;
        });
    }

    private void showQueryDialog(MyData data) {
        QueryOverInDialog dialog = new QueryOverInDialog(this);
        dialog.setItemSelectedListener(this::initView);
        dialog.show("请选择入口查验记录", data);
    }

    private void initView(MyRow row) {
        inStationId = row.getString("stationId");
        setEText(R.id.et_reason, row.getString("thingMatter"));
        setEText(R.id.et_car_no, row.getString("basicCarLicencePlate"));
        setEText(R.id.et_file_no, row.getString("basicCarMovingFilesNumber"));
        setEText(R.id.et_owner_name, row.getString("basicCarOwnerName"));
        setEText(R.id.et_run_no, row.getString("basicCarTradingCard"));
        setEText(R.id.et_size, row.getString("siteLoadSize"));
        setEText(R.id.et_in_weight, row.getString("siteTestWeight"));

        setEText(R.id.et_station_name, row.getString("stationName"));
        setEText(R.id.et_pass_valid, row.getString("sitePassValidity"));
        setEText(R.id.et_pass_company, row.getString("siteIssueCompany"));
        setEText(R.id.et_goods_name, row.getString("siteGoodsName"));
        setEText(R.id.et_is_equal, row.getString("siteIsMatch").equals("1") ? "是" : "否");
        setEText(R.id.et_is_care, row.getString("siteIsTutelage").equals("1") ? "是" : "否");
        if (row.getString("siteIsTutelage").equals("1")) {
            carePersonLl.setVisibility(View.VISIBLE);
            setEText(R.id.et_care_person, row.getString("siteGuardian"));
        } else {
            carePersonLl.setVisibility(View.GONE);
        }
        queryImgs = (MyData) row.get("entrImgAssemble");
        if (queryImgs != null && queryImgs.size() > 0) {
            show(R.id.carin_recycler_photo);
            OverHighOutQueryPhotoAdapter adapter = new OverHighOutQueryPhotoAdapter(this, queryImgs);
            RecyclerView.LayoutManager gridLy;
            if (!AppUtil.isTablet(this)) {//手机
                gridLy = new GridLayoutManager(this, 4);
            } else {//平板
                gridLy = new GridLayoutManager(this, 6);
            }
            carInRecyclerPhoto.setLayoutManager(gridLy);
            carInRecyclerPhoto.setAdapter(adapter);
            carInRecyclerPhoto.setNestedScrollingEnabled(false);
        } else {
            hide(R.id.carin_recycler_photo);
        }
    }

    private void setData() {
        setEText(R.id.et_reason, overOutRecorde.getReason());
        setEText(R.id.et_car_no, overOutRecorde.getCarNo());
        setEText(R.id.et_pass_no, overOutRecorde.getPassNo());
        setEText(R.id.et_file_no, overOutRecorde.getFileNo());
        setEText(R.id.et_owner_name, overOutRecorde.getOwnerName());
        setEText(R.id.et_run_no, overOutRecorde.getRunNo());
        setEText(R.id.et_bill_no, overOutRecorde.getBillNo());
        setEText(R.id.et_size, overOutRecorde.getDimens());
        setEText(R.id.et_car_weight, overOutRecorde.getAllWeight());
        setEText(R.id.et_in_weight, overOutRecorde.getInWeight());

        inStationId = overOutRecorde.getInStationId();
        setEText(R.id.et_station_name, overOutRecorde.getInStation());
        setEText(R.id.et_pass_valid, overOutRecorde.getValidDate());
        setEText(R.id.et_pass_company, overOutRecorde.getPassCompany());
        setEText(R.id.et_out_time, overOutRecorde.getOutDate());
        setEText(R.id.et_out_weight, overOutRecorde.getOutWeight());
        setEText(R.id.et_goods_name, overOutRecorde.getGoodsName());
        setEText(R.id.et_axleLoad, overOutRecorde.getAxleLoad());
        setEText(R.id.et_out_size, overOutRecorde.getOutDimens());
        setEText(R.id.et_has_certificate, overOutRecorde.getHasCertificate() ? "有" : "无");
        setEText(R.id.et_is_equal, overOutRecorde.getIsEqual() ? "是" : "否");
        setEText(R.id.et_is_care, overOutRecorde.getIsCare() ? "是" : "否");
        if (overOutRecorde.getIsCare()) {
            carePersonLl.setVisibility(View.VISIBLE);
        } else {
            carePersonLl.setVisibility(View.INVISIBLE);
        }
        setEText(R.id.et_care_person, overOutRecorde.getCarePerson());
        setEText(R.id.et_is_reduce, overOutRecorde.getIsReduce() ? "是" : "否");
        if (overOutRecorde.getIsReduce()) {
            hide(R.id.normal_fee_ll);
            show(R.id.reduce_ll);
            setEText(R.id.et_before_fee, overOutRecorde.getBeforeFee());
            setEText(R.id.et_after_fee, overOutRecorde.getAfterFee());
        } else {
            show(R.id.normal_fee_ll);
            hide(R.id.reduce_ll);
            setEText(R.id.et_total_fee, overOutRecorde.getTotalFee());
        }
        setEText(R.id.et_result, overOutRecorde.getResult());

        if (!Utils.checkNullString(overOutRecorde.getSignatureChecker())) {
            signatureRow.put("signatureCheckerBt", overOutRecorde.getSignatureChecker());
            signatureCheckerImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureCheckerImg, ImageUtil.base64ToBitmap(overOutRecorde.getSignatureChecker()));
        }
        if (!Utils.checkNullString(overOutRecorde.getSignatureFeeMan())) {
            signatureRow.put("signatureFeemanBt", overOutRecorde.getSignatureFeeMan());
            signatureFeeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureFeeImg, ImageUtil.base64ToBitmap(overOutRecorde.getSignatureFeeMan()));
        }
        if (!Utils.checkNullString(overOutRecorde.getSignatureMonitor())) {
            signatureRow.put("signatureMonitorBt", overOutRecorde.getSignatureMonitor());
            signatureMonitorImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitorImg, ImageUtil.base64ToBitmap(overOutRecorde.getSignatureMonitor()));
        }
        if (overOutRecorde.getImages() != null && overOutRecorde.getImages().size() > 0) {
            overCarOutPhotos = overOutRecorde.getImages();
        }
    }

    @OnClick({R.id.et_out_time, R.id.et_pass_valid, R.id.et_has_certificate, R.id.et_is_equal, R.id.et_is_care, R.id.et_is_reduce,
            R.id.img_check_man, R.id.img_fee_man, R.id.img_monitor, R.id.save, R.id.submit})
    public void onClick(View view) {
        super.onClick(view);
        Intent i = new Intent(this, SignatureActivity.class);
        switch (view.getId()) {
            case R.id.et_out_time://出站时间
                initTimerPicker(true, true, true);
                mTimerPicker.show(view, "", C.df_yMdHm.format(new Date()));
                break;
            case R.id.et_pass_valid://通行证有效期
                initTimerPicker(true, false, false);
                mTimerPicker.show(view, "请选择开始日期", C.df_yMd.format(new Date()));
                break;
            case R.id.et_has_certificate:
                showListDialog(view, getResources().getStringArray(R.array.has_certificate));
                break;
            case R.id.et_is_equal://车证是否相符
            case R.id.et_is_care://是否监护
            case R.id.et_is_reduce://是否减征
                showListDialog(view, getResources().getStringArray(R.array.is_reduce));
                break;
            case R.id.img_check_man://查验人员签字
                startActivityForResult(i, 1211);
                break;
            case R.id.img_fee_man://收费人员签字
                startActivityForResult(i, 1212);
                break;
            case R.id.img_monitor://当班班长签字
                startActivityForResult(i, 1213);
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
                case 1211:
                    signatureRow.put("signatureCheckerBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    setImage(signatureCheckerImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 1212:
                    signatureRow.put("signatureFeemanBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    setImage(signatureFeeImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 1213:
                    signatureRow.put("signatureMonitorBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    setImage(signatureMonitorImg, ImageUtil.getBitmapFormPath(path));
                    break;
            }
        }
    }

    private void saveAndSubmitData(boolean submit) {
        saveData();
        if (!submit) {
            if (isEditSave) {
                DaoUtils.getOverHighOutInstance().updateData(overOutRecorde);
            } else {
                isEditSave = true;
                DaoUtils.getOverHighOutInstance().insertData(overOutRecorde);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                overOutRecorde.setIsSubmit(true);
                overOutRecorde.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getOverHighOutInstance().updateData(overOutRecorde);
                            else
                                DaoUtils.getOverHighOutInstance().insertData(overOutRecorde);

                            if (!AppUtil.isNetworkConnected(this)) {
                                overOutRecorde = new OverHighCarOut();
                                clearEdit();
                                isEditSave = false;
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                if (overOutRecorde.getImages() != null && overOutRecorde.getImages().size() > 0) {
                                    //放入图片
                                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                                    for (String photo : overOutRecorde.getImages()) {
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

    private boolean checkNull() {
        if (!Utils.isCarNum(overOutRecorde.getCarNo())) {
            UI.showToast(this, "请输入正确的车牌号！");
            return true;
        }
        if (Utils.checkNullString(getEText(R.id.et_pass_no))) {
            UI.showToast(this, "请输入车辆通行证号！");
            return true;
        }
        if (Utils.checkNullString(overOutRecorde.getReason()) ||
                Utils.checkNullString(overOutRecorde.getCheckTeam()) ||
                Utils.checkNullString(overOutRecorde.getFileNo()) ||
                Utils.checkNullString(overOutRecorde.getOwnerName()) ||
                Utils.checkNullString(overOutRecorde.getRunNo()) ||
                Utils.checkNullString(overOutRecorde.getBillNo()) ||
                Utils.checkNullString(overOutRecorde.getDimens()) ||
                Utils.checkNullString(overOutRecorde.getAllWeight()) ||
                Utils.checkNullString(overOutRecorde.getInWeight())) {
            UI.showToast(this, "请完善车辆基本信息！");
            return true;
        }
        if (Utils.checkNullString(overOutRecorde.getInStation()) ||
                Utils.checkNullString(overOutRecorde.getOutDate()) ||
                Utils.checkNullString(overOutRecorde.getValidDate()) ||
                Utils.checkNullString(overOutRecorde.getPassCompany()) ||
                Utils.checkNullString(overOutRecorde.getOutWeight()) ||
                Utils.checkNullString(overOutRecorde.getGoodsName()) ||
                Utils.checkNullString(overOutRecorde.getAxleLoad()) ||
                Utils.checkNullString(overOutRecorde.getOutDimens()) ||
                Utils.checkNullString(overOutRecorde.getTotalFee()) ||
                Utils.checkNullString(overOutRecorde.getResult())) {
            UI.showToast(this, "请完善现场查验信息！");
            return true;
        }
        if (overOutRecorde.getIsCare() && Utils.checkNullString(overOutRecorde.getCarePerson())) {
            UI.showToast(this, "请输入监护人员！");
            return true;
        }
        if (Utils.checkNullString(overOutRecorde.getSignatureFeeMan())) {
            UI.showToast(this, "收费人员未签名！");
            return true;
        }
        if (Utils.checkNullString(overOutRecorde.getSignatureChecker())) {
            UI.showToast(this, "查验人员未签名！");
            return true;
        }
        if (Utils.checkNullString(overOutRecorde.getSignatureMonitor())) {
            UI.showToast(this, "当班班长未签名！");
            return true;
        }
        return false;
    }

    private void saveData() {
        if (isEditSave)
            overOutRecorde.setId(overOutRecorde.getId());
        else
            overOutRecorde.setId(null);
        overOutRecorde.setIsSubmit(false);
        overOutRecorde.setIsComplete(false);
        overOutRecorde.setStaionId(User.getInstance().getStationId());
        overOutRecorde.setCheckTeamId(User.getInstance().getTeamId());
        overOutRecorde.setReason(getEText(R.id.et_reason));
        overOutRecorde.setCheckTeam(getEText(R.id.et_check_team));
        overOutRecorde.setCarNo(getEText(R.id.et_car_no));
        overOutRecorde.setPassNo(getEText(R.id.et_pass_no));
        overOutRecorde.setFileNo(getEText(R.id.et_file_no));
        overOutRecorde.setOwnerName(getEText(R.id.et_owner_name));
        overOutRecorde.setRunNo(getEText(R.id.et_run_no));
        overOutRecorde.setBillNo(getEText(R.id.et_bill_no));
        overOutRecorde.setDimens(getEText(R.id.et_size));
        overOutRecorde.setAllWeight(getEText(R.id.et_car_weight));
        overOutRecorde.setInWeight(getEText(R.id.et_in_weight));

        overOutRecorde.setInStation(getEText(R.id.et_station_name));
        overOutRecorde.setInStationId(inStationId);
        overOutRecorde.setValidDate(getEText(R.id.et_pass_valid));
        overOutRecorde.setPassCompany(getEText(R.id.et_pass_company));
        overOutRecorde.setOutDate(getEText(R.id.et_out_time));
        overOutRecorde.setOutWeight(getEText(R.id.et_out_weight));
        overOutRecorde.setGoodsName(getEText(R.id.et_goods_name));
        overOutRecorde.setAxleLoad(getEText(R.id.et_axleLoad));
        overOutRecorde.setOutDimens(getEText(R.id.et_out_size));
        overOutRecorde.setHasCertificate(getEText(R.id.et_has_certificate).equals("有"));
        overOutRecorde.setIsEqual(getEText(R.id.et_is_equal).equals("是"));
        overOutRecorde.setIsCare(getEText(R.id.et_is_care).equals("是"));
        if (overOutRecorde.getIsCare()) {
            overOutRecorde.setCarePerson(getEText(R.id.et_care_person));
        }
        overOutRecorde.setIsReduce(getEText(R.id.et_is_reduce).equals("是"));
        if (overOutRecorde.getIsReduce()) {
            overOutRecorde.setBeforeFee(getEText(R.id.et_before_fee));
            overOutRecorde.setAfterFee(getEText(R.id.et_after_fee));
            overOutRecorde.setTotalFee(getEText(R.id.et_after_fee));
        } else {
            overOutRecorde.setTotalFee(getEText(R.id.et_total_fee));
        }
        overOutRecorde.setResult(getEText(R.id.et_result));

        overOutRecorde.setSignatureFeeMan(signatureRow.getString("signatureFeemanBt"));
        overOutRecorde.setSignatureChecker(signatureRow.getString("signatureCheckerBt"));
        overOutRecorde.setSignatureMonitor(signatureRow.getString("signatureMonitorBt"));

        List<String> ll = new ArrayList<>();
        if (overCarOutPhotos.size() > 0) {
            for (String s : overCarOutPhotos) {
                if (!Utils.checkNullString(s))
                    ll.add(s);
            }
        }
        overOutRecorde.setImages(ll);
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "queryOverInList":
                MyData data = (MyData) result.obj;
                if (data != null && data.size() > 0) {
                    showQueryDialog(data);
                } else {
                    UI.showToast(this, "未查询到该车辆的入口查验记录！");
                    clearQueryEdit();
                }
                break;
            case "uploadOverHighCarOut":
                //数据提交后清空数据库
                DaoUtils.getOverHighOutInstance().deleteById(overOutRecorde.getId());
                UI.showToast(getApplicationContext(), "治超出口查验记录已提交成功！");
                isEditSave = false;
                clearEdit();
                break;
            case "uploadPhotos":
                uploadData(result.obj.toString());
                break;
        }
    }

    private void clearQueryEdit() {
        inStationId = "";
        setEText(R.id.et_reason, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_file_no, "");
        setEText(R.id.et_owner_name, "");
        setEText(R.id.et_run_no, "");
        setEText(R.id.et_size, "");
        setEText(R.id.et_in_weight, "");
        setEText(R.id.et_station_name, "");
        setEText(R.id.et_pass_valid, "");
        setEText(R.id.et_pass_company, "");
        setEText(R.id.et_goods_name, "");
        setEText(R.id.et_is_equal, "是");
        setEText(R.id.et_is_care, "是");
        setEText(R.id.et_care_person, "");
        carePersonLl.setVisibility(View.VISIBLE);
        hide(R.id.carin_recycler_photo);
    }

    public void uploadData(String imgIds) {
        MyRow request = new MyRow();
        request.put("imgIds", imgIds);
        request.put("serialNumber", "");
        request.put("stationId", overOutRecorde.getStaionId());
        request.put("thingMatter", overOutRecorde.getReason());
        request.put("checkTeamId", overOutRecorde.getCheckTeamId());
        request.put("basicCarLicencePlate", overOutRecorde.getCarNo());
        request.put("basicCarMovingFilesNumber", overOutRecorde.getFileNo());
        request.put("basicCarOwnerName", overOutRecorde.getOwnerName());
        request.put("basicCarTradingCard", overOutRecorde.getRunNo());
        request.put("msgOddNumber", overOutRecorde.getBillNo());
        request.put("msgGeometricDimension", overOutRecorde.getDimens());
        request.put("msgTotalWeight", overOutRecorde.getAllWeight());
        request.put("msgEntrWeight", overOutRecorde.getInWeight());

        request.put("siteEntrStationName", overOutRecorde.getInStation());
        request.put("siteEntrStationId", overOutRecorde.getInStationId());
        request.put("sitePassValidity", overOutRecorde.getValidDate());
        request.put("siteIssueCompany", overOutRecorde.getPassCompany());
        request.put("sitePassageNumber", overOutRecorde.getPassNo());
        String[] dateTime = overOutRecorde.getOutDate().split(" ");
        request.put("siteExitDate", dateTime[0]);
        request.put("siteExitTime", dateTime[1]);
        request.put("siteExitTestWeight", overOutRecorde.getOutWeight());
        request.put("siteGoodsName", overOutRecorde.getGoodsName());
        request.put("siteAxleAllocation", overOutRecorde.getAxleLoad());
        request.put("siteExitSize", overOutRecorde.getOutDimens());
        request.put("siteIsLicence", overOutRecorde.getHasCertificate() ? "1" : "0");
        request.put("siteIsMatch", overOutRecorde.getIsEqual() ? "1" : "0");
        request.put("siteIsTutelage", overOutRecorde.getIsCare() ? "1" : "0");
        request.put("siteGuardian", overOutRecorde.getCarePerson());
        if (!overOutRecorde.getIsReduce())
            request.put("sitePaymentMonty", overOutRecorde.getTotalFee());
        else
            request.put("sitePaymentMonty", overOutRecorde.getAfterFee());
        request.put("resultCode", overOutRecorde.getResult());

        request.put("tollSignatureBase64", overOutRecorde.getSignatureFeeMan());
        request.put("inspectoSignatureBase64", overOutRecorde.getSignatureChecker());
        request.put("monitorSignatureBase64", overOutRecorde.getSignatureMonitor());
        uploadOverHighCarOut(request, new OnSuccessAndFaultSub(this, "uploadOverHighCarOut", "正在提交数据...", this));
    }

    private void clearEdit() {
        setEText(R.id.et_reason, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_pass_no, "");
        setEText(R.id.et_file_no, "");
        setEText(R.id.et_owner_name, "");
        setEText(R.id.et_run_no, "");
        setEText(R.id.et_bill_no, "");
        setEText(R.id.et_size, "");
        setEText(R.id.et_car_weight, "");
        setEText(R.id.et_in_weight, "");
        hide(R.id.carin_recycler_photo);

        setEText(R.id.et_station_name, "");
        setEText(R.id.et_pass_valid, "");
        setEText(R.id.et_pass_company, "");
        setEText(R.id.et_out_time, "");
        setEText(R.id.et_out_weight, "");
        setEText(R.id.et_goods_name, "");
        setEText(R.id.et_axleLoad, "");
        setEText(R.id.et_out_size, "");
        setEText(R.id.et_has_certificate, "有");
        setEText(R.id.et_is_equal, "是");
        setEText(R.id.et_is_care, "是");
        carePersonLl.setVisibility(View.VISIBLE);
        setEText(R.id.et_care_person, "");
        setEText(R.id.et_is_reduce, "否");
        show(R.id.normal_fee_ll);
        hide(R.id.reduce_ll);
        setEText(R.id.et_total_fee, "");
        setEText(R.id.et_before_fee, "");
        setEText(R.id.et_after_fee, "");
        setEText(R.id.et_result, "");

        setImage(signatureFeeImg, R.drawable.selector_solid_gray);
        setImage(signatureCheckerImg, R.drawable.selector_solid_gray);
        setImage(signatureMonitorImg, R.drawable.selector_solid_gray);

        signatureRow.clear();
        overCarOutPhotos.clear();
        overCarOutPhotos.add("");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.et_has_certificate:
                setEText(view.getId(), getResources().getStringArray(R.array.has_certificate)[index]);
                break;
            case R.id.et_is_reduce://是否减征
                setEText(view.getId(), getResources().getStringArray(R.array.is_reduce)[index]);
                if (getEText(R.id.et_is_reduce).equals("是")) {
                    hide(R.id.normal_fee_ll);
                    show(R.id.reduce_ll);
                } else {
                    show(R.id.normal_fee_ll);
                    hide(R.id.reduce_ll);
                }
                break;
            case R.id.et_is_equal://车证是否相符
                setEText(view.getId(), getResources().getStringArray(R.array.is_reduce)[index]);
                break;
            case R.id.et_is_care://是否监护
                setEText(view.getId(), getResources().getStringArray(R.array.is_reduce)[index]);
                if (getEText(R.id.et_is_care).equals("是")) {
                    carePersonLl.setVisibility(View.VISIBLE);
                } else {
                    carePersonLl.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showYear, boolean showHour, boolean showMinute) {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            switch (v.getId()) {
                case R.id.et_out_time://出站时间
                    setEText(v.getId(), C.df_yMdHm.format(new Date(timestamp)));
                    mTimerPicker.dismissDialog();
                    break;
                case R.id.et_pass_valid://通行证有效期
                    dateFullCount++;
                    if (dateFullCount < 2) {
                        setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
                        mTimerPicker.show(v, "请选择截止日期", C.df_yMd.format(new Date()));
                    } else {
                        String startT = getEText(v.getId());
                        setEText(v.getId(), startT + " - " + C.df_yMd.format(new Date(timestamp)));
                        dateFullCount = 0;
                        mTimerPicker.dismissDialog();
                    }
                    break;
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

    public void onItemClick(View view, ViewHolder holder, int position) {
        tempposition = position;
        if (TextUtils.isEmpty(overCarOutPhotos.get(position))) {
            getPicture(view, new String[]{"相机", "相册"}, null, null, false);
        } else {
            Bundle bundle = new Bundle();
            MyData datas = new MyData();
            for (String ss : overCarOutPhotos) {
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

    public void onDetailItemClickListener(View itemView, int position) {
        Bundle bundle = new Bundle();
        MyData datas = new MyData();
        for (MyRow ro : queryImgs) {
            MyRow row = new MyRow();
            row.put("path", BC.BASE_URL_IMAGE + ro.getString("accessPath"));
            datas.add(row);
        }
        bundle.putSerializable("datas", datas);
        bundle.putInt("position", position);
        openIntent(LargePicDispalyActivity.class, "查看大图", bundle);
    }

    public void removePhoto(int position) {
        new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认删除图片？").setContentFontSize(15)
                .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                    overCarOutPhotos.remove(position);
                    boolean isAdd = true;
                    for (String s : overCarOutPhotos) {
                        if (Utils.checkNullString(s))
                            isAdd = false;
                    }
                    if (isAdd) {
                        overCarOutPhotos.add("");
                    }
                    adapter.notifyDataSetChanged();
                    helloView.hide();
                }))
                .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
    }

    @Override
    protected void doUploadImage(String tmpfile, Bitmap small) {
        super.doUploadImage(tmpfile, small);
        overCarOutPhotos.set(tempposition, tmpfile);
        if (overCarOutPhotos.size() < MAX_PIC_NUMBER) {
            overCarOutPhotos.add("");
        }
        adapter.notifyDataSetChanged();
    }
}
