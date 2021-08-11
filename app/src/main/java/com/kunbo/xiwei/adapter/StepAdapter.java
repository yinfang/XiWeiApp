package com.kunbo.xiwei.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.zyf.device.BaseActivity;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

public class StepAdapter extends RecyclerCommonAdapter {
    private Context context;
    private MyData datas;

    public StepAdapter(Context context, MyData datas) {
        super(context, R.layout.item_step, datas);
        this.datas = datas;
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        MyRow ro = (MyRow) o;
        LinearLayout ll = (LinearLayout) holder.getItemView();
        TextView tvStep = holder.getView(R.id.center_step);
        TextView tvStepTip = holder.getView(R.id.step_tip);
        View leftLine = holder.getView(R.id.left_line);
        View rightLine = holder.getView(R.id.right_line);

        holder.show(R.id.left_line);
        holder.show(R.id.right_line);
        if (position == 0) {
           leftLine.setVisibility(View.INVISIBLE);
        }
        if (position == datas.size() - 1) {
          rightLine.setVisibility(View.INVISIBLE);
        }

        tvStep.setText(position + 1 + "");
        tvStepTip.setText(ro.getString("stepTip"));
        if (ro.getBoolean("selected")) {
            leftLine.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.yarngreen,null));
            rightLine.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.yarngreen,null));
            tvStepTip.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.yarngreen,null));
            tvStep.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.white,null));
            tvStep.setBackgroundResource(R.drawable.cycle_green);
        } else {
            leftLine.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.gray_e6,null));
            rightLine.setBackgroundColor(ResourcesCompat.getColor(context.getResources(),R.color.gray_e6,null));
            tvStepTip.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.gray_hint_text,null));
            tvStep.setTextColor(ResourcesCompat.getColor(context.getResources(),R.color.gray_hint_text,null));
            tvStep.setBackgroundResource(R.drawable.cycle_gray);
        }
        ll.setOnClickListener(v -> ((BaseActivity) context).onStepItemClick(holder.itemView, holder, position,datas.size()));
    }

}
