package com.kunbo.xiwei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kunbo.xiwei.modle.User;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.Utils;
import com.zyf.util.image.GlideUtil;
import com.zyf.xiweiapp.R;

import java.util.Date;

/**
 * 值班表每日值班信息弹框
 */
public class OndutyInfoDialog {
    private Context context;
    private StringBuffer sbNames;
    private StringBuffer sbPhones;

    public OndutyInfoDialog(@NonNull Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void show(MyRow row) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.onduty_info_dialog, null);
        TextView title = v.findViewById(R.id.title);
        TextView stationNames = v.findViewById(R.id.station_names);
        TextView stationPhones = v.findViewById(R.id.station_phones);
        TextView ticketNames = v.findViewById(R.id.ticket_names);
        TextView ticketPhones = v.findViewById(R.id.ticket_phones);
        TextView monitorNames = v.findViewById(R.id.monitor_names);
        TextView monitorPhones = v.findViewById(R.id.monitor_phones);

        title.setText(User.getInstance().getStationName() + C.df_yM.format(new Date()) + "-" + row.getString("dutyDay") + "日值班信息");
        MyData stations = (MyData) row.get("siteAgent");
        getNamesAndPhones(stations);
        stationNames.setText( Html.fromHtml(sbNames.toString()));
        stationPhones.setText(Html.fromHtml(sbPhones.toString()));

        MyData tickets = (MyData) row.get("ticket");
        getNamesAndPhones(tickets);
        ticketNames.setText( Html.fromHtml(sbNames.toString()));
        ticketPhones.setText(Html.fromHtml(sbPhones.toString()));

        MyData monitors = (MyData) row.get("monitor");
        getNamesAndPhones(monitors);
        monitorNames.setText( Html.fromHtml(sbNames.toString()));
        monitorPhones.setText( Html.fromHtml(sbPhones.toString()));

        ad.setView(v);
        AlertDialog d = ad.create();
        d.show();
    }

    public void getNamesAndPhones(MyData data) {
        sbNames = new StringBuffer();
        sbPhones = new StringBuffer();
        if (data != null && data.size() > 0) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                if (data.get(i).getString("personId").equals(User.getInstance().getPersonId())) {//id为当前登录用户时标红
                    if (i == size - 1) {
                        sbNames.append("<font color=\"#ff0000\">" + data.get(i).getString("watchkeeperName") + "</font>");
                        sbPhones.append("<font color=\"#ff0000\">" + data.get(i).getString("watchkeeperPhone") + "</font>");
                    } else {
                        sbNames.append("<font color=\"#ff0000\">" + data.get(i).getString("watchkeeperName") + "</font>" + "<br>");
                        sbPhones.append("<font color=\"#ff0000\">" + data.get(i).getString("watchkeeperPhone") + "</font>" + "<br>");
                    }
                } else {
                    if (i == size - 1) {
                        sbNames.append(data.get(i).getString("watchkeeperName"));
                        sbPhones.append(data.get(i).getString("watchkeeperPhone"));
                    } else {
                        sbNames.append(data.get(i).getString("watchkeeperName") + "<br>");
                        sbPhones.append(data.get(i).getString("watchkeeperPhone") + "<br>");
                    }
                }
            }
        }
    }
}
