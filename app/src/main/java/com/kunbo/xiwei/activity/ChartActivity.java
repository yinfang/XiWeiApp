package com.kunbo.xiwei.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.kunbo.xiwei.modle.User;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.dialog.CustomDatePickerDialog;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zyf.net.api.ApiSubscribe.getChartData;

/**
 * 统计图表
 */
public class ChartActivity extends BaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.line_chart)
    LineChart lineChart;
    @BindView(R.id.bar_chart)
    BarChart barChart;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    private String from;
    private MyData datas;
    private int[] methods = new int[]{1, 2, 3};
    private int method = 2;
    private CustomDatePickerDialog mTimerPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        from = bundle.getString("from");
        setEText(R.id.tv_date, C.df_yM.format(new Date()));
        datas = new MyData();
        //TODO 测试数据
        initLineChart(lineChart, datas);
        initBarChart(barChart, datas);
        initPieChart(pieChart, datas);
//        getList();
    }

    private void getList() {
        if (from.equals("feeNum"))//收费额、车流量
            getChartData(User.getInstance().getStationId(), getEText(R.id.tv_date), new OnSuccessAndFaultSub(this, "getChartData", this));
        else if (from.equals("overHigh"))//治超检测数据
            getChartData(User.getInstance().getStationId(), getEText(R.id.tv_date), new OnSuccessAndFaultSub(this, "getChartData", this));
    }

    @OnClick({R.id.tv_method, R.id.tv_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_method:
                showListDialog(view, getResources().getStringArray(R.array.query_method));
                break;
            case R.id.tv_date:
                if (method == 1) {//按天
                    initTimerPicker(true, true);
                    mTimerPicker.show(view, "", C.df_yMd.format(new Date()));
                } else if (method == 2) {//按月
                    initTimerPicker(true, false);
                    mTimerPicker.show(view, "", C.df_yM.format(new Date()));
                } else {//按年
                    initTimerPicker(false, false);
                    mTimerPicker.show(view, "", C.df_y.format(new Date()));
                }
                break;
        }
    }

    @Override
    public void listSelected(View view, int index) {
        super.listSelected(view, index);
        setEText(view.getId(), getResources().getStringArray(R.array.query_method)[index]);
        method = methods[index];
        if (method == 1) {//按天
            setEText(R.id.tv_date, C.df_yMd.format(new Date()));
        } else if (method == 2) {//按月
            setEText(R.id.tv_date, C.df_yM.format(new Date()));
        } else {//按年
            setEText(R.id.tv_date, C.df_y.format(new Date()));
        }
        getList();
    }

    /**
     * 初始化时间日期选择器
     */
    private void initTimerPicker(boolean showMonth, boolean showDay) {
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePickerDialog(this, (v, timestamp) -> {
            if (method == 1) {//按天
                setEText(v.getId(), C.df_yMd.format(new Date(timestamp)));
            } else if (method == 2) {//按月
                setEText(v.getId(), C.df_yM.format(new Date(timestamp)));
            } else {
                setEText(v.getId(), C.df_y.format(new Date(timestamp)));
            }
            mTimerPicker.dismissDialog();
            getList();
        }, "", "");
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示年月日时分
        mTimerPicker.setCanShowMonthDay(showMonth, showDay);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    public void initLineChart(LineChart lineChart, MyData datas) {
        //TODO test
        for (int i = 1; i < 25; i++) {
            MyRow row = new MyRow();
            row.put("CheckinNum", new Random().nextInt(100));
            row.put("CheckinHour", i);
            datas.add(row);
        }

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            values.add(new Entry(i, datas.get(i).getInt("CheckinNum")));
        }
        LineDataSet set1 = new LineDataSet(values, "车流量");
        set1.setColor(getResources().getColor(R.color.title_blue));
        set1.setCircleColor(getResources().getColor(R.color.title_blue));
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(12f);
        set1.setFillColor(getResources().getColor(R.color.light_green));
        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);//设置显示为曲线还是折线

//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1);
        LineData data = new LineData(set1);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);//设置每个点上面显示的字符的样式
            }
        });
        lineChart.setData(data);
        lineChart.setNoDataText("暂无数据");   // 没有数据时样式
        lineChart.setDrawGridBackground(false);
