package com.zyf.view.pageMenuLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zyf.common.R;
import com.zyf.model.MyData;
import com.zyf.view.pageMenuLayout.EntranceAdapter.EntranceAdapter;
import com.zyf.view.pageMenuLayout.EntranceAdapter.PageViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * 自定义仿美团首页分页切换功能按钮
 * 使用示例：
 * 1.布局文件
 * <LinearLayout
 * android:id="@+id/page_menu_ll"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:orientation="vertical"
 * app:layout_constraintBottom_toTopOf="@id/regist_device"
 * app:layout_constraintLeft_toLeftOf="parent"
 * app:layout_constraintRight_toRightOf="parent"
 * app:layout_constraintTop_toBottomOf="@id/banner">
 * <com.zyf.view.PageMenuLayout.PageMenuLayout
 * android:id="@+id/page_menu"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * app:pagemenu_row_count="2"
 * app:pagemenu_span_count="5" />
 * <com.zyf.view.PageMenuLayout.IndicatorView
 * android:id="@+id/page_indicator"
 * android:layout_width="match_parent"
 * android:layout_height="32dp"
 * android:layout_gravity="bottom|center_horizontal"
 * android:layout_marginLeft="16dp"
 * android:layout_marginRight="16dp"
 * app:gravity="0"
 * app:indicatorColor="#668b8989"
 * app:indicatorColorSelected="#FF5722"
 * app:indicatorWidth="6" />
 * </LinearLayout>
 * 2.数据绑定
 * mPageMenuLayout.setItemLayout(R.layout.item_page_menu);
 * mPageMenuLayout.setPageDatas(data);
 * page_Indicator.setIndicatorCount(mPageMenuLayout.getPageCount());
 * mPageMenuLayout.setOnPageListener(new ViewPager.SimpleOnPageChangeListener() {
 * @Override public void onPageSelected(int position) {
 * super.onPageSelected(position);
 * page_Indicator.setCurrentIndicator(position);
 * }
 * });
 * 3.重写item点击事件
 * @Override public void onPageMenuItemClick(View view, Object o, int position) {
 * UI.showToast(this, "点击了" + ((MyRow) o).getString("name"));
 * }
 */
public class PageMenuLayout extends RelativeLayout {
    private Context context;
    private static final int DEFAULT_ROW_COUNT = 2;
    private static final int DEFAULT_SPAN_COUNT = 5;
    private CustomViewPager mViewPager;
    /**
     * 行数
     */
    private int mRowCount = DEFAULT_ROW_COUNT;
    /**
     * 列数
     */
    private int mSpanCount = DEFAULT_SPAN_COUNT;
    /**
     * 功能按钮xml
     */
    private int mlayout;

    public PageMenuLayout(Context context) {
        this(context, null, 0);
        this.context = context;
    }

    public PageMenuLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public PageMenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mViewPager = new CustomViewPager(context);
        addView(mViewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageMenuLayout);
        if (typedArray != null) {
            mRowCount = typedArray.getInteger(R.styleable.PageMenuLayout_pagemenu_row_count, DEFAULT_ROW_COUNT);
            mSpanCount = typedArray.getInteger(R.styleable.PageMenuLayout_pagemenu_span_count, DEFAULT_SPAN_COUNT);
            typedArray.recycle();
        }

    }

    public void setPageDatas(MyData datas) {
        setPageDatas(mRowCount, mSpanCount, datas);
    }

    /**
     * @return 菜单总页数
     */
    public int getPageCount() {
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            return mViewPager.getAdapter().getCount();
        } else {
            return 0;
        }
    }

    /**
     * 页面滚动监听
     *
     * @param pageListener
     */
    public void setOnPageListener(ViewPager.OnPageChangeListener pageListener) {
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(pageListener);
        }
    }

    /**
     * 设置行数
     *
     * @param rowCount
     */
    public void setRowCount(int rowCount) {
        mRowCount = rowCount;
    }

    /**
     * 设置列数
     *
     * @param spanCount
     */
    public void setSpanCount(int spanCount) {
        mSpanCount = spanCount;
    }

    /**
     * 设置功能按钮xml
     */
    public void setItemLayout(int layout) {
        mlayout = layout;
    }

    public void setPageDatas(int rowCount, int spanCount, MyData datas) {
        if (datas == null) {
            datas = new MyData();
        }
        mRowCount = rowCount;
        mSpanCount = spanCount;
        if (mRowCount == 0 || mSpanCount == 0) {
            return;
        }
        int pageSize = mRowCount * mSpanCount;
        int pageCount = (int) Math.ceil(datas.size() * 1.0 / pageSize);
        List<View> viewList = new ArrayList<>();
        for (int index = 0; index < pageCount; index++) {
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), mSpanCount));
            EntranceAdapter entranceAdapter = new EntranceAdapter(context, mlayout, datas, pageSize, index);
            recyclerView.setAdapter(entranceAdapter);
            viewList.add(recyclerView);
        }
        PageViewPagerAdapter adapter = new PageViewPagerAdapter(viewList);
        mViewPager.setAdapter(adapter);
    }
}