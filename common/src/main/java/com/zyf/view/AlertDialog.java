package com.zyf.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.zyf.common.R;


/**
 * 活动申请dialog
 *
 * @author zyf
 */

@SuppressLint("ResourceAsColor")
public class AlertDialog {
    Context context;
    android.app.AlertDialog ad;
    public TextView titleView, messageView, cancel, sure;
    public View view,v;

    public AlertDialog(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();
        ad.setCancelable(false);
        ad.show();
        // 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
        Window window = ad.getWindow();
        window.setContentView(R.layout.dialog);
        titleView = window.findViewById(R.id.title);
        messageView = window.findViewById(R.id.message);
        v = window.findViewById(R.id.line);
        view= window.findViewById(R.id.view);
        cancel = window.findViewById(R.id.cancel);
        sure = window.findViewById(R.id.positive);
    }

    public void setTitle(int resId) {
        titleView.setText(resId);
        messageView.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public void setTitle(String title) {
        titleView.setText(title);
        messageView.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    public void setMessage(String message) {
        titleView.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        messageView.setText(message);
        messageView.setVisibility(View.VISIBLE);
    }

    public void setTitleAndMsg(String title, String msg) {
        titleView.setText(title);
        messageView.setText(msg);
    }


    public void setOKDialog() {//只有确定按钮
        cancel.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
    }

    /**
     * 确定按钮
     *
     * @param text        文字
     * @param buttonStyle button点击样式
     * @param color       文字颜色
     * @param listener
     */
    public void setPositiveButton(String text, int buttonStyle, int color,
                                  final View.OnClickListener listener) {
        sure.setText(text);
        if (buttonStyle > 0) {
            sure.setBackgroundResource(buttonStyle);
        }
        if (color > 0) {
            sure.setTextColor(context.getResources().getColor(color));
        }
        sure.setOnClickListener(listener);
    }

    /**
     * 取消按钮
     *
     * @param text
     * @param listener
     */
    public void setNegativeButton(String text, int buttonStyle, int color,
                                  final View.OnClickListener listener) {
        cancel.setText(text);
        if (buttonStyle > 0) {
            cancel.setBackgroundResource(buttonStyle);
        }
        if (color > 0) {
            cancel.setTextColor(context.getResources().getColor(color));
        }
        cancel.setOnClickListener(listener);
    }

    /**
     * 内容对其方式
     */
    public void setMsgGravityLeft() {
        messageView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }
}
