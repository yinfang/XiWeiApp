package com.zyf.device;

import android.os.Handler;
import android.widget.TextView;

import com.zyf.common.R;

/**
 * 需要短信验证码的界面父类，例如注册界面
 *
 * @author chenyh
 */
public class VerifyCodeActivity extends BaseActivity {
    private int elapsed;
    private Handler h = new Handler();
    /**
     * 获取短信码按钮，子类中声明
     */
    protected TextView btnAsk;

    protected void resetAsk() {
        h.removeCallbacks(myRunnable);
        elapsed = 0;
        btnAsk.setClickable(true);
        btnAsk.setText(R.string.get_verify_code);
    }

    private Runnable myRunnable = new Runnable() {
        public void run() {
            setTime();
            h.postDelayed(myRunnable, 1000);
        }
    };

    private void setTime() {
        if (elapsed > 0) {
            elapsed--;
            btnAsk.setText(String.format(getString(R.string.wait), elapsed));
        } else {
            resetAsk();
        }
    }

    protected void disableAskButton() {
        resetAsk();
        elapsed = 60;
        btnAsk.setClickable(false);
        h.postDelayed(myRunnable, 1000);
    }


}
