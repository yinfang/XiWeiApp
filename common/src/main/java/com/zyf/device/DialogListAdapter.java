package com.zyf.device;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zyf.common.R;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;

public class DialogListAdapter extends RecyclerCommonAdapter {
    boolean showImage;

    public DialogListAdapter(Context a, MyData data, boolean showImage) {
        super(a, R.layout.dialog_list_item, data);
        this.showImage = showImage;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        ImageView iv = holder.getView(R.id.imageView1);
        MyRow row = (MyRow) o;
        if (showImage) {
            int image = row.getInt("image");
            if (image > 0) {
                iv.setImageResource(image);
                iv.setVisibility(View.VISIBLE);
            } else {
                iv.setVisibility(View.INVISIBLE);
            }
        } else {
            iv.setVisibility(View.GONE);
        }
        holder.setText(R.id.name, row.getString("name"));
    }
}
