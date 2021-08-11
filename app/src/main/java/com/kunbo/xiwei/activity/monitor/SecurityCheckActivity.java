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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.adapter.SecurityCheckAdapter;
import com.kunbo.xiwei.db.entity.SecurityCheckRecorde;
import com.kunbo.xiwei.db.entity.SecurityItems;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.SecurityItem;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.ListDialog;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.zyf.device.CamareAndCropActivity;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.uploadSecurityCheck;

/**
 * 安全巡检
 */
public class SecurityCheckActivity extends CamareAndCropActivity implements OnSuccessAndFaultListener {
    private SecurityCheckAdapter securityCheckAdapter;
    @BindView(R.id.check_list)
    RecyclerView recyclerCheck;
    @BindView(R.id.scroll)
    NestedScrollView scrollView;
    private List<String> feeMans = new ArrayList<>();//班组收费员
    private List<String> feeManIds = new ArrayList<>();//班组收费员id
    private List<SecurityItem> datas = new ArrayList<>();
    @AutoRestore
    int statePos, checkerPos, width, tempPos;
    private CustomDatePickerDialog mTimerPicker;
    private SecurityCheckRecorde checkRecorde = new SecurityCheckRecorde();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_check);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        show(R.id.refresh_ll);
        setEText(R.id.header_title, User.getInstance().getStationName() + "夜间巡检表");
        initView();
    }

    private void initView() {
        if (!AppUtil.isTablet(this))
            width = UI.getScreenWidth(Objects.requireNonNull(this)) / 4;
        else
            width = UI.getScreenWidth(Objects.requireNonNull(this)) / 8;
        hide(R.id.go_history);
        getMembersData();
       /* //TODO 查询数据库，更新UI
        List<SecurityCheckRecorde> recordes = DaoUtils.getSecurityCheckInstance().loadAllData();
        if (recordes != null && recordes.size() > 0) {//有保存的数据
            checkRecorde = recordes.get(0);
            datas = checkRecorde.getCheckList();
        }*/
        securityCheckAdapter = new SecurityCheckAdapter(this, datas);
        recyclerCheck.setAdapter(securityCheckAdapter);
        refreshItems();
    }

    @OnClick({R.id.refresh_ll, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_ll://刷新数据
                notifyDataChanged();
                break;
            /*case R.id.save://保存
                saveAndSubmitData(false);
                break;*/
            case R.id.submit://提交
                saveAndSubmitData(true);
                break;
        }
    }

    private void saveAndSubmitData(boolean submit) {
        checkRecorde.setCheckList(datas);
        if (!submit) {
            boolean isSave = DaoUtils.getSecurityCheckInstance().insertData(checkRecorde);
            if (isSave) {
                UI.showToast(this, "保存成功！");
            }
        } else {//提交数据
            MyData data = new MyData();
            boolean hasSelect = false;
            for (SecurityItem entity : checkRecorde.getCheckList()) {
                if (entity.isSelecte()) {
                    hasSelect = true;
                }
            }
            if (!hasSelect) {
                UI.showToast(this, "请选择需要提交的项目！");
                return;
            }
            for (SecurityItem entity : checkRecorde.getCheckList()) {
                if (entity.isSelecte()) {//选中的巡检项
                    if (Utils.checkNullString(entity.getState())) {//状态未选择
                        UI.showToast(this, "请填写状态描述！");
                        return;
                    }
                    if (entity.getState().equals("异常") || entity.getState().equals("破损")) {//异常情况未填写备注
                        if (Utils.checkNullString(entity.getRemark())) {
                            UI.showToast(this, "异常或破损状态请填写备注！");
                            return;
                        }
                    }
                    entity.setSubmit(true);
                    entity.setComplete(true);
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
            new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("确认提交数据？").setContentFontSize(15)
                    .addAction(new LemonHelloAction("确定", (helloView, helloInfo, helloAction) -> {
                        helloView.hide();
                        DaoUtils.getSecurityCheckInstance().insertData(checkRecorde);
                        if (!AppUtil.isNetworkConnected(this)) {
                            checkRecorde = new SecurityCheckRecorde();
                            refreshItems();
                            UI.showToast(this, "数据已保存，联网时可直接上传!");
                        } else {
                            uploadSecurityCheck(data, new OnSuccessAndFaultSub(this, "uploadSecurityCheck", "正在提交数据...", this));
                        }
                    }))
                    .addAction(new LemonHelloAction("取消", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
        }
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "uploadSecurityCheck":
                //数据提交后清空数据库
                DaoUtils.getSecurityCheckInstance().deleteById(checkRecorde.getId());
                scrollToTop(scrollView);
                UI.showToast(this, "安全巡检记录已提交成功！");
                //刷新巡检项目
                refreshItems();
                break;
        }
    }

    private void refreshItems() {
        datas.clear();
        List<SecurityItems> items = DaoUtils.getSecurityItemInstance().loadAllData();
        if (items != null && items.size() > 0) {
            for (SecurityItems item : items) {
                SecurityItem it = new SecurityItem(item.getId().toString(), item.getIsSelecte(), item.getExaminePeriodDesc(), C.df_yMdHm.format(new Date()), item.getExamineSite(),
                        item.getExamineLocation(), item.getExamineProject(), item.getExamineContext(), item.getState(), item.getChecker(), item.getCheckerId(), item.getRemark(), false, false);
                datas.add(it);
            }
            securityCheckAdapter.notifyDataSetChanged();
            show(R.id.scroll);
            hide(R.id.empty_ll);
        } else {
            hide(R.id.scroll);
            show(R.id.empty_ll);
        }
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showYear, boolean showHour, boolean showMinute) {
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
            datas.get(tempPos).setExamineTime(C.df_yMdHm.format(new Date(timestamp)));
            mTimerPicker.dismissDialog();
            securityCheckAdapter.notifyDataSetChanged();
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

    public void selectDateTime(TextView tvRange, int position) {
        tempPos = position;
        initTimerPicker(true, true, true);
        mTimerPicker.show(tvRange, "", C.df_yMdHm.format(new Date()));
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.tv_state://状态描述
                datas.get(statePos).setState(getResources().getStringArray(R.array.check_state)[index]);
                break;
            case R.id.tv_checker://检查人员
                datas.get(checkerPos).setChecker(feeMans.get(index));
                datas.get(checkerPos).setCheckerId(feeManIds.get(index));
                break;
        }
        securityCheckAdapter.notifyDataSetChanged();
    }

    /**
     * 选择要提交的巡检项目
     */
    public void selectItem(int position) {
        datas.get(position).setSelecte(!datas.get(position).isSelecte());
        securityCheckAdapter.notifyDataSetChanged();
    }

    /**
     * 选择状态
     */
    public void selectState(View view, int position) {
        statePos = position;
        showListDialog(view, getResources().getStringArray(R.array.check_state));
    }

    /**
     * 选择检查人员
     */
    public void selectChecker(View view, int position) {
        checkerPos = position;
        ListDialog dialog = new ListDialog(this);
        dialog.setOnSelectedListener((view1, index) -> listSelected(view1, index));
        dialog.show(view, feeMans.toArray(new String[0]));

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
                runOnUiThread(() -> refreshItems());
            }
        }).start();
    }
}
