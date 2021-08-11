package com.kunbo.xiwei.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zyf.device.BaseActivity;
import com.zyf.util.FileUtil;
import com.zyf.util.UI;
import com.zyf.view.SignatureView;
import com.zyf.xiweiapp.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 电子签名
 */
public class SignatureActivity extends BaseActivity {
    @BindView(R.id.tips)
    TextView tvTips;
    @BindView(R.id.clear)
    TextView tvClear;
    @BindView(R.id.view_signature)
    SignatureView signatureView;
    @BindView(R.id.confirm)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
        signatureView.setOnSignedListener(new SignatureView.OnSignedListener() {
            @Override
            public void onStartSigning() {
                hide(R.id.tips);
            }

            @Override
            public void onSigned() {

            }

            @Override
            public void onClear() {
                show(R.id.tips);
            }
        });
    }

    @OnClick({R.id.confirm, R.id.clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                signatureView.clear();
                break;
            case R.id.header_back:
                UI.showOKCancelDialog(this, "温馨提示", "您还未签名，确认退出？", 1000, null);
                break;
            case R.id.confirm:
                if (signatureView.getTouched()) {
                    saveBitmap();
                } else {
                    UI.showToast(this, "请先签名!");
                }
                break;
        }
    }

    private void saveBitmap() {
        try {
            signatureView.save(Environment.getExternalStorageDirectory() + File.separator + "/Signature/",
                    String.format("Signature_%d.png", System.currentTimeMillis()),
                    false, 0);
            String path = signatureView.getSavePath();
            Intent in = new Intent();
            in.putExtra("path", path);
            setResult(RESULT_OK, in);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UI.showOKCancelDialog(this, "温馨提示", "您还未签名，确认退出？", 1000, null);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
