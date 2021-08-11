package com.kunbo.xiwei.adapter;

import android.content.Context;

import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.util.AppUtil;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.HashMap;
import java.util.List;

public class StationDetailOnTeamAdapter extends RecyclerCommonAdapter {
    private Context context;

    public StationDetailOnTeamAdapter(Context context, List datas) {
        super(context, R.layout.item_station_team_detail, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        HashMap row = (HashMap) o;
        if (AppUtil.isTablet(context)) {
            if (position == 0) {
                holder.show(R.id.title_ll);
            } else {
                holder.hide(R.id.title_ll);
            }
        }
        holder.setText(R.id.tv_team, row.get("groupType") + "");
        holder.setText(R.id.tv_range, row.get("timeRange") + "");
        holder.setText(R.id.tv_monitor, row.get("monitorName") + "");
        holder.setText(R.id.et_num, row.get("number") + "");
    }
}
