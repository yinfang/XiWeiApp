package com.zyf.util.dialog;

import android.app.Dialog;
import android.content.Context;

public class MyTimeDialog {

    private MyTimePickerDialog.OnTimeSetListener OnTimeSetListener;
    private Context context;

    public MyTimeDialog(Context context) {
        this.context = context;
    }

    public void setOnTimeSetListener(
            MyTimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.OnTimeSetListener = onTimeSetListener;
    }

    public void show(String title, int hour, int minute) {
        Dialog d = new MyTimePickerDialog(context, OnTimeSetListener, title, hour, minute,
                true);
        d.show();
    }

}
