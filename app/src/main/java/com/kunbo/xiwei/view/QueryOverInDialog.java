package com.kunbo.xiwei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

/**
 * 查询超限车辆入口登记记录
 */
public class QueryOverInDialog {
    private Context context;
    private ItemSelectedListener onSelectedListener;

    public QueryOverInDialog(@NonNull Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void setItemSelectedListener(ItemSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    public interface ItemSelectedListener {
        void onItemSelected(MyRow row);
    }

    public void show(String resIdTitle, MyData data) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
//        ad.setTitle(resIdTitle);
        View v = View.inflate(context, R.layout.carin_query_dialog_list, null);
        TextView title = v.findViewById(R.id.title);
        title.setText(resIdTitle);
        RecyclerView lv = v.findViewById(R.id.list);
        lv.setLayoutManager(new LinearLayoutManager(context));
        QueryItemAdapter adapter = new QueryItemAdapter(context, data);
        lv.setAdapter(adapter);
        ad.setView(v);
        AlertDialog d = ad.create();
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        adapter.setOnItemClickListener(new MyListSelectedListener(d, data));
        d.show();
    }

    class MyListSelectedListener implements RecyclerCommonAdapter.OnItemClickListener {
        AlertDialog dialog;
        MyData data;

        MyListSelectedListener(AlertDialog dialog, MyData data) {
            this.dialog = dialog;
            this.data = data;
        }

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            if (onSelectedListener != null) {
                onSelectedListener.onItemSelected(data.get(position));
            }
            dialog.dismiss();
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    }

    public class QueryItemAdapter extends RecyclerCommonAdapter {


        QueryItemAdapter(Context context, MyData data) {
            super(context, R.layout.item_query_carin, data);
        }

        @Override
        protected void convert(ViewHolder holder, Object o, int position) {
            MyRow ro = (MyRow) o;
            holder.setText(R.id.date, "查验时间：" + ro.getString("siteExamineDate") + " " + ro.getString("siteExamineTime"));
            holder.setText(R.id.content1, "车牌号：" + ro.getString("basicCarLicencePlate"));
            holder.setText(R.id.content2, "入口站点：" + ro.getString("stationName"));
        }
    }

}
