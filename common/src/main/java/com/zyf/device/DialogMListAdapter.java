package com.zyf.device;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zyf.common.R;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;

public class DialogMListAdapter extends RecyclerCommonAdapter {

    public DialogMListAdapter(Context a, MyData data) {
        super(a, R.layout.dialog_mlist_item, data);

    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        ImageView selecte = holder.getView(R.id.select);
        MyRow row = (MyRow) o;
        if (row.getBoolean("selecte")) {
            selecte.setBackgroundResource(R.drawable.checkbox_on);
        } else {
            selecte.setBackgroundResource(R.drawable.checkbox_off);
        }
        holder.setText(R.id.name, row.getString("name"));
    }
}
