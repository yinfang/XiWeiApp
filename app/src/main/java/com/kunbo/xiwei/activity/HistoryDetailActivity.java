package com.kunbo.xiwei.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.AppApplication;
import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.adapter.OndutyCarRoadDetailAdapter;
import com.kunbo.xiwei.adapter.OverHighOutQueryPhotoAdapter;
import com.kunbo.xiwei.adapter.StationDetailOnTeamAdapter;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.zyf.domain.C;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.AppUtil;
import com.zyf.util.JsonUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.util.image.ImageUtil;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.editMonitorRecorde;
import static com.zyf.xiweiapp.R.id.et_time_range;

/**
 * 历史记录详情
 */
public class HistoryDetailActivity extends MyBaseActivity {
    private OndutyCarRoadDetailAdapter carInAdapter, carOutAdapter;
    MyRow row;
    @AutoRestore
    int width;
    List teamDatas;
    List<HashMap> carInData, carOutData;
    RecyclerView carInList, carOutList;
    private List<String> feeMans = new ArrayList<>();//出入口车道收费员
    private List<String> feeManIds = new ArrayList<>();//出入口车道选择的收费员id
    private List<String> carInRoads = new ArrayList<>();//入口车道
    private List<String> carOutRoads = new ArrayList<>();//出口车道
    private List<String> carInRoadIds = new ArrayList<>();//入口车道id
    private List<String> carOutRoadIds = new ArrayList<>();//出口车道id
    boolean[] selected; //出入口车道收费员是否选中临时记录
    @AutoRestore
    boolean isCarInLane, isCarInFeeMan, isCarInTimeRange;//是否是入口车道选择收费员/时间段
    @AutoRestore
    int tempPos, timeFullCount = 0;
    @AutoRestore
    String outPeopleId;
    String[] teams = {"白班", "夜班"};//班次
    private CustomDatePickerDialog mTimerPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            row = JsonUtil.getRow(bundle.getString("data"));
            switch (Objects.requireNonNull(bundle.getString("from"))) {
                case "classOnduty":
                    setContentView(R.layout.activity_class_detail);
                    setClassOndutyData();
                    break;
                case "stationOnduty":
                    setContentView(R.layout.activity_station_detail);
                    setStationOndutyData();
                    break;
                case "special":
                    setContentView(R.layout.activity_special_detail);
                    setSpecialOData();
                    break;
                case "civilized":
                    setContentView(R.layout.activity_civilized_detail);
//                    setCivilizedData();
                    break;
                case "complaint":
                    setContentView(R.layout.activity_complaint_detail);
//                    setComplaintData();
                    break;
            }
        }
    }

    /**
     * 班长值班
     */
    private void setClassOndutyData() {
        getScreenWidth();
        UI.hideSoftKeyboard(this);
        carInList = findViewById(R.id.carin_list);
        carOutList = findViewById(R.id.carout_list);
        ImageView ondutyImg = findViewById(R.id.img_onduty_signature);
        ImageView exchangeImg = findViewById(R.id.img_exchange_signature);
        ImageView recieveImg = findViewById(R.id.img_recieve_signature);

        setImage(R.id.user_photo, User.getInstance().getPhoto(), R.mipmap.headimg_default, true);
        setEText(R.id.user_name, User.getInstance().getName());
        setEText(R.id.et_date, row.getString("dutyDate"));
        setEText(R.id.et_weekday, row.getString("weekDay"));
        setEText(R.id.et_weather, row.getString("weather"));
        setEText(R.id.et_team, row.getString("shiftType"));
        setEText(R.id.et_class, User.getInstance().getTeamName());
        setEText(et_time_range, row.getString("timeStamp"));
        setEText(R.id.et_out_people, row.getString("outworkerName"));
        setEText(R.id.et_total_fee, row.getString("draftedBy"));
        setEText(R.id.et_back_card, row.getString("cardRecycleNum"));
        setEText(R.id.et_output_card, row.getString("cardCreditNum"));
        outPeopleId = row.getString("outworkerId");

        setEText(R.id.et_class_require, row.getString("beforeOnduty"));
        setEText(R.id.et_work_content, row.getString("duringOnduty"));
        setEText(R.id.et_class_sumup, row.getString("afterOnduty"));
        setEText(R.id.et_onduty_remark, row.getString("remark"));

        if (!Utils.checkNullString(row.getString("shiftExchangeSignatureBase64"))) {
            exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(exchangeImg, ImageUtil.base64ToBitmap(row.getString("shiftExchangeSignatureBase64")));
        }

        if (row.get("assemble") != null) {
            teamDatas = (List) row.get("assemble");
            carInData = new ArrayList();
            carOutData = new ArrayList();
            if (teamDatas != null)
                for (Object obj : teamDatas) {
                    HashMap map = (HashMap) obj;
                    if ((map.get("laneType") + "").contains("CKCD")) {
                        carOutData.add(map);
                    } else {
                        carInData.add(map);
                    }
                }
            if (carInData.size() > 0) {
                carInAdapter = new OndutyCarRoadDetailAdapter(this, true, false, carInData);
                carInList.setAdapter(carInAdapter);
                carInList.setNestedScrollingEnabled(false);
            } else {
                hide(R.id.carin_list);
            }
            if (carOutData.size() > 0) {
                carOutAdapter = new OndutyCarRoadDetailAdapter(this, false, false, carOutData);
                carOutList.setAdapter(carOutAdapter);
                carOutList.setNestedScrollingEnabled(false);
            } else {
                hide(R.id.carout_list);
            }
        } else {
            hide(R.id.carin_list);
            hide(R.id.carout_list);
        }
        if (row.get("succession") != null) {
            HashMap receiveRow = (HashMap) row.get("succession");
            setEText(R.id.et_receive_people, receiveRow.get("succeedMonitorName") + "");
            setEText(R.id.et_money_exchange, receiveRow.get("succeedImprest") + "");
            setEText(R.id.et_goods_exchange, receiveRow.get("succeedLaneItems") + "");
            setEText(R.id.et_machine_run, receiveRow.get("succeedLaneEquipment") + "");
            setEText(R.id.et_inout_dirty, receiveRow.get("succeedSanitation") + "");
            setEText(R.id.et_service_goods, receiveRow.get("succeedServiceProduct") + "");
            setEText(R.id.et_else, receiveRow.get("succeedElse") + "");
            setEText(R.id.et_reveive_remark, receiveRow.get("remark") + "");
            if (!Utils.checkNullString(receiveRow.get("succeedSignatureBase64") + "")) {
                recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                setImage(recieveImg, ImageUtil.base64ToBitmap(receiveRow.get("succeedSignatureBase64").toString()));
            }
        }
        if (row.get("siteExamine") != null) {
            HashMap checkRow = (HashMap) row.get("siteExamine");
            setEText(R.id.et_standard_on, checkRow.get("standardMountGuard") + "");
            setEText(R.id.et_standard_exchange, checkRow.get("standardShiftProcedure") + "");
            setEText(R.id.et_work_record, checkRow.get("civilServiceWork") + "");
            setEText(R.id.et_num_same, checkRow.get("isRidesWithFit") + "");
            setEText(R.id.et_inandout_dirty, checkRow.get("kioskAndSquareHealth") + "");
            setEText(R.id.et_check_remark, checkRow.get("remark") + "");
            if (!Utils.checkNullString(checkRow.get("siteOfficeBase64") + "")) {
                ondutyImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
                setImage(ondutyImg, ImageUtil.base64ToBitmap(checkRow.get("siteOfficeBase64").toString()));
            }
        }
    }

    /**
     * 站长值班
     */
    private void setStationOndutyData() {
        getScreenWidth();

        RecyclerView teamList = findViewById(R.id.team_list);
        ImageView exchangeImg = findViewById(R.id.img_exchange_signature);
        ImageView recieveImg = findViewById(R.id.img_recieve_signature);

        setImage(R.id.user_photo, User.getInstance().getPhoto(), R.mipmap.headimg_default, true);
        setEText(R.id.user_name, User.getInstance().getName());
        setEText(R.id.et_date, row.getString("dutyDate"));
        setEText(R.id.et_weekday, row.getString("weekDay"));
        setEText(R.id.et_weather, row.getString("weather"));
        setEText(R.id.et_ticket, row.getString("ticketName"));
        if (row.get("teamDutyList") != null) {
            Object teamDatas = row.get("teamDutyList");
            StationDetailOnTeamAdapter adapter;
            adapter = new StationDetailOnTeamAdapter(this, (List) teamDatas);
            teamList.setAdapter(adapter);
            teamList.setNestedScrollingEnabled(false);
        } else {
            hide(R.id.team_list);
        }
//        setEText(R.id.et_recorde_no, row.getString("visitNo"));
        setEText(R.id.et_time_range, row.getString("timeStamp"));
        setEText(R.id.et_station, User.getInstance().getName());
        setEText(R.id.et_totle_fee, row.getString("totalFee"));
        setEText(R.id.et_check_num, row.getString("checkNum"));
        setEText(R.id.et_return_num, row.getString("returnNum"));
        setEText(R.id.et_car_num, row.getString("carNum"));
        setEText(R.id.et_notify_check, row.getString("notifyCheck"));
        setEText(R.id.et_implement_state, row.getString("implementState"));
        setEText(R.id.et_free_money, row.getString("exemptionAmountInAdvance"));
        setEText(R.id.et_in_num, row.getString("inletFlow"));
        setEText(R.id.et_out_num, row.getString("exitFlow"));

        setEText(R.id.et_class_require, row.getString("beforeOnduty"));
        setEText(R.id.et_class_during, row.getString("duringOnduty"));
        setEText(R.id.et_class_sumup, row.getString("afterOnduty"));

        setEText(R.id.et_todo, row.getString("backlog"));
        setEText(R.id.et_station_remark, row.getString("remark"));

        String exchangeSignature = row.getString("shiftExchangeSignatureBase64");
        if (!Utils.checkNullString(exchangeSignature)) {
            exchangeImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(exchangeImg, ImageUtil.base64ToBitmap(exchangeSignature));
        }
        String recieveSignature = row.getString("succeedSignatureBase64");
        if (!Utils.checkNullString(recieveSignature)) {
            recieveImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(recieveImg, ImageUtil.base64ToBitmap(recieveSignature));
        }
    }

    /**
     * 特情处理
     */
    private void setSpecialOData() {
        getScreenWidth();

        RecyclerView photoList = findViewById(R.id.recycler_photo);
        ImageView feemanImg = findViewById(R.id.img_feeman);
        ImageView monitorImg = findViewById(R.id.img_monitor);

        setImage(R.id.user_photo, User.getInstance().getPhoto(), R.mipmap.headimg_default, true);
        setEText(R.id.user_name, User.getInstance().getName());
        setEText(R.id.et_date, row.getString("secretDate"));
        setEText(R.id.et_time, row.getString("secretTime"));
        setEText(R.id.et_team, row.getString("secretTeamName"));
        setEText(R.id.et_class, row.getString("secretTeamType"));
        setEText(R.id.et_feeman, row.getString("secretTollName"));
        setEText(R.id.et_job_no, row.getString("secretTollCard"));
        setEText(R.id.et_car_road, row.getString("secretLaneName"));
        setEText(R.id.et_car_type, row.getString("secretVehicleTypeName"));
        setEText(R.id.et_car_no, row.getString("secretPlateNumber"));
        setEText(R.id.et_specail_type, row.getString("secretClassifyName"));
        setEText(R.id.et_special_desc, row.getString("secretExplain"));
        if (row.get("secretRecordImgAssemble") != null) {
            Object teamDatas = row.get("secretRecordImgAssemble");
            OverHighOutQueryPhotoAdapter adapter;
            adapter = new OverHighOutQueryPhotoAdapter(this, (List) teamDatas);
            photoList.setAdapter(adapter);
            photoList.setNestedScrollingEnabled(false);
        } else {
            hide(R.id.recycler_photo);
        }
        setEText(R.id.et_remark, row.getString("remark"));
        setEText(R.id.et_if_solve, row.getString("secretIsNotSolve"));

        String solveState = row.getString("secretState");
        setEText(R.id.et_solve_state, solveState);
        if (solveState.contains("未"))
            setEColor(R.id.et_solve_state, R.color.red);
        else
            setEColor(R.id.et_solve_state, R.color.darkgreen);

        setEText(R.id.et_resolver, row.getString("secretHandlerPersonName"));
        setEText(R.id.resolve_time, row.getString("secretHandlerTime"));

        String ticketDesc = row.getString("secretTicketStateDesc");
        setEText(R.id.tv_ticket_check, ticketDesc);
        setEText(R.id.tv_ticket, row.getString("secretTicketName"));
        if (ticketDesc.contains("待"))
            setEColor(R.id.tv_ticket_check, R.color.red);
        else
            setEColor(R.id.tv_ticket_check, R.color.darkgreen);

        String feemanSignature = row.getString("tollSignatureBase64");
        if (!Utils.checkNullString(feemanSignature)) {
            feemanImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(feemanImg, ImageUtil.base64ToBitmap(feemanSignature));
        }
        String recieveSignature = row.getString("monitorSignatureBase64");
        if (!Utils.checkNullString(recieveSignature)) {
            monitorImg.setLayoutParams(new LinearLayout.LayoutParams(width, width / 2));
            setImage(monitorImg, ImageUtil.base64ToBitmap(recieveSignature));
        }
    }

    public void getScreenWidth() {
        if (!AppUtil.isTablet(AppApplication.getContext())) {
            width = UI.getScreenWidth(this) / 4;
        } else {
            width = UI.getScreenWidth(this) / 8;
        }
    }

    /**
     * 班长值班记录编辑功能
     *
     * @param view
     */
    public void doWork(View view) {
        switch (view.getId()) {
            case R.id.et_out_people:
                showListDialog(view, feeMans.toArray(new String[0]));
                break;
            case R.id.et_team:
                showListDialog(view, teams);
                break;
            case R.id.et_time_range:
                mTimerPicker.show(view, "请选择开始时间", C.df_yMdHm.format(new Date()));
                break;
            case R.id.edit:
                UI.showSoftKeyboard(this);
                initData();
                initTimerPicker();
                initUi(true);
                break;
            case R.id.submit:
                if (!checkNull()) {
                    row.put("shiftType", getEText(R.id.et_team));
                    row.put("timeStamp", getEText(R.id.et_time_range));
                    row.put("outworkerName", outPeopleId);
                    row.put("draftedBy", getEText(R.id.et_total_fee));
                    row.put("cardRecycleNum", getEText(R.id.et_back_card));
                    row.put("cardCreditNum", getEText(R.id.et_output_card));

                    row.put("beforeOnduty", getEText(R.id.et_class_require));
                    row.put("duringOnduty", getEText(R.id.et_work_content));
                    row.put("afterOnduty", getEText(R.id.et_class_sumup));
                    row.put("remark", getEText(R.id.et_onduty_remark));
                    teamDatas = new ArrayList();
                    teamDatas.addAll(carInData);
                    teamDatas.addAll(carOutData);
                    row.put("assemble", teamDatas);
                    editMonitorRecorde(row, new OnSuccessAndFaultSub(this, "editMonitorRecorde", "正在提交数据...", this));
                }
        }
    }

    private boolean checkNull() {
        if (Utils.checkNullString(outPeopleId) ||
                Utils.checkNullString(getEText(R.id.et_total_fee)) ||
                Utils.checkNullString(getEText(R.id.et_back_card)) ||
                Utils.checkNullString(getEText(R.id.et_output_card)) ||
                Utils.checkNullString(getEText(R.id.et_class_require)) ||
                Utils.checkNullString(getEText(R.id.et_work_content)) ||
                Utils.checkNullString(getEText(R.id.et_class_sumup))) {
            UI.showToast(this, "请完善当班记录信息！");
            return true;
        }
        if (carInData != null && carInData.size() > 0) {
            for (int i = carInData.size() - 1; i >= 0; i--) {
                HashMap hash = carInData.get(i);
                if (Utils.checkNullString(hash.get("laneId") + "") &&
                        //Utils.checkNullString(hash.get("timeRange") + "") &&
                        Utils.checkNullString(hash.get("staffList") + "")) {
                    carInData.remove(i);
                }
            }
            carInAdapter.notifyDataSetChanged();
        }
        if (carInData != null && carInData.size() > 0) {
            for (int i = 0; i < carInData.size(); i++) {
                HashMap hash = carInData.get(i);
                if (Utils.checkNullString(hash.get("laneId") + "") ||
                        Utils.checkNullString(hash.get("staffList") + "")) {
                    UI.showToast(this, "请填写完整的入口车道信息！");
                    return true;
                }
            }
        }
        if (carInData == null || carInData.size() == 0) {
            UI.showToast(this, "请填写入口车道信息！");
            return true;
        }
        if (carOutData != null && carOutData.size() > 0) {
            for (int i = carOutData.size() - 1; i >= 0; i--) {
                HashMap hash1 = carOutData.get(i);
                if (Utils.checkNullString(hash1.get("laneId") + "") &&
                        //Utils.checkNullString(hash1.get("timeRange") + "") &&
                        Utils.checkNullString(hash1.get("staffList") + "")) {
                    carOutData.remove(i);
                }
            }
            carOutAdapter.notifyDataSetChanged();
        }
        if (carOutData != null && carOutData.size() > 0) {
            for (int i = 0; i < carOutData.size(); i++) {
                HashMap hash1 = carOutData.get(i);
                if (Utils.checkNullString(hash1.get("laneId") + "") ||
                        Utils.checkNullString(hash1.get("staffList") + "")) {
                    UI.showToast(this, "请填写完整的出口车道信息！");
                    return true;
                }
            }
        }
        if (carOutData == null || carOutData.size() == 0) {
            UI.showToast(this, "请填写出口车道信息！");
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(Result result, String method) {
        super.onSuccess(result, method);
        scrollToTop(findViewById(R.id.scroll));
        UI.showToast(this, "班长值班记录已提交成功！");
        initUi(false);
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        if (carInData == null || carInData.size() == 0) {
            carInData.add(new HashMap());
            carInAdapter.notifyDataSetChanged();
        }
        if (carOutData == null || carOutData.size() == 0) {
            carOutData.add(new HashMap());
            carOutAdapter.notifyDataSetChanged();
        }
        UI.showToast(this, result.msg);
    }

    private void initUi(boolean enable) {
        int[] tvs = {R.id.et_team, R.id.et_out_people, R.id.et_time_range};
        int[] ets = {R.id.et_total_fee, R.id.et_back_card, R.id.et_output_card, R.id.et_class_require, R.id.et_work_content, R.id.et_class_sumup, R.id.et_onduty_remark,};
        setEnable(tvs, enable);
        setEnable(ets, enable);
        carInAdapter = new OndutyCarRoadDetailAdapter(this, true, enable, carInData);
        carInList.setAdapter(carInAdapter);
        carInList.setNestedScrollingEnabled(false);

        carOutAdapter = new OndutyCarRoadDetailAdapter(this, false, enable, carOutData);
        carOutList.setAdapter(carOutAdapter);
        carOutList.setNestedScrollingEnabled(false);
    }

    private void setEnable(int[] views, boolean enable) {
        for (int vis : views) {
            View view = findViewById(vis);
            view.setEnabled(enable);
            if (enable) {
                view.setBackgroundResource(R.drawable.selector_solid_gray);
            } else {
                view.setBackgroundResource(R.color.white);
            }
        }
    }

    /**
     * 初始化列表选择信息
     */
    public void initData() {
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

    /**
     * 选择车道
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
     * 选择车道收费员
     */
    public void selectFeeMans(ViewHolder holder, String names, int position, Boolean isCarIn) {
        isCarInFeeMan = isCarIn;
        tempPos = position;
        showMListDialog(holder.itemView, feeMans.toArray(new String[0]),  TextUtils.isEmpty(names) ? new String[]{} : names.split(","));
    }

    /**
     * 新增车道值班信息
     */
    public void addCarLl(Boolean isCarIn) {
        if (isCarIn) {
            carInData.add(new HashMap());
            carInAdapter.notifyDataSetChanged();
        } else {
            carOutData.add(new HashMap());
            carOutAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除车道值班信息
     */
    public void removeCarLl(Boolean isCarIn, int position) {
        if (isCarIn) {
            carInData.remove(position);
            carInAdapter.notifyItemRemoved(position);
            carInAdapter.notifyItemRangeChanged(position, carInData.size() - position);
        } else {
            carOutData.remove(position);
            carOutAdapter.notifyItemRemoved(position);
            carOutAdapter.notifyItemRangeChanged(position, carOutData.size() - position);
        }
    }

    @Override
    public void listSelected(View view, int index) {
        switch (view.getId()) {
            case R.id.et_out_people:
                setEText(view.getId(), feeMans.get(index));
                outPeopleId = feeManIds.get(index);
                break;
            case R.id.et_team:
                setEText(view.getId(), teams[index]);
                break;
            case R.id.et_num:
                if (isCarInLane) {//入口车道选择
                    carInData.get(tempPos).put("laneName", carInRoads.get(index));
                    carInData.get(tempPos).put("laneId", carInRoadIds.get(index));
                    carInAdapter.notifyDataSetChanged();
                } else {
                    carOutData.get(tempPos).put("laneName", carOutRoads.get(index));
                    carOutData.get(tempPos).put("laneId", carOutRoadIds.get(index));
                    carOutAdapter.notifyDataSetChanged();
                }
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
        String names = sb.substring(0, sb.length() - 1);
        if (isCarInFeeMan) {//入口车道收费员选择
            carInData.get(tempPos).put("staffList", names);
            carInAdapter.notifyDataSetChanged();
        } else {
            carOutData.get(tempPos).put("staffList", names);
            carOutAdapter.notifyDataSetChanged();
        }
    }
}
