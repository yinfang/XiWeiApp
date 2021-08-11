package com.kunbo.xiwei.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;

import com.kunbo.xiwei.activity.monitor.OndutyFormActivity;
import com.kunbo.xiwei.modle.User;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

/**
 * 站值班表（每月）
 */
public class OndutyFormItemAdapter extends RecyclerCommonAdapter {
    private Context context;
    private StringBuffer sbNames;

    public OndutyFormItemAdapter(Context context, List datas) {
        super(context, R.layout.item_onduty_form, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        MyRow ro = (MyRow) o;
        LinearLayout itemLl = holder.getView(R.id.item_ll);
        if (ro != null && !ro.isEmpty()) {
            holder.show(R.id.item_ll);
            holder.hide(R.id.no_data);

            holder.setText(R.id.tv_day, ro.getString("dutyDay"));

            MyData stations = (MyData) ro.get("siteAgent");
            getNamesAndPhones(stations);
            holder.setText(R.id.tv_station, Html.fromHtml(sbNames.toString()));

           /* MyData tickets = (MyData) ro.get("ticket");
            getNamesAndPhones(tickets);
            holder.setText(R.id.tv_ticket, sbNames.toString());*/

            MyData monitors = (MyData) ro.get("monitor");
            getNamesAndPhones(monitors);
            holder.setText(R.id.tv_monitor, Html.fromHtml(sbNames.toString()));

            itemLl.setOnClickListener(v -> ((OndutyFormActivity) context).onItemClick(position));
        } else {
            holder.show(R.id.no_data);
            holder.hide(R.id.item_ll);
        }
    }

    private void getNamesAndPhones(MyData data) {
        sbNames = new StringBuffer();
        if (data != null && data.size() > 0) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                String name = data.get(i).getString("watchkeeperName");
                if (data.get(i).getString("personId").equals(User.getInstance().getPersonId())) {//id为当前登录用户时标红
                    if (i == size - 1)
                        sbNames.append("<font color=\"#ff0000\">" + name + "</font>");
                    else
                        sbNames.append("<font color=\"#ff0000\">" + name + "</font><br>");
                } else {
                    if (i == size - 1)
                        sbNames.append(name);
                    else
                        sbNames.append(name + "<br>");
                }
            }
        }
    }
}
