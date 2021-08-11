package com.kunbo.xiwei.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.adapter.HistoryItemAdapter;
import com.kunbo.xiwei.modle.User;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.UI;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.xiweiapp.R;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prototypez.savestate.core.annotation.AutoRestore;

import static com.zyf.net.api.ApiSubscribe.getMonitorHistoryList;
import static com.zyf.net.api.ApiSubscribe.getSpecialHistoryList;
import static com.zyf.net.api.ApiSubscribe.getStationHistoryList;

/**
 * 历史记录
 * 上拉加载,下拉刷新
 */
public class HistoryActivity extends BaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.refresh_recycler)
    RecyclerView refreshRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private HistoryItemAdapter adapter;
    MyData allData, datas;//data详情数据，datas精简后的列表数据
    private CustomDatePickerDialog mTimerPicker;
    private int page = 1;
    private boolean isRefresh = false;
    @AutoRestore
    String from;
    @AutoRestore
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            from = b.getString("from");
        }
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        setEText(R.id.et_start_date, C.df_yMd.format(c.getTime()));
        setEText(R.id.et_end_date, C.df_yMd.format(new Date()));
        allData = new MyData();
        datas = new MyData();
        adapter = new HistoryItemAdapter(this, datas);
        refreshRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (datas != null) {
            datas.clear();
        }
        getList(BC.PAGE_INDEX);
    }

    public void getList(int page) {
        switch (from) {
            case "stationOnduty":
                title = "站长值班记录详情";
                if (isRefresh || page > 1)
                    getStationHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getStationHistoryList", this, false));
                else
                    getStationHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getStationHistoryList", this));
                break;
            case "classOnduty":
                title = "班长值班记录详情";
                if (isRefresh || page > 1)
                    getMonitorHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getMonitorHistoryList", this, false));
                else
                    getMonitorHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getMonitorHistoryList", this));
                break;
            case "special":
                title = "特情记录详情";
                if (isRefresh || page > 1)
                    getSpecialHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getSpecialHistoryList", this, false));
                else
                    getSpecialHistoryList(String.valueOf(page), String.valueOf(BC.PAGE_SIZE), User.getInstance().getStationId(), User.getInstance().getPersonId(), getEText(R.id.et_start_date), getEText(R.id.et_end_date), new OnSuccessAndFaultSub(this, "getSpecialHistoryList", this));
                break;
            case "civilized":
                title = "文明服务记录详情";

                break;
            case "complaint":
                title = "投诉管理记录详情";

                break;
        }
    }

    @Override
    public void onSuccess(Result result, String method) {
        MyRow r = (MyRow) result.obj;
        MyData data = (MyData) r.get("records");
        if (data != null && data.size() > 0) {
            for (MyRow ro : data) {
                allData.add(ro);
            }
            switch (from) {
                case "stationOnduty":
                    for (MyRow row : data) {
                        MyRow ro = new MyRow();
                        ro.put("title", "值班时间：" + row.getString("dutyDate"));
                        ro.put("content1", row.getString("weekDay"));
                        ro.put("content2", "征费额：" + row.getString("totalFee"));
                        datas.add(ro);
                    }
                    break;
                case "classOnduty":
                    for (MyRow row : data) {
                        MyRow ro = new MyRow();
                        ro.put("title", "值班时间：" + row.getString("dutyDate"));
                        ro.put("content1", row.getString("weekDay"));
                        ro.put("content2", "收费额：" + row.getString("draftedBy"));
                        datas.add(ro);
                    }
                    break;
                case "special":
                    for (MyRow row : data) {
                        MyRow ro = new MyRow();
                        ro.put("title", "车牌号：" + row.getString("secretPlateNumber"));
                        ro.put("content1", "特情类型：" + row.getString("secretClassifyName"));
                        ro.put("content2", "收费员：" + row.getString("secretTollName") + "\t\t提交时间：" + row.getString("secretDate") + "\t" + row.getString("secretTime"));
                        datas.add(ro);
                    }
                    break;
                case "civilized":

                    break;
                case "complaint":

                    break;
            }
            show(R.id.refresh_recycler);
            hide(R.id.no_data);
        } else {
            if (page == 1) {
                show(R.id.no_data);
                hide(R.id.refresh_recycler);
            }
        }
        adapter.notifyDataSetChanged();
        if (isRefresh) {
            refreshLayout.finishRefresh();
            isRefresh = false;
        }
        if (page > 1) {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    @OnClick({R.id.et_start_date, R.id.et_end_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_start_date:
                initTimerPicker();
                mTimerPicker.show(view, "", C.df_yMd.format(new Date()));
                break;
            case R.id.et_end_date:
                initTimerPicker();
                mTimerPicker.show(view, "", C.df_yMd.format(new Date()));
                break;
        }
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker() {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
            if (datas != null) {
                datas.clear();
            }
            mTimerPicker.dismissDialog();
            getList(BC.PAGE_INDEX);
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时分
        mTimerPicker.setCanShowDateTime(true, false, false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (datas != null) {
                datas.clear();
            }
            isRefresh = true;
            page = BC.PAGE_INDEX;
            getList(BC.PAGE_INDEX);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            if (page > 1) {
                getList(page);
            }
        });
    }

    public void onItemClick(int position) {
        Bundle extras = new Bundle();
        extras.putString("from", from);
        extras.putString("data", new Gson().toJson(allData.get(position)));
        openIntent(HistoryDetailActivity.class, title, extras);
    }
}
