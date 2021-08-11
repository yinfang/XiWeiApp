package com.kunbo.xiwei.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunbo.xiwei.activity.monitor.ClassOndutyActivity;
import com.kunbo.xiwei.modle.CarInOutEntity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

public class OndutyCarInRoadAdapter extends RecyclerCommonAdapter {
    private ClassOndutyActivity context;
    private Boolean isCarIn = true;
    private List datas;

    public OndutyCarInRoadAdapter(ClassOndutyActivity context, List datas) {
        super(context, R.layout.item_car_inout, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        LinearLayout removeLl = holder.getView(R.id.remove_ll);
        if (position == 0) {
            TextView title = holder.getView(R.id.list_title);
            title.setText("入口车道");
            holder.show(R.id.list_title_ll);
            holder.getView(R.id.remove_image).setVisibility(View.INVISIBLE);
            removeLl.setEnabled(false);
        } else {
            holder.hide(R.id.list_title_ll);
            holder.getView(R.id.remove_image).setVisibility(View.VISIBLE);
            removeLl.setEnabled(true);
        }
        Object entity = o;

        TextView etNum = holder.getView(R.id.et_num);
        TextView timeRange = holder.getView(R.id.tv_time_range);
        TextView feeMans = holder.getView(R.id.tv_fee_mans);
        LinearLayout addLl = holder.getView(R.id.add_ll);

        etNum.setText(((CarInOutEntity) entity).getLane());
        feeMans.setText(((CarInOutEntity) entity).getFeeMans());
        //timeRange.setText(((CarInOutEntity) entity).getTimeRange());

        etNum.setOnClickListener(v -> context.selectLane(etNum, position, isCarIn));
        //timeRange.setOnClickListener(v -> context.selectTimeRange(timeRange, position, isCarIn));
        feeMans.setOnClickListener(v -> context.selectFeeMans(holder, feeMans.getText().toString(),position, isCarIn));

        addLl.setOnClickListener(v -> context.addCarInLl());
        removeLl.setOnClickListener(v -> context.removeCarInLl(position));
    }

}
