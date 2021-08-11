package com.zyf.device;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyf.common.R;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.UI;
import com.zyf.view.recyclerview.ViewHolder;

import java.util.List;

/**
 * SmartRefreshLayout
 * 上拉加载，下拉刷新使用示例
 */
public class RefreshActivity extends BaseActivity {
    RecyclerView refreshRecycler;
    SmartRefreshLayout refreshLayout;
    private MyData datas = new MyData();
    private RefreshItemDelAdapter adapter;
    private MyData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        refreshRecycler = findViewById(R.id.refresh_recycler);
        refreshLayout = findViewById(R.id.refreshLayout);
        initView();
        initListener();
    }

    private void initView() {
        data = new MyData();
        for (int i = 0; i < 19; i++) {
            MyRow row = new MyRow();
            row.put("title", "我是第" + i + "个item");
            data.add(row);
        }
        adapter = new RefreshItemDelAdapter(this, R.layout.item_activity_refresh, data);
        refreshRecycler.setAdapter(adapter);
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (data != null) {
                    data.clear();
                }
                MyRow row = new MyRow();
                row.put("title", "刷新完成了！");
                data.add(row);
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                MyRow row = new MyRow();
                row.put("title", "我是上拉加载的数据！");
                data.add(row);
                adapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
            }
        });
    }

    /**
     * item点击事件
     */
    public void onItemClick(ViewHolder holder, MyRow ro, int position) {
        UI.showToast(this, "点击了第" + (position + 1) + "条数据");
    }

    /**
     * 侧滑删除item
     */
    public void itemToDelete(ViewHolder holder, MyRow ro, int position) {
        data.remove(position);
        UI.showToast(this, "删除了第" + (position + 1) + "条数据！");
    }

    /**
     * 侧滑置顶item
     */
    public void itemToTop(ViewHolder holder, MyRow ro, int position) {
        if (position > 0 && position < data.size()) {
            MyRow row = data.get(position);
            data.remove(row);
            adapter.notifyItemInserted(0);
            data.add(0, row);
            adapter.notifyItemRemoved(position + 1);
            LinearLayoutManager layoutManager = (LinearLayoutManager) refreshRecycler.getLayoutManager();
            if (layoutManager != null && layoutManager.findFirstVisibleItemPosition() != 0) {
                refreshRecycler.scrollToPosition(0);
            }
            UI.showToast(this, "第" + (position + 1) + "条数据置顶了！");
        }
    }

    public class RefreshItemDelAdapter extends RecyclerCommonAdapter {
        private Context context;

        public RefreshItemDelAdapter(Context context, int layoutId, List datas) {
            super(context, layoutId, datas);
            this.context = context;
        }

        @Override
        protected void convert(ViewHolder holder, Object o, int position) {
            MyRow ro = (MyRow) o;
            TextView content = holder.getView(R.id.content);
            holder.setText(R.id.content, ro.getString("title"));
            Button btnTop = holder.getView(R.id.btnTop);
            Button btnDel = holder.getView(R.id.btnDelete);
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RefreshActivity) context).onItemClick(holder, ro, position);
                }
            });
            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RefreshActivity) context).itemToTop(holder, ro, position);
                }
            });
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RefreshActivity) context).itemToDelete(holder, ro, position);
                }
            });
        }

    }
}
