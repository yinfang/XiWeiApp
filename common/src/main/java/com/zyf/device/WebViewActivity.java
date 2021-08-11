package com.zyf.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyf.common.R;
import com.zyf.view.lemonbubble.LemonBubble;

@SuppressLint({"SetJavaScriptEnabled", "Registered"})
public class WebViewActivity extends BaseActivity {
    WebView wv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        wv = findViewById(R.id.webView);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setSupportZoom(true);
        wv.setWebViewClient(new MyWebViewClient(this));
        wv.getSettings().setUseWideViewPort(true);
        wv.setInitialScale(20);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.setBackgroundColor(0);
        if (url != null) {
            wv.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

class MyWebViewClient extends WebViewClient {
    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LemonBubble.showRoundProgress(context, "加载中...");
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        LemonBubble.hide();
    }

    @Override
    public void onReceivedError(WebView view, int erroCode, String description,
                                String failingUrl) {
        LemonBubble.hide();
    }

}
