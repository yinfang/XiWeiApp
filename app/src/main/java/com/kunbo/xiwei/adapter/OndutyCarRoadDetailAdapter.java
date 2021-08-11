package com.kunbo.xiwei.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunbo.xiwei.activity.HistoryDetailActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.util.Utils;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * 班长记录详情页车道信息
 */
public class OndutyCarRoadDetailAdapter extends RecyclerCommonAdapter {
    private Context context;
    private Boolean isCarIn, canEdit;

    public OndutyCarRoadDetailAdapter(Context context, boolean isCarIn, boolean canEdit, List datas) {
        super(context, R.layout.item_car_inout_detail, datas);
        this.isCarIn = isCarIn;
        this.canEdit = canEdit;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        LinearLayout removeLl = holder.getView(R.id.remove_ll);
        LinearLayout addLl = holder.getView(R.id.add_ll);
        if (position == 0) {
            TextView title = holder.getView(R.id.list_title);
            if (isCarIn)
                title.setText("入口车道");
            else
                title.setText("出口车道");
            holder.show(R.id.list_title_ll);
            if (canEdit) {//可编辑
                addLl.setVisibility(View.VISIBLE);
                removeLl.setVisibility(View.VISIBLE);
                holder.getView(R.id.remove_image).setVisibility(View.INVISIBLE);
            } else {
                addLl.setVisibility(View.GONE);
                removeLl.setVisibility(View.GONE);
                holder.getView(R.id.remove_image).setVisibility(View.VISIBLE);
            }
            removeLl.setEnabled(false);
        } else {
            holder.hide(R.id.list_title_ll);
            if (canEdit) {//可编辑
                removeLl.setVisibility(View.VISIBLE);
                removeLl.setEnabled(true);
            } else {
                removeLl.setVisibility(View.GONE);
                removeLl.setEnabled(false);
            }
        }
        TextView etNum = holder.getView(R.id.et_num);
        TextView timeRange = holder.getView(R.id.tv_time_range);
        TextView feeMans = holder.getView(R.id.tv_fee_mans);

        HashMap map = (HashMap) o;
        etNum.setText(Utils.checkNullString(map.get("laneName") + "") ? "" : map.get("laneName") + "");
        //timeRange.setText(Utils.checkNullString(map.get("timeRange") + "") ? "" : map.get("timeRange") + "");
        feeMans.setText(Utils.checkNullString(map.get("staffList") + "") ? "" : map.get("staffList") + "");

        if (canEdit) {//可编辑
            etNum.setOnClickListener(v -> ((HistoryDetailActivity) context).selectLane(etNum, position, isCarIn));
            //timeRange.setOnClickListener(v -> ((HistoryDetailActivity) context).selectTimeRange(timeRange, position, isCarIn));
            feeMans.setOnClickListener(v -> ((HistoryDetailActivity) context).selectFeeMans(holder, feeMans.getText().toString(),position, isCarIn));

            addLl.setOnClickListener(v -> ((HistoryDetailActivity) context).addCarLl(isCarIn));
            removeLl.setOnClickListener(v -> ((HistoryDetailActivity) context).removeCarLl(isCarIn, position));
        } else {
            etNum.setClickable(false);
            //timeRange.setClickable(false);
            feeMans.setClickable(false);
        }
    }

}
