package com.zyf.util.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zyf.common.R;
import com.zyf.device.DialogMListAdapter;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;

import java.util.Arrays;

public class MListDialog {
    private Context context;
    private MListDialog.OnSelectedListener onSelectedListener;

    public MListDialog(Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void setOnSelectedListener(MListDialog.OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @SuppressLint("InflateParams")
    public void show(final View view, int resIdTitle, String[] captions,
                     boolean selected[], boolean showAllSelecte) {
        final boolean[] oldSelected = Arrays.copyOf(selected, selected.length);
        if (captions != null && captions.length != selected.length) {
            throw new RuntimeException(
                    "MList dialog: defined selected number not match captions!");
        }
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        if (resIdTitle > 0) {
            ad.setTitle(resIdTitle);
        }
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_mlist, null);
        TextView sure = v.findViewById(R.id.sure);
        TextView cancel = v.findViewById(R.id.cancel);
        TextView selcteAll = v.findViewById(R.id.selcte_all);
        if (showAllSelecte) {
            selcteAll.setVisibility(View.VISIBLE);
        }
        MyData data = new MyData();
        for (int i = 0; i < captions.length; i++) {
            MyRow row = new MyRow();
            row.put("name", captions[i]);
            row.put("selecte", selected[i]);
            data.add(row);
        }

        DialogMListAdapter adapter = new DialogMListAdapter(context, data);
        RecyclerView lv = v.findViewById(R.id.list);
        lv.setLayoutManager(new LinearLayoutManager(context));
        lv.setAdapter(adapter);
        ad.setView(v);
        Dialog d = ad.create();
        adapter.setOnItemClickListener(new MListDialog.MyListSelectedListener(adapter, data, view, selected));
        d.show();
        sure.setOnClickListener(v1 -> {
            onSelectedListener.onSelected(v1, selected);
            d.dismiss();
        });
        cancel.setOnClickListener(v1 -> {
            for (int i = 0; i < oldSelected.length; i++) {
                data.get(i).put("selecte", oldSelected[i]);
            }
            d.dismiss();
        });
        selcteAll.setOnClickListener(v1 -> {
            boolean hasSelcted = true;
            for (int i = 0; i < selected.length; i++) {
                if (!selected[i]) {
                    hasSelcted = false;
                    break;
                }
            }
            for (int i = 0; i < data.size(); i++) {
                MyRow ro = data.get(i);
                if (!hasSelcted) {
                    ro.put("selecte", true);
                    selected[i] = true;
                } else {
                    ro.put("selecte", false);
                    selected[i] = false;
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    public interface OnSelectedListener {
        void onSelected(View view, boolean[] selected);
    }

    class MyListSelectedListener implements RecyclerCommonAdapter.OnItemClickListener {
        MyData data;
        View viewp;
        boolean[] selected;
        DialogMListAdapter adapter;

        MyListSelectedListener(DialogMListAdapter adapter, MyData data, View viewp, boolean[] selected) {
            this.data = data;
            this.viewp = viewp;
            this.selected = selected;
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            if (onSelectedListener != null) {
                MyRow ro = data.get(position);
                ro.put("selecte", !ro.getBoolean("selecte"));
                selected[position] = !selected[position];
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            return false;
        }
    }
}
