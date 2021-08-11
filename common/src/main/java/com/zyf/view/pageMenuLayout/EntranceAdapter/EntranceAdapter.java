package com.zyf.view.pageMenuLayout.EntranceAdapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.zyf.common.R;
import com.zyf.device.BaseActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.view.recyclerview.ViewHolder;

/**
 * 分页菜单项功能按钮  适配器
 */
public class EntranceAdapter extends RecyclerCommonAdapter {
    private BaseActivity baseActivity;
    private Context context;
    private MyData mDatas;
    private int mPageSize, mIndex;

    public EntranceAdapter(Context context, int layoutId, MyData datas, int pageSize, int index) {
        super(context, layoutId, datas);
        mDatas = datas;
        mPageSize = pageSize;
        mIndex = index;
        this.context = context;
        this.baseActivity = (BaseActivity) context;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() > (mIndex + 1) * mPageSize ? mPageSize : (mDatas.size() - mIndex * mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPageSize;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position + mIndex * mPageSize;
        convert(holder, mDatas.get(pos), pos);
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        MyRow ro = (MyRow) o;
        LinearLayout.LayoutParams layoutParams = null;
        if(AppUtil.isTablet(context)){
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) UI.getScreenWidth(context) / 5.2f));
        }else{
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ((float) UI.getScreenWidth(context) / 3));
        }
        holder.getItemView().setLayoutParams(layoutParams);
        holder.setImage(R.id.bg, ro.get("bg"));
        holder.setImage(R.id.image, ro.get("path"), false);
        holder.setText(R.id.name, ro.getString("name"));
        holder.itemView.setOnClickListener(v -> baseActivity.onPageMenuItemClick(v, o, position));
    }
}
