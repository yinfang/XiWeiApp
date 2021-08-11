package com.kunbo.xiwei.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kunbo.xiwei.AppApplication;
import com.kunbo.xiwei.BC;
import com.zyf.device.BaseActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * 出口查询入口记录图片
 */
public class OverHighOutQueryPhotoAdapter extends RecyclerCommonAdapter {
    private BaseActivity context;

    public OverHighOutQueryPhotoAdapter(BaseActivity context, List datas) {
        super(context, R.layout.item_photo, datas);
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

        ImageView itemImg = holder.getView(R.id.item_image);

        HashMap row = (HashMap) o;
        holder.setImage(itemImg, BC.BASE_URL_IMAGE + row.get("accessPath"), false, 5);
        holder.hide(R.id.remove_img);
        itemImg.setOnClickListener(v -> context.onDetailItemClickListener(holder.itemView, position));
    }
}