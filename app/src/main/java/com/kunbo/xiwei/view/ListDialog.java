package com.kunbo.xiwei.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.zyf.device.DialogListAdapter;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.UI;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 带模糊搜索框的单选列表选择框
 */
public class ListDialog {
    private AutoCompleteTextView autoCompleteTextView;
    private Context context;
    private OnSelectedListener onSelectedListener;
    private MyData data;

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
    public void show(final View view, String[] datas) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_slist, null);
        autoCompleteTextView = v.findViewById(R.id.et_search_name);
        data = new MyData();
        for (int i = 0; i < datas.length; i++) {
            MyRow row = new MyRow();
            row.put("name", datas[i]);
            data.add(row);
        }

        autoCompleteTextView.addTextChangedListener(watcher);
        DialogListAdapter adapter = new DialogListAdapter(context, data, false);
        RecyclerView lv = v.findViewById(R.id.list);
        lv.setLayoutManager(new LinearLayoutManager(context));
        lv.setAdapter(adapter);
        ad.setView(v);
        Dialog d = ad.create();
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            //选中搜索项
            String name = ((TextView) view1).getText().toString();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getString("name").equals(name)) {
                    if (onSelectedListener != null) {
                        onSelectedListener.onSelected(view, i);
                    }
                    d.dismiss();
                    break;
                }
            }
        });
        adapter.setOnItemClickListener(new MyListSelectedListener(d, view));
        d.show();
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getSearchDatas(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 模糊搜索收费员
     *
     * @param str
     * @return
     */
    public void getSearchDatas(String str) {
        if (!TextUtils.isEmpty(str)) {
            List<String> searchNames = new ArrayList<>();
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    if ((data.get(i)).getString("name").contains(str)) {
                        searchNames.add(data.get(i).getString("name"));
                    }
                }
                if (searchNames.size() > 0) {
                    ArrayAdapter searchAdapter = new ArrayAdapter(context, R.layout.item_autocomplete_text, searchNames.toArray(new String[0]));
                    autoCompleteTextView.setAdapter(null);
                    autoCompleteTextView.setAdapter(searchAdapter);
                    autoCompleteTextView.showDropDown();
                } else {
                    UI.showToast(context, "未搜索到该收费员！");
                }
            }
        }
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

    class ArrayAdapter extends android.widget.ArrayAdapter<String> implements Filterable {//重写getFilter，避免模糊搜索 中间项 不显示下拉框
        private String[] listResult;

        public ArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
            this.listResult = objects;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Assign the data to the FilterResults
                        filterResults.values = listResult;
                        filterResults.count = listResult.length;
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
