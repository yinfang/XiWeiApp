package com.zyf.view.pageMenuLayout.EntranceAdapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * ViewPager适配器
 */
public class PageViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    public PageViewPagerAdapter(List<View> viewList) {
        this.mViewList = viewList;
        if (viewList == null) {
            mViewList = new ArrayList<>();
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        container.removeView(mViewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mViewList.size();
        View view = mViewList.get(realPosition);
        if (container.equals(view.getParent())) {
            container.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (mViewList.isEmpty()) {
            return 0;
        }
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}