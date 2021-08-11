package com.zyf.util.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.zyf.common.R;
import com.zyf.device.BaseActivity;
import com.zyf.device.DialogListAdapter;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListDialog {
    private Context context;
    private OnSelectedListener onSelectedListener;

    public ListDialog(Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @SuppressLint("InflateParams")
    public void show(final View view, int resIdTitle, String[] captions,
                     int[] images) {
        if (images != null && images.length != captions.length) {
            throw new RuntimeException(
                    "List dialog: defined images number not match captions!");
        }
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        if (resIdTitle > 0) {

            ad.setTitle(resIdTitle);
        }
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_list, null);
        MyData data = new MyData();
        for (int i = 0; i < captions.length; i++) {
            MyRow row = new MyRow();
            if (images != null) {
                row.put("image", images[i]);
            }
            row.put("name", captions[i]);
            data.add(row);
        }
        DialogListAdapter adapter = new DialogListAdapter(context, data,
                images != null);
        RecyclerView lv = v.findViewById(R.id.list);
        lv.setLayoutManager(new LinearLayoutManager(context));
        lv.setAdapter(adapter);
        ad.setView(v);
        Dialog d = ad.create();
        adapter.setOnItemClickListener(new MyListSelectedListener(d, view));
        d.show();
    }

    public interface OnSelectedListener {
        void onSelected(View view, int index);
    }

    class MyListSelectedListener implements RecyclerCommonAdapter.OnItemClickListener {
        Dialog dialog;
        View viewp;

        MyListSelectedListener(Dialog dialog, View viewp) {
            this.dialog = dialog;
            this.viewp = viewp;
        }

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(viewp, position);
            }
            dialog.dismiss();
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    }
}
