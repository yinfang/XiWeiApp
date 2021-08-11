package com.kunbo.xiwei.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.VideoActivity;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.util.DownloadAndOpenFile;
import com.zyf.util.UI;
import com.zyf.xiweiapp.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TbsTestActivity extends VideoActivity  {
    private TbsReaderView mTbsReaderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_click_test);
//        ButterKnife.bind(this);
//         downLoadAndOpenFile();
    }

    /**
     * 下载打开文件
     */
    private void downLoadAndOpenFile() {
        MyRow row = new MyRow();
        row.put("filePath", "");
        row.put("fileName", "");
        String fileName = row.getString("fileName");
        new DownloadAndOpenFile(this, row, new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(Result result, String method) {
                DownloadAndOpenFile.openFileWithQbsdk(TbsTestActivity.this, (File) result.obj);
               /* mTbsReaderView = new TbsReaderView(TbsTestActivity.this, new TbsReaderView.ReaderCallback() {
                    @Override
                    public void onCallBackAction(Integer integer, Object o, Object o1) {

                    }
                });
                DownloadAndOpenFile.openFileWithTbs(TbsTestActivity.this, mTbsReaderView, (File) result.obj, fileName);*/
            }

            @Override
            public void onFault(Result result, String method) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTbsReaderView) {
            mTbsReaderView.onStop();
        }
    }
}
