package com.kunbo.xiwei.activity;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.adapter.StepAdapter;
import com.kunbo.xiwei.view.AMapLocationUtils;
import com.kunbo.xiwei.view.ListDialog;
import com.kunbo.xiwei.view.MListDialog;
import com.zyf.device.CamareAndCropActivity;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.OkHttpUtil;
import com.zyf.net.api.ApiService;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.util.JsonUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.xiweiapp.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyBaseActivity extends CamareAndCropActivity {
    public StepAdapter stepAdapter;
    @BindView(R.id.step_list)
    public RecyclerView recyclerStep;
    @BindView(R.id.step_befo_ll)
    LinearLayout stepBefo;
    @BindView(R.id.step_next_ll)
    LinearLayout stepNext;
    public MyData stepDatas = new MyData();//步骤数
    NestedScrollView[] views;//步骤录入页面布局
    private boolean showSubmit;

    public void setStep(Activity context, String[] stepTips, boolean showSubmit) {
        this.showSubmit = showSubmit;
        for (int i = 0; i < stepTips.length; i++) {
            MyRow ro = new MyRow();
            ro.put("stepTip", stepTips[i]);
            if (i == 0)
                ro.put("selected", true);
            else
                ro.put("selected", false);
            stepDatas.add(ro);
        }
        stepAdapter = new StepAdapter(this, stepDatas);
        recyclerStep.setLayoutManager(new GridLayoutManager(context, stepTips.length));
        recyclerStep.setAdapter(stepAdapter);
        views = new NestedScrollView[stepDatas.size()];
        for (int i = 0; i < stepDatas.size(); i++) {
            views[i] = (NestedScrollView) UI.getView(context, "step" + (i + 1) + "_ll", "id");
        }
    }

    public void setEText(TextView tv, String ss) {
        if (tv != null) {
            tv.setText(ss);
        }
    }

    /**
     * 和风天气查询实况天气
     *
     * @return
     */
    public void getWeather(TextView tvWeather) {
        requestWeather(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(Result result, String method) {
                setEText(tvWeather.getId(), (String) result.obj);
            }

            @Override
            public void onFault(Result result, String method) {
                UI.showToast(getApplicationContext(), "定位失败！");
            }
        });
    }

    public void requestWeather(OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        AMapLocation location = AMapLocationUtils.getInstance(getApplicationContext()).getLocation();
        if (location != null) {
            String url = "now?location=" + location.getLongitude() + "," + location.getLatitude() + "&key=" + BC.heFenKey;
            OkHttpUtil httpUtil = OkHttpUtil.getInstance();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://free-api.heweather.net/s6/weather/")
                    .client(httpUtil.getClient())
                    .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava
                    .build();
            retrofit.create(ApiService.class).getWeather(url)
                    .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retry(5)
                    .subscribe(new DisposableObserver<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                String res = responseBody.string();
                                if (!Utils.checkNullString(res)) {
                                    MyRow rowRes = JsonUtil.getRow(res);
                                    MyData data = (MyData) rowRes.get("HeWeather6");
//                                    MyRow basic = (MyRow) data.get(0).get("basic");//基础位置信息
                                    MyRow now = (MyRow) data.get(0).get("now");//实况天气
                                    if (now != null) {
                                        Result result = new Result();
                                        result.obj = now.getString("cond_txt");
                                        mOnSuccessAndFaultListener.onSuccess(result, "weather");
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            UI.showToast(getApplicationContext(), "网络异常，请检查网络设置！");
                            mOnSuccessAndFaultListener.onFault(null, "weather");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mOnSuccessAndFaultListener.onFault(null, "weather");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AMapLocationUtils.getInstance(getApplicationContext()).removeLocManager();
    }

    /**
     * 上一步下一步点击事件
     *
     * @param view
     */
    @OnClick({R.id.step_befo_ll, R.id.step_next_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.step_befo_ll://上一步
                switch (views.length) {//根据步骤数控制页面显示
                    case 2:
                        hide(R.id.submit);
                        hide(R.id.save);
                        views[0].setVisibility(View.VISIBLE);
                        views[1].setVisibility(View.GONE);
                        stepBefo.setVisibility(View.INVISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        stepDatas.get(1).put("selected", false);
                        stepAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        hide(R.id.submit);
                        hide(R.id.save);
                        if (views[1].isShown()) {
                            stepBefo.setVisibility(View.INVISIBLE);
                            views[0].setVisibility(View.VISIBLE);
                            views[1].setVisibility(View.GONE);
                            stepDatas.get(1).put("selected", false);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[2].isShown()) {
                            stepNext.setVisibility(View.VISIBLE);
                            views[1].setVisibility(View.VISIBLE);
                            views[2].setVisibility(View.GONE);
                            stepDatas.get(2).put("selected", false);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        break;
                    case 4:
                        hide(R.id.submit);
                        hide(R.id.save);
                        if (views[1].isShown()) {
                            stepBefo.setVisibility(View.INVISIBLE);
                            views[0].setVisibility(View.VISIBLE);
                            views[1].setVisibility(View.GONE);
                            stepDatas.get(1).put("selected", false);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[2].isShown()) {
                            views[1].setVisibility(View.VISIBLE);
                            views[2].setVisibility(View.GONE);
                            stepDatas.get(2).put("selected", false);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[3].isShown()) {
                            stepNext.setVisibility(View.VISIBLE);
                            views[2].setVisibility(View.VISIBLE);
                            views[3].setVisibility(View.GONE);
                            stepDatas.get(3).put("selected", false);
                            stepAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;
            case R.id.step_next_ll://下一步
                switch (views.length) {//根据步骤数控制页面显示
                    case 2:
                        if (showSubmit)
                            show(R.id.submit);
                        show(R.id.save);
                        views[0].setVisibility(View.GONE);
                        views[1].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.GONE);
                        stepDatas.get(1).put("selected", true);
                        stepAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        if (views[0].isShown()) {
                            stepBefo.setVisibility(View.VISIBLE);
                            views[0].setVisibility(View.GONE);
                            views[1].setVisibility(View.VISIBLE);
                            stepDatas.get(1).put("selected", true);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[1].isShown()) {
                            if (showSubmit)
                                show(R.id.submit);
                            show(R.id.save);
                            stepNext.setVisibility(View.GONE);
                            views[1].setVisibility(View.GONE);
                            views[2].setVisibility(View.VISIBLE);
                            stepDatas.get(2).put("selected", true);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        break;
                    case 4:
                        if (views[0].isShown()) {
                            stepBefo.setVisibility(View.VISIBLE);
                            views[0].setVisibility(View.GONE);
                            views[1].setVisibility(View.VISIBLE);
                            stepDatas.get(1).put("selected", true);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[1].isShown()) {
                            views[1].setVisibility(View.GONE);
                            views[2].setVisibility(View.VISIBLE);
                            stepDatas.get(2).put("selected", true);
                            stepAdapter.notifyDataSetChanged();
                            return;
                        }
                        if (views[2].isShown()) {
                            if (showSubmit)
                                show(R.id.submit);
                            show(R.id.save);
                            stepNext.setVisibility(View.GONE);
                            views[2].setVisibility(View.GONE);
                            views[3].setVisibility(View.VISIBLE);
                            stepDatas.get(3).put("selected", true);
                            stepAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                break;

        }
    }

    /**
     * 步骤点击事件
     *
     * @param view
     * @param o
     * @param position
     * @param total
     */
    @Override
    public void onStepItemClick(View view, Object o, int position, int total) {
        super.onStepItemClick(view, o, position, total);
        for (int i = 0; i < stepDatas.size(); i++) {
            stepDatas.get(i).put("selected", false);
        }
        for (int i = 0; i <= position; i++) {
            stepDatas.get(i).put("selected", true);
        }
        stepAdapter.notifyDataSetChanged();
        switch (total) {
            case 2://页面分两个步骤
                if (position == 0) {//点击步骤一
                    hide(R.id.submit);
                    hide(R.id.save);
                    views[0].setVisibility(View.VISIBLE);
                    views[1].setVisibility(View.GONE);
                    stepBefo.setVisibility(View.INVISIBLE);
                    stepNext.setVisibility(View.VISIBLE);
                    return;
                }
                if (position == 1) {//点击步骤二
                    if (showSubmit)
                        show(R.id.submit);
                    show(R.id.save);
                    views[0].setVisibility(View.GONE);
                    views[1].setVisibility(View.VISIBLE);
                    stepBefo.setVisibility(View.VISIBLE);
                    stepNext.setVisibility(View.GONE);
                }
                break;
            case 3://页面分三个步骤
                switch (position) {
                    case 0://点击步骤一
                        hide(R.id.submit);
                        hide(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[0].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.INVISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        break;
                    case 1://点击步骤二
                        hide(R.id.submit);
                        hide(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[1].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        break;
                    case 2://点击步骤三
                        if (showSubmit)
                            show(R.id.submit);
                        show(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[2].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.GONE);
                        break;
                }

                break;
            case 4://页面分四个步骤
                switch (position) {
                    case 0://点击步骤一
                        hide(R.id.submit);
                        hide(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[0].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.INVISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        break;
                    case 1://点击步骤二
                        hide(R.id.submit);
                        hide(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[1].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        break;
                    case 2://点击步骤三
                        hide(R.id.submit);
                        hide(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[2].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.VISIBLE);
                        break;
                    case 3://点击步骤四
                        if (showSubmit)
                            show(R.id.submit);
                        show(R.id.save);
                        for (View ve : views) {
                            ve.setVisibility(View.GONE);
                        }
                        views[3].setVisibility(View.VISIBLE);
                        stepBefo.setVisibility(View.VISIBLE);
                        stepNext.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    /**
     * 弹出多选列表
     *
     * @param view  触发控件
     * @param names 已选中的元素
     */
    public void showMListDialog(final View view, String[] datas, String[] names) {
        MListDialog dialog = new MListDialog(this);
        dialog.setOnSelectedListener((view1, selected1) -> mlistSelected(view1, selected1));
        dialog.show(view, datas, names, true);
    }

    /**
     * 多选列表回调方法，子类覆盖此方法
     *
     * @param view     触发控件
     * @param selected 选择后的值，长度与文本数组长度相同
     */
    public void mlistSelected(View view, boolean[] selected) {
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view  触发控件
     * @param datas 显示的数据
     */
    public void showMyListDialog(View view, String[] datas) {
        ListDialog dialog = new ListDialog(this);
        dialog.setOnSelectedListener((view1, index) -> listSelected(view1, index));
        dialog.show(view, datas);
    }

    /**
     * 列表选择的回调方法，子类覆盖此方法。
     *
     * @param view
     * @param index
     */
    public void listSelected(View view, int index) {
    }
}
