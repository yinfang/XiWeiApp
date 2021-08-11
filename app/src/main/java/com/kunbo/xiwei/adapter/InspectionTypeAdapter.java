package com.kunbo.xiwei.adapter;

import android.widget.RadioButton;
import android.widget.TextView;

import com.kunbo.xiwei.activity.agent.InspectionManageActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

/**
 * 稽查类型adapter
 */
public class InspectionTypeAdapter extends RecyclerCommonAdapter {
    private InspectionManageActivity context;

    public InspectionTypeAdapter(InspectionManageActivity context, MyData datas) {
        super(context, R.layout.item_inspection_type, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {

        MyRow row = (MyRow) o;
        RadioButton radio = holder.getView(R.id.radio);
        TextView type = holder.getView(R.id.type);

        boolean checked = row.getBoolean("selected");
        radio.setChecked(checked);
        type.setText(row.getString("type"));
        holder.getItemView().setOnClickListener(view->context.onItemClick(checked,position));

    }

}
