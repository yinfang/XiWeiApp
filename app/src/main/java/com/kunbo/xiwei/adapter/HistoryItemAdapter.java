package com.kunbo.xiwei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.kunbo.xiwei.activity.HistoryActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

public class HistoryItemAdapter extends RecyclerCommonAdapter {
    private Context context;

    public HistoryItemAdapter(Context context, List datas) {
        super(context, R.layout.item_history, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        MyRow ro = (MyRow) o;
        LinearLayout content = holder.getView(R.id.item_ll);
        holder.setText(R.id.title, ro.getString("title"));
        holder.setText(R.id.content1, ro.getString("content1"));
        holder.setText(R.id.content2, ro.getString("content2"));
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HistoryActivity) context).onItemClick(position);
            }
        });
    }
}