//        lineChart.setVisibleXRange(6, 6);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.getDescription().setEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setGridColor(getResources().getColor(R.color.white));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(datas.size(), false);
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return C.df_Hm.format(new Date(millis));
            }
        });
        YAxis leftAxis = lineChart.getAxisLeft();//y轴独有
        leftAxis.setGridColor(getResources().getColor(R.color.white));
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setGranularity(1f);//设置轴值之间最小间隔1
        leftAxis.setAxisMinimum(0f);//为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        leftAxis.setTextSize(11f);
        leftAxis.setYOffset(-9f);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴的数值显示在外侧

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);//不显示右侧线

        setLegend(lineChart);
        lineChart.postInvalidate();
    }

    void initBarChart(BarChart barChart, final MyData datas) {
        //TODO test
        ArrayList<BarEntry> yValues1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            yValues1.add(new BarEntry(i,new Random().nextInt(100)));
        }
        ArrayList<BarEntry> yValues2 = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            yValues2.add(new BarEntry(i, new Random().nextInt(500)));
        }
        BarDataSet set1 = new BarDataSet(yValues1, "车流量");
        set1.setColor( getResources().getColor(R.color.my_orange));
        BarDataSet set2 = new BarDataSet(yValues2, "收费额");
        set2.setColor( getResources().getColor(R.color.light_green));
        final BarData data = new BarData(set1,set2);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);//设置每个柱子上面显示的字符的样式
            }
        });
        data.setBarWidth(0.3f);
        data.setValueTextSize(12f);
        barChart.setData(data);
        barChart.setNoDataText("暂无数据");
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);//点击没阴影
        barChart.setFitBars(false);     //设置X轴范围两侧柱形条是否显示一半
        barChart.groupBars(0, 0.08f, 0.03f);//组柱状图设置，组间距，柱状图间距
//        barChart.setVisibleXRange(4, 6);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.GRAY);
        xAxis.setDrawLabels(true);//是否显示X坐标轴上的刻度，默认是true, x坐标轴默认显示的是1,2,3,4,5.....
        xAxis.setLabelCount(10, false);//第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        xAxis.setDrawAxisLine(true);
        xAxis.setTextSize(11f);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setGridColor(Color.BLACK);
        //xAxis.setCenterAxisLabels(true);//设置居中 组类型数据有效

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisLineColor(Color.GRAY);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextSize(11f);
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);//为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//y轴的数值显示在外侧
//        leftAxis.setLabelCount(6, true);//第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        setLegend(barChart);
        barChart.postInvalidate();
    }


    public void initPieChart(PieChart pieChart, MyData datas) {//type 0全部场馆  1 分场馆
        String[] parties = new String[]{
                "Party A", "Party B", "Party C", "Party D", "Party E"};
        int[] colors = colors = new int[]{getResources().getColor(R.color.green),
                getResources().getColor(R.color.my_orange),
                getResources().getColor(R.color.light_green),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.blue_cb)};
        //饼状图数据
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 100) + 100 / 5), parties[i % parties.length], i));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(7f);//设置饼状Item被选中时变化的距离
        dataSet.setValueTextSize(12);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setDrawValues(false);//不显示文字
        pieChart.setData(data);
        pieChart.setNoDataText("暂无数据");
        pieChart.setDrawHoleEnabled(true);  //是否显示PieChart内部圆环(true:下面属性才有意义)
        pieChart.setHoleRadius(58f); //设置PieChart内部圆的半径(这里设置28.0f)
        pieChart.setTransparentCircleRadius(61f);//设置PieChart内部透明圆的半径(这里设置31.0f)
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setDrawEntryLabels(true);//显示图例方框
        pieChart.setRotation(0);//设置绘制的起始角度　Y轴正方向是0度
        pieChart.setRotationEnabled(true);//设置pieChart图表是否可以手动旋转
        pieChart.animateY(1000, Easing.EaseInOutQuad); //设置动画
        pieChart.setUsePercentValues(true);//显示为百分数
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("收费额、车流量示意图");
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.getDescription().setEnabled(false);

        setLegend(pieChart);
        pieChart.invalidate();
    }

    public void setLegend(Chart chart) {
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }
}
