package com.kunbo.xiwei.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kunbo.xiwei.activity.monitor.SecurityCheckActivity;
import com.kunbo.xiwei.modle.SecurityItem;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.List;

/**
 * 安全巡检 adapter
 */
public class SecurityCheckAdapter extends RecyclerCommonAdapter {
    private SecurityCheckActivity context;
    private List datas;

    public SecurityCheckAdapter(SecurityCheckActivity context, List datas) {
        super(context, R.layout.item_security_check, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        SecurityItem entity = (SecurityItem) o;
        LinearLayout selecteLl = holder.getView(R.id.select_ll);
        ImageView selecte = holder.getView(R.id.select);
        TextView tvDateTime = holder.getView(R.id.tv_datetime);
        TextView tvRange = holder.getView(R.id.tv_range);
        TextView tvLocation = holder.getView(R.id.tv_location);
        TextView tvPosition = holder.getView(R.id.tv_position);
        TextView tvItem = holder.getView(R.id.tv_item);
        TextView tvContent = holder.getView(R.id.tv_content);
        TextView tvState = holder.getView(R.id.tv_state);
        TextView tvChecker = holder.getView(R.id.tv_checker);
        EditText etRemark = holder.getView(R.id.et_remark);

        if (entity.isSelecte()) {
            selecte.setBackgroundResource(R.drawable.checkbox_on);
        } else {
            selecte.setBackgroundResource(R.drawable.checkbox_off);
        }
        tvRange.setText(entity.getExaminePeriodDesc());
        tvDateTime.setText(TextUtils.isEmpty(entity.getExamineTime()) ? "" : entity.getExamineTime());
        tvLocation.setText(entity.getExamineSite());
        tvPosition.setText(entity.getExamineLocation());
        tvItem.setText(entity.getExamineProject());
        tvContent.setText(entity.getExamineContext());
        tvState.setText(entity.getState());
        tvChecker.setText(entity.getChecker());

        etRemark.setTag(position);//绑定tag标记
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int position = (int) etRemark.getTag();//根据tag位置绑定数据，避免错乱
                ((SecurityItem) datas.get(position)).setRemark(s.toString());
            }
        });

        if (entity.getState().equals("异常") || entity.getState().equals("破损")) {
            holder.show(R.id.remark_ll);
            etRemark.setText(((SecurityItem) datas.get(position)).getRemark());
        } else {
            holder.hide(R.id.remark_ll);
            etRemark.setText("");
        }

        selecteLl.setOnClickListener(v -> {
            etRemark.clearFocus();
            context.selectItem(position);
        });
        tvDateTime.setOnClickListener(v -> {
            etRemark.clearFocus();
            context.selectDateTime(tvRange, position);
        });
        tvState.setOnClickListener(v -> {
            etRemark.clearFocus();
            context.selectState(tvState, position);
        });
        tvChecker.setOnClickListener(v -> {
            etRemark.clearFocus();
            context.selectChecker(tvChecker, position);
        });
    }

}
