package com.kunbo.xiwei.activity.monitor;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.adapter.OndutyFormItemAdapter;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.OndutyInfoDialog;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.UI;
import com.zyf.xiweiapp.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zyf.net.api.ApiSubscribe.getOndutyList;

/**
 * 站值班表(每月)
 */
public class OndutyFormActivity extends BaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.refresh_recycler)
    RecyclerView refreshRecycler;
    private OndutyFormItemAdapter adapter;
    private String[] weekDays = {"一", "二", "三", "四", "五", "六", "日"};
    private String[] weekDays1 = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private MyData datas;//详情数据
    private Calendar calendar;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        setContentView(R.layout.activity_onduty_form);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //获取当前月第一天：
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String[] list = C.df_yM.format(calendar.getTime()).split("-");
        setEText(R.id.header_title, String.format(getString(R.string.onduty_form_title), User.getInstance().getStationName(), list[0], list[1]));
        datas = new MyData();
        date = C.df_yM.format(calendar.getTime());
        getList();
    }

    public void getList() {
        getOndutyList(User.getInstance().getStationId(), date, new OnSuccessAndFaultSub(this, "getOndutyList", this));
    }

    @Override
    public void onSuccess(Result result, String method) {
        String[] list = C.df_yM.format(calendar.getTime()).split("-");
        setEText(R.id.header_title, String.format(getString(R.string.onduty_form_title), User.getInstance().getStationName(), list[0], list[1]));
        MyRow da = (MyRow) result.obj;
        if (!da.isEmpty()) {
            datas = (MyData) da.get("details");
            if (datas != null && datas.size() > 0) {
                boolean isMon = datas.get(0).getString("dutyWeek").contains("一") || datas.get(0).getString("dutyWeek").contains("Mon");
                if (!isMon) {//1号不是星期一时用空对象补全表格
                    int weekDay = 1;//1号是周几
                    for (int i = 0; i < weekDays.length; i++) {
                        if (datas.get(0).getString("dutyWeek").contains(weekDays[i]) ||
                                datas.get(0).getString("dutyWeek").contains(weekDays1[i])) {
                            weekDay = i;
                            break;
                        }
                    }
                    for (int i = 0; i < weekDay; i++) {
                        MyRow row = new MyRow();
                        datas.add(i, row);
                    }
                }
                adapter = new OndutyFormItemAdapter(this, datas);
                refreshRecycler.setAdapter(adapter);
                refreshRecycler.setNestedScrollingEnabled(false);
                refreshRecycler.setNestedScrollingEnabled(false);
                setEText(R.id.tv_remark, da.getString("remark"));
                hide(R.id.img_no_data);
                show(R.id.tv_remark);
                show(R.id.week_ll);
                show(R.id.list_ll);
            } else {
                hide(R.id.week_ll);
                hide(R.id.list_ll);
                hide(R.id.tv_remark);
                show(R.id.img_no_data);
            }
        } else {
            hide(R.id.week_ll);
            hide(R.id.list_ll);
            hide(R.id.tv_remark);
            show(R.id.img_no_data);
        }
    }

    @Override
    public void onFault(Result result, String method) {
        super.onFault(result, method);
        UI.showToast(this, result.msg);
    }

    @OnClick({R.id.tv_before, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_before://上月
                calendar = Calendar.getInstance();
                calendar.setTime(stringToDate());
                calendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_next://下月
                calendar = Calendar.getInstance();
                calendar.setTime(stringToDate());
                calendar.add(Calendar.MONTH, 1);
                break;
        }
        if (datas != null) {
            datas.clear();
        }
        date = C.df_yM.format(calendar.getTime());
        getList();
    }

    public Date stringToDate() {
        try {
            return C.df_yM.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onItemClick(int position) {
        OndutyInfoDialog dialog = new OndutyInfoDialog(this);
        dialog.show(datas.get(position));
    }

}
