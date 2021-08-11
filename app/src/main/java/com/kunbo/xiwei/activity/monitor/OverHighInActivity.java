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

import com.kunbo.xiwei.AppApplication;
import com.kunbo.xiwei.activity.MyBaseActivity;
import com.kunbo.xiwei.activity.SignatureActivity;
import com.kunbo.xiwei.adapter.OverHighInPhotoAdapter;
import com.kunbo.xiwei.db.entity.OverHighCarIn;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
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

import static com.zyf.net.api.ApiSubscribe.uploadOverHighCarIn;
import static com.zyf.net.api.ApiSubscribe.uploadPhotos;

/**
 * 治超管理---入口超限
 */
public class OverHighInActivity extends MyBaseActivity implements OnSuccessAndFaultListener {
    private OverHighInPhotoAdapter adapter;
    @BindView(R.id.recycler_photo)
    RecyclerView recyclerPhoto;
    @BindView(R.id.care_person_ll)
    LinearLayout carePersonLl;
    @BindView(R.id.img_tip_man)
    ImageView signatureTipImg;
    @BindView(R.id.img_check_man)
    ImageView signatureCheckerImg;
    @BindView(R.id.img_monitor)
    ImageView signatureMonitorImg;
    MyRow signatureRow = new MyRow();//签名数据
    List<String> overCarInPhotos = new ArrayList<>();//图片数据
    @AutoRestore
    int width;
    @AutoRestore
    int dateFullCount = 0;
    @AutoRestore
    int tempposition;//点击的图片位置
    static int MAX_PIC_NUMBER = 6;
    private CustomDatePickerDialog mTimerPicker;
    OverHighCarIn overInRecorde = new OverHighCarIn();
    private boolean isEditSave = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_in);
        ButterKnife.bind(this);
        //show(R.id.top_submit);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
    }

    private void initView() {
        if (!AppUtil.isTablet(AppApplication.getContext()))
            width = UI.getScreenWidth(this) / 4;
        else
            width = UI.getScreenWidth(this) / 8;

        setEText(R.id.et_check_time, C.df_yMdHm.format(new Date()));
        if (!TextUtils.isEmpty(User.getInstance().getSignature())) {
            signatureRow.put("signatureMonitorBt", ImageUtil.encodeUrlToBase64(User.getInstance().getSignature()));
            signatureMonitorImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitorImg, User.getInstance().getSignature());
        }
        //TODO 查询数据库，更新UI
        List<OverHighCarIn> recordes = DaoUtils.getOverHighInInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {
            if (!recordes.get(recordes.size() - 1).getIsSubmit()) {
                overInRecorde = recordes.get(recordes.size() - 1);
                isEditSave = true;
                setData();
            }
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.tv_station, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.tv_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.tv_role, User.getInstance().getTeamName() + User.getInstance().getJob());
            setEText(R.id.et_check_team, User.getInstance().getTeamName());
        }
        if (overCarInPhotos.size() < MAX_PIC_NUMBER) {
            overCarInPhotos.add("");
        }
        adapter = new OverHighInPhotoAdapter(this, R.layout.item_photo, overCarInPhotos);
        RecyclerView.LayoutManager gridLy;
        if (!AppUtil.isTablet(AppApplication.getContext())) {//手机
            gridLy = new GridLayoutManager(this, 4);
        } else {//平板
            gridLy = new GridLayoutManager(this, 6);
        }
        recyclerPhoto.setLayoutManager(gridLy);
        recyclerPhoto.setAdapter(adapter);
        recyclerPhoto.setNestedScrollingEnabled(false);

        setStep(this, new String[]{"基本信息", "现场查验", "告知事项"}, true);
    }

    private void setData() {
        setEText(R.id.et_reason, overInRecorde.getReason());
        setEText(R.id.et_car_no, overInRecorde.getCarNo());
        setEText(R.id.et_file_no, overInRecorde.getFileNo());
        setEText(R.id.et_owner_name, overInRecorde.getCarOwnerName());
        setEText(R.id.et_run_no, overInRecorde.getRunNo());

        setEText(R.id.et_out_name, overInRecorde.getName());
        setEText(R.id.et_out_relation, overInRecorde.getRelation());
        setEText(R.id.et_out_company, overInRecorde.getCompany());
        setEText(R.id.et_legal_people, overInRecorde.getCompanyLegalPeople());

        setEText(R.id.et_check_time, overInRecorde.getCheckDate());
        setEText(R.id.et_pass_valid, overInRecorde.getValidDate());
        setEText(R.id.et_pass_no, overInRecorde.getPassNo());
        setEText(R.id.et_pass_company, overInRecorde.getPassCompany());
        setEText(R.id.et_check_weight, overInRecorde.getWeight());
        setEText(R.id.et_load_size, overInRecorde.getDimens());
        setEText(R.id.et_goods_name, overInRecorde.getGoodsName());
        setEText(R.id.et_transport_company, overInRecorde.getTransportCompany());
        setEText(R.id.et_is_equal, overInRecorde.getIsEqual() ? "是" : "否");
        setEText(R.id.et_check_way, overInRecorde.getCheckWay());
        setEText(R.id.et_is_care, overInRecorde.getIscare() ? "是" : "否");
        if (overInRecorde.getIscare()) {
            carePersonLl.setVisibility(View.VISIBLE);
        } else {
            carePersonLl.setVisibility(View.GONE);
        }
        setEText(R.id.et_care_person, overInRecorde.getCarePerson());
        setEText(R.id.et_else_case, overInRecorde.getOther());
        setEText(R.id.tv_rules, overInRecorde.getRules());
        setEText(R.id.et_tip_time, overInRecorde.getInformedDate());
        setEText(R.id.et_certif_no, overInRecorde.getCertificateNo());

        if (!Utils.checkNullString(overInRecorde.getSignatureInformed())) {
            signatureRow.put("signatureTipBt", overInRecorde.getSignatureInformed());
            signatureTipImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureTipImg, ImageUtil.base64ToBitmap(overInRecorde.getSignatureInformed()));
        }
        if (!Utils.checkNullString(overInRecorde.getSignatureChecker())) {
            signatureRow.put("signatureCheckerBt", overInRecorde.getSignatureChecker());
            signatureCheckerImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureCheckerImg, ImageUtil.base64ToBitmap(overInRecorde.getSignatureChecker()));
        }
        if (!Utils.checkNullString(overInRecorde.getSignatureMonitor())) {
            signatureRow.put("signatureMonitorBt", overInRecorde.getSignatureMonitor());
            signatureMonitorImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(signatureMonitorImg, ImageUtil.base64ToBitmap(overInRecorde.getSignatureMonitor()));
        }
        if (overInRecorde.getImages() != null && overInRecorde.getImages().size() > 0) {
            overCarInPhotos = overInRecorde.getImages();
        }
    }

    @OnClick({R.id.et_check_time, R.id.et_pass_valid, R.id.et_is_equal, R.id.et_is_care, R.id.img_tip_man, R.id.et_tip_time, R.id.img_check_man,
            R.id.img_monitor, R.id.save, R.id.submit})
    public void onClick(View view) {
        super.onClick(view);
        Intent i = new Intent(this, SignatureActivity.class);
        switch (view.getId()) {
         /*   case R.id.go_history://历史记录
                Bundle extras = new Bundle();
                extras.putString("from", "overIn");
                openIntent(HistoryActivity.class, "治超入口查验历史记录", extras);
                break;*/
            case R.id.et_check_time://查验时间
                initTimerPicker(true, true, true);
                mTimerPicker.show(view, "", C.df_yMdHm.format(new Date()));
                break;
            case R.id.et_tip_time://被告知日期
                initTimerPicker(true, false, false);
                mTimerPicker.show(view, "", C.df_yMd.format(new Date()));
                break;
            case R.id.et_pass_valid://通行证有效期
                initTimerPicker(true, false, false);
                mTimerPicker.show(view, "请选择开始日期", C.df_yMd.format(new Date()));
                break;
            case R.id.et_is_equal://车证是否相符
            case R.id.et_is_care://是否监护
                showListDialog(view, getResources().getStringArray(R.array.is_reduce));
                break;
            case R.id.img_tip_man://被告知人签字
                startActivityForResult(i, 1111);
                break;
            case R.id.img_check_man://查验人签字
                startActivityForResult(i, 1112);
                break;
            case R.id.img_monitor://当班班长签字
                startActivityForResult(i, 1113);
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
    public void onActivityResult(int mapCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(mapCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String path = data.getStringExtra("path");
            switch (mapCode) {
                case 1111:
                    signatureRow.put("signatureTipBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    setImage(signatureTipImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 1112:
                    signatureRow.put("signatureCheckerBt", ImageUtil.bitmapToBase64(ImageUtil.getBitmapFormPath(path)));
                    setImage(signatureCheckerImg, ImageUtil.getBitmapFormPath(path));
                    break;
                case 1113:
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
                DaoUtils.getOverHighInInstance().updateData(overInRecorde);
            } else {
                isEditSave=true;
                DaoUtils.getOverHighInInstance().insertData(overInRecorde);
            }
            UI.showToast(this, "保存成功！");
        } else {//提交数据
            if (!checkNull()) {
                overInRecorde.setIsSubmit(true);
                overInRecorde.setIsComplete(true);
                new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                        .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                            helloView.hide();
                            if (isEditSave)
                                DaoUtils.getOverHighInInstance().updateData(overInRecorde);
                            else
                                DaoUtils.getOverHighInInstance().insertData(overInRecorde);

                            if (!AppUtil.isNetworkConnected(this)) {
                                overInRecorde = new OverHighCarIn();
                                clearEdit();
                                isEditSave = false;
                                UI.showToast(this, "数据已保存，联网时可直接上传!");
                            } else {
                                if (overInRecorde.getImages() != null && overInRecorde.getImages().size() > 0) {
                                    MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
                                    //放入图片
                                    for (String photo : overInRecorde.getImages()) {
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

    public void saveData() {
        if (isEditSave)
            overInRecorde.setId(overInRecorde.getId());
        else
            overInRecorde.setId(null);
        overInRecorde.setIsSubmit(false);
        overInRecorde.setIsComplete(false);
        overInRecorde.setUserId(User.getInstance().getUserId());
        overInRecorde.setPersonId(User.getInstance().getPersonId());
        overInRecorde.setStaionId(User.getInstance().getStationId());
        overInRecorde.setCheckTeamId(User.getInstance().getTeamId());
        overInRecorde.setReason(getEText(R.id.et_reason));
        overInRecorde.setCheckTeam(getEText(R.id.et_check_team));
        overInRecorde.setCarNo(getEText(R.id.et_car_no));
        overInRecorde.setFileNo(getEText(R.id.et_file_no));
        overInRecorde.setCarOwnerName(getEText(R.id.et_owner_name));
        overInRecorde.setRunNo(getEText(R.id.et_run_no));

        overInRecorde.setName(getEText(R.id.et_out_name));
        overInRecorde.setRelation(getEText(R.id.et_out_relation));
        overInRecorde.setCompany(getEText(R.id.et_out_company));
        overInRecorde.setCompanyLegalPeople(getEText(R.id.et_legal_people));

        overInRecorde.setCheckDate(getEText(R.id.et_check_time));
        overInRecorde.setValidDate(getEText(R.id.et_pass_valid));
        overInRecorde.setPassNo(getEText(R.id.et_pass_no));
        overInRecorde.setPassCompany(getEText(R.id.et_pass_company));
        overInRecorde.setWeight(getEText(R.id.et_check_weight));
        overInRecorde.setDimens(getEText(R.id.et_load_size));
        overInRecorde.setGoodsName(getEText(R.id.et_goods_name));
        overInRecorde.setTransportCompany(getEText(R.id.et_transport_company));
        overInRecorde.setIsEqual(getEText(R.id.et_is_equal).equals("是"));
        overInRecorde.setCheckWay(getEText(R.id.et_check_way));
        overInRecorde.setIscare(getEText(R.id.et_is_care).equals("是"));
        if (overInRecorde.getIscare()) {
            overInRecorde.setCarePerson(getEText(R.id.et_care_person));
        }
        overInRecorde.setOther(getEText(R.id.et_else_case));
        overInRecorde.setRules(getEText(R.id.tv_rules));
        overInRecorde.setInformedDate(getEText(R.id.et_tip_time));
        overInRecorde.setCertificateNo(getEText(R.id.et_certif_no));

        overInRecorde.setSignatureInformed(signatureRow.getString("signatureTipBt"));
        overInRecorde.setSignatureChecker(signatureRow.getString("signatureCheckerBt"));
        overInRecorde.setSignatureMonitor(signatureRow.getString("signatureMonitorBt"));

        List<String> ll = new ArrayList<>();
        if (overCarInPhotos.size() > 0) {
            for (String s : overCarInPhotos) {
                if (!Utils.checkNullString(s))
                    ll.add(s);
            }
        }
        overInRecorde.setImages(ll);
    }

    private boolean checkNull() {
        if (!Utils.isCarNum(getEText(R.id.et_car_no))) {
            UI.showToast(this, "请输入正确的车牌号！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getReason()) ||
                Utils.checkNullString(overInRecorde.getCheckTeam()) ||
                Utils.checkNullString(overInRecorde.getFileNo()) ||
                Utils.checkNullString(overInRecorde.getCarOwnerName()) ||
                Utils.checkNullString(overInRecorde.getRunNo())) {
            UI.showToast(this, "请完善车辆基本信息！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getName()) ||
                Utils.checkNullString(overInRecorde.getRelation()) ||
                Utils.checkNullString(overInRecorde.getCompany()) ||
                Utils.checkNullString(overInRecorde.getCompanyLegalPeople())) {
            UI.showToast(this, "请完善当事人基本信息！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getCheckDate()) ||
                Utils.checkNullString(overInRecorde.getValidDate()) ||
                Utils.checkNullString(overInRecorde.getPassNo()) ||
                Utils.checkNullString(overInRecorde.getPassCompany()) ||
                Utils.checkNullString(overInRecorde.getWeight()) ||
                Utils.checkNullString(overInRecorde.getDimens()) ||
                Utils.checkNullString(overInRecorde.getGoodsName()) ||
                Utils.checkNullString(overInRecorde.getTransportCompany()) ||
                Utils.checkNullString(overInRecorde.getCheckWay())) {
            UI.showToast(this, "请完善现场查验信息！");
            return true;
        }
        if (overInRecorde.getIscare() && Utils.checkNullString(overInRecorde.getCarePerson())) {
            UI.showToast(this, "请输入监护人员！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getSignatureChecker())) {
            UI.showToast(this, "查验人员未签名！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getRules())) {
            UI.showToast(this, "请填写告知事项！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getInformedDate())) {
            UI.showToast(this, "被告知人未填写日期！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getSignatureChecker())) {
            UI.showToast(this, "查验人员未签名！");
            return true;
        }
        if (Utils.checkNullString(overInRecorde.getSignatureMonitor())) {
            UI.showToast(this, "当班班长未签名！");
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
            case "uploadOverHighCarIn":
                //数据提交后清空数据库
                DaoUtils.getOverHighInInstance().deleteById(overInRecorde.getId());
                UI.showToast(getApplicationContext(), "治超入口查验记录已提交成功！");
                isEditSave = false;
                clearEdit();
                break;
        }
    }

    private void uploadData(String imgIds) {
        //构建body
        MyRow request = new MyRow();
        request.put("imgIds", imgIds);
        request.put("userId", overInRecorde.getUserId());
        request.put("personId", overInRecorde.getPersonId());
        request.put("stationId", overInRecorde.getStaionId());
        request.put("serialNumber", "");
        request.put("thingMatter", overInRecorde.getReason());
        request.put("checkTeamId", overInRecorde.getCheckTeamId());
        request.put("basicCarLicencePlate", overInRecorde.getCarNo());
        request.put("basicCarMovingFilesNumber", overInRecorde.getFileNo());
        request.put("basicCarOwnerName", overInRecorde.getCarOwnerName());
        request.put("basicCarTradingCard", overInRecorde.getRunNo());

        request.put("partyName", overInRecorde.getName());
        request.put("partyVehicleRelation", overInRecorde.getRelation());
        request.put("partyCompanyName", overInRecorde.getCompany());
        request.put("partyCompanyLegalPerson", overInRecorde.getCompanyLegalPeople());
        String[] dateTime = overInRecorde.getCheckDate().split(" ");
        request.put("siteExamineDate", dateTime[0]);
        request.put("siteExamineTime", dateTime[1]);
        request.put("sitePassValidity", overInRecorde.getValidDate());
        request.put("sitePassageNumber", overInRecorde.getPassNo());
        request.put("siteIssueCompany", overInRecorde.getPassCompany());
        request.put("siteTestWeight", overInRecorde.getWeight());
        request.put("siteLoadSize", overInRecorde.getDimens());
        request.put("siteGoodsName", overInRecorde.getGoodsName());
        request.put("siteCarrierCompany", overInRecorde.getTransportCompany());
        request.put("siteIsMatch", overInRecorde.getIsEqual() ? "1" : "0");
        request.put("siteVerificationMode", overInRecorde.getCheckWay());
        request.put("siteIsTutelage", overInRecorde.getIscare() ? "1" : "0");
        request.put("siteGuardian", overInRecorde.getCarePerson());
        request.put("tellMatters", overInRecorde.getRules());
        request.put("siteOtherCondition", overInRecorde.getOther());
        request.put("informedDate", overInRecorde.getInformedDate());
        request.put("identifiedNumber", overInRecorde.getCertificateNo());

        request.put("nunciatusSignatureBase64", overInRecorde.getSignatureInformed());
        request.put("inspectoSignatureBase64", overInRecorde.getSignatureChecker());
        request.put("monitorSignatureBase64", overInRecorde.getSignatureMonitor());
        uploadOverHighCarIn(request, new OnSuccessAndFaultSub(this, "uploadOverHighCarIn", "正在提交数据...", this));
    }

    private void clearEdit() {
        setEText(R.id.et_reason, "");
        setEText(R.id.et_car_no, "");
        setEText(R.id.et_file_no, "");
        setEText(R.id.et_owner_name, "");
        setEText(R.id.et_run_no, "");

        setEText(R.id.et_out_name, "");
        setEText(R.id.et_out_relation, "");
        setEText(R.id.et_out_company, "");
        setEText(R.id.et_legal_people, "");

        setEText(R.id.et_check_time, "");
        setEText(R.id.et_pass_valid, "");
        setEText(R.id.et_pass_no, "");
        setEText(R.id.et_pass_company, "");
        setEText(R.id.et_check_weight, "");
        setEText(R.id.et_load_size, "");
        setEText(R.id.et_goods_name, "");
        setEText(R.id.et_transport_company, "");
        setEText(R.id.et_is_equal, "是");
        setEText(R.id.et_check_way, "");
        setEText(R.id.et_is_care, "是");
        carePersonLl.setVisibility(View.VISIBLE);

        setEText(R.id.et_care_person, "");
        setEText(R.id.et_else_case, "");
        setEText(R.id.tv_rules, getString(R.string.over_tips));
        setEText(R.id.et_tip_time, "");
        setEText(R.id.et_certif_no, "");

        setImage(signatureTipImg, R.drawable.selector_solid_gray);
        setImage(signatureCheckerImg, R.drawable.selector_solid_gray);
        setImage(signatureMonitorImg, R.drawable.selector_solid_gray);

        signatureRow.clear();
        overCarInPhotos.clear();
        overCarInPhotos.add("");
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
            case R.id.et_is_care:
                setEText(view.getId(), getResources().getStringArray(R.array.is_reduce)[index]);
                if (getEText(R.id.et_is_care).equals("是")) {
                    carePersonLl.setVisibility(View.VISIBLE);
                } else {
                    carePersonLl.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setEText(view.getId(), getResources().getStringArray(R.array.is_reduce)[index]);
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
                case R.id.et_pass_valid://通行证有效期
                    dateFullCount++;
                    if (dateFullCount < 2) {
                        setEText(v.getId(), C.df_yMd.format(new Date(timestamp)) + " - ");
                        mTimerPicker.show(v, "请选择截止日期", C.df_yMd.format(new Date()));
                    } else {
                        String startT = getEText(v.getId());
                        setEText(v.getId(), startT + C.df_yMd.format(new Date(timestamp)));
                        dateFullCount = 0;
                        mTimerPicker.dismissDialog();
                    }
                    break;
                case R.id.et_check_time://查验时间
                    setEText(v.getId(), C.df_yMdHm.format(new Date(timestamp)));
                    mTimerPicker.dismissDialog();
                    break;
                case R.id.et_tip_time://告知日期
                    setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
                    mTimerPicker.dismissDialog();
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
        if (TextUtils.isEmpty(overCarInPhotos.get(position))) {
            getPicture(view, new String[]{"相机", "相册"}, null, null, false);
        } else {
            Bundle bundle = new Bundle();
            MyData datas = new MyData();
            for (String ss : overCarInPhotos) {
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

    public void removePhoto(int position) {
        new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认删除图片？").setContentFontSize(15)
                .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                    overCarInPhotos.remove(position);
                    boolean isAdd = true;
                    for (String s : overCarInPhotos) {
                        if (Utils.checkNullString(s))
                            isAdd = false;
                    }
                    if (isAdd) {
                        overCarInPhotos.add("");
                    }
                    adapter.notifyDataSetChanged();
                    helloView.hide();
                }))
                .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
    }

    @Override
    protected void doUploadImage(String tmpfile, Bitmap small) {
        super.doUploadImage(tmpfile, small);
//        Bitmap bp = ImageUtil.getBitmapFormPath(tmpfile);
        overCarInPhotos.set(tempposition, tmpfile);
        if (overCarInPhotos.size() < MAX_PIC_NUMBER) {
            overCarInPhotos.add("");
        }
        adapter.notifyDataSetChanged();
    }
}
