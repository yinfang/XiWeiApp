package com.kunbo.xiwei.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.adapter.PicMultiSelectAdapter;
import com.zyf.device.BaseActivity;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.UI;
import com.zyf.xiweiapp.R;

import butterknife.OnClick;

/**
 * 多图选择
 */
public class PicMultiSelectActivity extends BaseActivity {
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    RecyclerView gridView;
    PicMultiSelectAdapter adapter;
    private int maxSelect = 8;
    private MyData pics = new MyData();
    MyData datas = new MyData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_multi_select);
        gridView = findViewById(R.id.gridView);
        adapter = new PicMultiSelectAdapter(this, datas);
        gridView.setAdapter(adapter);
        getLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        maxSelect = getIntent().getIntExtra("maxSelect", 8);
        if (maxSelect == 1) {
            adapter.setSingle(true);
            findViewById(R.id.tv_sure).setVisibility(View.GONE);
        } else {
            adapter.setSingle(false);
            findViewById(R.id.tv_sure).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(LOADER_ALL);
    }

    @OnClick(R.id.tv_sure)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                if (adapter != null) {
                    MyData data = new MyData();
                    for (int i = 0; i < adapter.getDatas().size(); i++) {
                        MyRow r = (MyRow) adapter.getDatas().get(i);
                        if (r.getBoolean("check")) {
                            data.add(r);
                        }
                    }
                    if (data.size() > 0) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", data);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        //上传时批量压缩图片（当前客户要求不压缩）toZoomImage(data);
                    } else {
                        UI.showToast(this, "请选择图片");
                    }
                }
                break;
        }
    }


    /* */
    /**
     * 压缩图片
     *//*
    private void toZoomImage(MyData data) {
        List<String> paths = new ArrayList<>();
        for (final MyRow row : data) {
            pics.add(row);
            paths.add(row.getString("path"));
        }
//        new MyAsyncTask(this, CompressImage.class).run(paths);
    }

    @Override
    public void onPostExecute(Class<?> op, Result result) {
//        if (op == CompressImage.class) {
//            List<String> lists = (List<String>) result.obj;
//            if (lists.size() > 0) {
//                for (int i = 0; i < pics.size(); i++) {
//                    pics.get(i).put("path", lists.get(i));
//                }
//            }
//        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", pics);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }*/

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager
            .LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader =
                        new CursorLoader(PicMultiSelectActivity.this,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                                null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(PicMultiSelectActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                datas = new MyData();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        MyRow row = new MyRow();
                        row.put("check", false);
                        String path = data.getString(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow
                                (IMAGE_PROJECTION[2]));
                        row.put("path", path);
                        row.put("name", name);
                        row.put("dateTime", dateTime);
                        datas.add(row);
                    } while (data.moveToNext());
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public void onItemClick(int position) {
        MyRow row = (MyRow) adapter.getDatas().get(position);
        if (adapter.isSingle()) {
            MyData data = new MyData();
            data.add(row);
          /*  String filePath = ImageUtil.saveTempBitmap(row.getString("path"), "tmp", ".jpg");
            row.put("path", filePath);
            pics.add(row);*/
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (maxSelect > adapter.getSelectedCount()) {
                row.put("check", !row.getBoolean("check"));
                adapter.notifyDataSetChanged();
            } else {
                if (row.getBoolean("check")) {
                    row.put("check", false);
                    adapter.notifyDataSetChanged();
                } else {
                    UI.showToast(this, "您最多只能选择" + maxSelect + "张图片!");
                }
            }
        }
    }
}
