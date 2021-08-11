package com.zyf.util.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.zyf.device.BaseActivity;

public class MyDateDialog {

    private MyDatePickerDialog.OnDateSetListener onDateSetListener;
    private Context context;

    public MyDateDialog(Context context) {
        this.context = context;
    }

    public void setOnDateSetListener(
            MyDatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    public void show(String title, int year, int month, int day, boolean showday) {

        MyDatePickerDialog d = new MyDatePickerDialog(context, onDateSetListener, title, year, month, day);
        d.show();
        if (!showday) {
            int SDKVersion = Integer.parseInt(Build.VERSION.SDK);//获取系统版本
            DatePicker dp = findDatePicker((ViewGroup) d.getWindow().getDecorView());//设置弹出年月
            if (dp != null) {
                if (SDKVersion < 11) {
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
                if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }
    }

    public void show(String title, int year, int month, int day, long minDate, long maxDate, boolean showday) {

        MyDatePickerDialog d = new MyDatePickerDialog(context, onDateSetListener, title, year, month, day);
        d.getDatePicker().setMinDate(minDate);
        if (maxDate > 0)
            d.getDatePicker().setMaxDate(maxDate);
        d.show();
        if (!showday) {
            int SDKVersion = Integer.parseInt(Build.VERSION.SDK);//获取系统版本
            //DatePicker dp = findDatePicker((ViewGroup) d.getWindow().getDecorView());//设置弹出年月
            DatePicker dp = d.getDatePicker();
            if (dp != null) {
                if (SDKVersion < 11) {
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
                if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 只显示年或月的DatePicker
     *
     * @param year
     * @param month
     * @param day
     * @param showMonth
     * @param showDay
     */
    public void show(String title, int year, int month, int day, boolean showYear, boolean showMonth, boolean showDay) {

        Dialog d = new MyDatePickerDialog(context, onDateSetListener, title, year, month, day);
        d.show();
        if (!showMonth && !showDay) {//只显示年
            int SDKVersion = Integer.parseInt(Build.VERSION.SDK);//获取系统版本
            DatePicker dp = findDatePicker((ViewGroup) d.getWindow().getDecorView());//设置弹出年月
            if (dp != null) {
                if (SDKVersion < 11) {
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
                if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }
        if (!showYear && !showDay) {//只显示月
            int SDKVersion = Integer.parseInt(Build.VERSION.SDK);//获取系统版本
            DatePicker dp = findDatePicker((ViewGroup) d.getWindow().getDecorView());//设置弹出年月
            if (dp != null) {
                if (SDKVersion < 11) {
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
                    ((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
                if (SDKVersion > 14) {
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
                    ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 从当前Dialog中查找DatePicker子控件
     *
     * @param group
     * @return
     */
    private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
}
