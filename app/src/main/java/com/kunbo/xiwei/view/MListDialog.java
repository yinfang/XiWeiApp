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
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.zyf.device.BaseActivity;
import com.zyf.device.DialogMListAdapter;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.UI;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 带模糊搜索框的多选列表选择框
 */
public class MListDialog implements View.OnClickListener {
    private AutoCompleteTextView autoCompleteTextView;
    private TextView sure, cancel, selcteAll;
    private Context context;
    private Dialog d;
    private MyData data = new MyData();
    private MListDialog.OnSelectedListener onSelectedListener;
    boolean[] oldSelected = new boolean[data.size()]; //原始选择框数值集合
    boolean[] selected = new boolean[data.size()]; //出入口车道收费员是否选中临时记录
    private DialogMListAdapter adapter;

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
    public void show(final View view, String[] datas, String[] names, boolean showAllSelecte) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_mlist, null);
        autoCompleteTextView = v.findViewById(R.id.et_search_name);
        sure = v.findViewById(R.id.sure);
        cancel = v.findViewById(R.id.cancel);
        selcteAll = v.findViewById(R.id.selcte_all);
        RecyclerView lv = v.findViewById(R.id.list);

        if (showAllSelecte) {
            selcteAll.setVisibility(View.VISIBLE);
        }

        data = getDatas(datas, names);
        adapter = new DialogMListAdapter(context, data);
        lv.setAdapter(adapter);
        ad.setView(v);
        d = ad.create();
        UI.hideSoftKeyboard((BaseActivity) context);
        adapter.setOnItemClickListener(new MListDialog.MyListSelectedListener(adapter, data, view, selected));
        d.show();

        autoCompleteTextView.addTextChangedListener(watcher);
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            //选中搜索项
            String name = ((TextView) view1).getText().toString();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getString("name").equals(name)) {
                    data.get(i).put("selecte", true);
                    selected[i] = true;
                    lv.scrollToPosition(i);
                    autoCompleteTextView.removeTextChangedListener(watcher);
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.addTextChangedListener(watcher);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        });
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        selcteAll.setOnClickListener(this);
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

    public MyData getDatas(String[] datas, String[] names) {
        if (datas != null && datas.length > 0) {
            selected = new boolean[datas.length];
            for (int i = 0; i < datas.length; i++) {
                MyRow row = new MyRow();
                row.put("name", datas[i]);
                if (names.length > 0) {
                    boolean ifHas = false;
                    for (String name : names) {
                        if (datas[i].equals(name))
                            ifHas = true;
                    }
                    if (ifHas) {
                        selected[i] = true;
                        row.put("selecte", selected[i]);
                    } else {
                        row.put("selecte", selected[i]);
                    }
                } else {
                    row.put("selecte", selected[i]);
                }
                data.add(row);
            }
        }
        oldSelected = Arrays.copyOf(selected, selected.length);
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                onSelectedListener.onSelected(v, selected);
                d.dismiss();
                break;
            case R.id.cancel://取消
                for (int i = 0; i < oldSelected.length; i++) {
                    data.get(i).put("selecte", oldSelected[i]);
                }
                d.dismiss();
                break;
            case R.id.selcte_all://全选
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
                break;
        }
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
