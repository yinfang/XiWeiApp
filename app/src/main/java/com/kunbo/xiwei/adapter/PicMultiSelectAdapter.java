package com.kunbo.xiwei.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kunbo.xiwei.AppApplication;
import com.kunbo.xiwei.activity.PicMultiSelectActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

/**
 * 多图片选择
 */

public class PicMultiSelectAdapter extends RecyclerCommonAdapter {
    private PicMultiSelectActivity context;
    private MyData data;
    private boolean single = false;

    public PicMultiSelectAdapter(PicMultiSelectActivity context, MyData data) {
        super(context, R.layout.pic_multi_select_item, data);
        this.context = context;
        this.data = data;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        MyRow row = (MyRow) o;
        FrameLayout frameItem = holder.getView(R.id.frame_item);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) frameItem.getLayoutParams();
        if (!AppUtil.isTablet(AppApplication.getContext())) {//手机
            params.height = UI.getScreenWidth(context) / 6;
        } else {
            params.height = UI.getScreenWidth(context) / 4;
        }
        frameItem.setLayoutParams(params);
        holder.setImage(R.id.iv_item, row.getString("path"));
        ImageView ivShow = holder.getView(R.id.iv_show);
        if (single) {
            ivShow.setVisibility(View.GONE);
        } else {
            if (row.getBoolean("check")) {
                holder.setImage(R.id.iv_show, R.drawable.ic_selected);

            } else {
                holder.setImage(R.id.iv_show, R.drawable.ic_unselected);
            }
        }
        frameItem.setOnClickListener(v -> context.onItemClick(position));
    }

    public int getSelectedCount() {
        int selected = 0;
        for (int i = 0; i < data.size(); i++) {
            MyRow row = data.get(i);
            if (row != null && row.getBoolean("check")) {
                selected += 1;
            }
        }
        return selected;
    }
}
