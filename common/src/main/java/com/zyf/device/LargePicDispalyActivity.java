package com.zyf.device;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyf.common.R;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.Utils;
import com.zyf.util.image.ImageUtil;
import com.zyf.view.zoomPhotoView.PhotoView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.annotations.NonNull;

public class LargePicDispalyActivity extends BaseActivity {
    TextView tvPos, tvTotal;
    ViewPager viewPager;
    private List<View> views = new ArrayList<>();
    private MyData datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_large_pic);
        initView();
    }

    protected void initView() {
        tvPos = findViewById(R.id.tv_pos);
        tvTotal = findViewById(R.id.tv_total);
        viewPager = findViewById(R.id.pager);
        datas = Utils.getData(getIntent().getExtras(), "datas");
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                MyRow ro = datas.get(i);
                if (TextUtils.isEmpty(ro.getString("path"))) {
                    datas.remove(ro);
                }
            }
        }
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setOffscreenPageLimit(2);
        setPagers();
    }

    public void setPagers() {
        if (datas.size() > 0) {
            int size = datas.size();
            for (int i = 0; i < size; i++) {
                if (!TextUtils.isEmpty(datas.get(i).getString("path"))) {
                    PhotoView v = new PhotoView(this);
                    v.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    views.add(v);
                }
            }
        }
        tvPos.setText(1 + "");
        tvTotal.setText(datas.size() > 0 ? datas.size() + "" : "1");
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, views);
        viewPager.setAdapter(adapter);
        int position = getIntent().getIntExtra("position", 0);
        viewPager.setCurrentItem(position);
        showPage(position);
    }

    public void showPage(int pos) {
        if (null != datas.get(pos)) {
            PhotoView v = (PhotoView) views.get(pos);
            setImage(v, datas.get(pos).getString("path"));
        }
    }

    class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int pos) {
            super.onPageSelected(pos);
            tvPos.setText("" + (pos + 1));
            showPage(pos);
        }
    }

    public class ImagePagerAdapter extends PagerAdapter {

        private List<View> views;
        Context context;

        public ImagePagerAdapter(Context context, List<View> views) {
            this.views = views;
            this.context = context;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }


        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
