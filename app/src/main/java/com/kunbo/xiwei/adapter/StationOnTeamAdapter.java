package com.kunbo.xiwei.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunbo.xiwei.activity.agent.StationOndutyActivity;
import com.kunbo.xiwei.modle.StationTeamEntity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

public class StationOnTeamAdapter extends RecyclerCommonAdapter {
    private StationOndutyActivity context;
    private List datas;

    public StationOnTeamAdapter(StationOndutyActivity context, List datas) {
        super(context, R.layout.item_station_team, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        Object entity = o;

        TextView team = holder.getView(R.id.tv_team);
        TextView timeRange = holder.getView(R.id.tv_range);
        TextView monitor = holder.getView(R.id.tv_monitor);
        EditText etNum = holder.getView(R.id.et_num);
        LinearLayout addLl = holder.getView(R.id.add_ll);
        LinearLayout removeLl = holder.getView(R.id.remove_ll);

        if (position == 0) {
            holder.hide(R.id.remove_image);
            holder.show(R.id.list_title_ll);
        } else {
            holder.show(R.id.remove_image);
            holder.hide(R.id.list_title_ll);
        }

        etNum.setTag(position);//绑定tag标记
        etNum.clearFocus();
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = (int) etNum.getTag();//根据tag位置绑定数据，避免数错乱
                ((StationTeamEntity) datas.get(position)).setNumber(s.toString());
            }
        });
        etNum.setText(((StationTeamEntity) datas.get(position)).getNumber());
        team.setText(((StationTeamEntity) entity).getGroupType());
        timeRange.setText(((StationTeamEntity) entity).getTimeRange());
        monitor.setText(((StationTeamEntity) entity).getMonitor());

        team.setOnClickListener(v -> {
            etNum.clearFocus();
            context.selectTeam(holder, position);
        });
        timeRange.setOnClickListener(v -> {
            etNum.clearFocus();
            context.selectTimeRange(timeRange, position);
        });
        monitor.setOnClickListener(v -> {
            etNum.clearFocus();
            context.selectMonitor(holder, position);
        });
        addLl.setOnClickListener(v -> {
            etNum.clearFocus();
            context.addTeamLl();
        });
        removeLl.setOnClickListener(v -> {
            etNum.clearFocus();
            context.removeTeamLl(position);
        });
    }

}
