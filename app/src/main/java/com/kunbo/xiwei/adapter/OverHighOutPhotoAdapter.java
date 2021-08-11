package com.kunbo.xiwei.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kunbo.xiwei.AppApplication;
import com.kunbo.xiwei.activity.monitor.OverHighOutActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

/**
 * 出口处理结果图片
 */
public class OverHighOutPhotoAdapter extends RecyclerCommonAdapter {
    private OverHighOutActivity context;

    public OverHighOutPhotoAdapter(OverHighOutActivity context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        LinearLayout ll = (LinearLayout) holder.getItemView();
        int width;
        if (!AppUtil.isTablet(AppApplication.getContext())) {//手机
            width = UI.getScreenWidth(context) / 5;
        } else {
            width = UI.getScreenWidth(context) / 8;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        ll.setLayoutParams(layoutParams);

        ImageView removeImg = holder.getView(R.id.remove_img);
        ImageView itemImg = holder.getView(R.id.item_image);

        String path = o.toString();
        if (!Utils.checkNullString(path)) {
            holder.setImage(itemImg, path, false, 5);
            holder.show(R.id.remove_img);
        } else {
            holder.setImage(itemImg, R.drawable.ic_add_photo, false, 5);
            holder.hide(R.id.remove_img);
        }
        itemImg.setOnClickListener(v -> context.onItemClick(holder.itemView, holder, position));
        removeImg.setOnClickListener(v -> context.removePhoto(position));
    }
}