package com.zyf.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyf.common.R;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.view.lemonhello.LemonHello;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.view.lemonhello.LemonHelloView;
import com.zyf.view.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;


public class UI {

    public static void showToast(Context context, int resId) {
        showToast(context, context.getText(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, CharSequence msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, CharSequence msg, int duration) {
        if (!Utils.checkNullString(msg.toString())) {
            Toast.makeText(context, msg, duration).show();
        }
    }

    public static void showError(Context context, String title, String msg) {
        LemonHello.getErrorHello(Utils.checkNullString(title) ? "发生错误" : title, msg)
                .addAction(new LemonHelloAction("关闭", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        lemonView.hide();
                    }
                })).show(context);
    }

    public static void showInfo(Context context, String title, String msg) {
        LemonHello.getInformationHello(Utils.checkNullString(title) ? "提示" : title, msg)
                .addAction(new LemonHelloAction("我知道了", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        lemonView.hide();
                    }
                })).show(context);
    }

    public static void showOKCancelDialog(BaseActivity context, int titleResId, int msgResId, int type) {
        showOKCancelDialog(context, titleResId, msgResId, type, null);
    }

    public static void showOKCancelDialog(BaseActivity context,
                                          int titleResId, int msgResId, int type, Object tag) {
        showOKCancelDialog(context, context.getText(titleResId).toString(), context.getText(msgResId).toString(), type, tag);
    }

    /**
     * 带确认和取消按钮的对话框
     *
     * @param context
     * @param title
     * @param message
     * @param type
     * @param tag
     */
    public static void showOKCancelDialog(final BaseActivity context, String title, String message, final int type, final Object tag) {
        new LemonHelloInfo().setTitle(title).setTitleFontSize(18).setContent(message).setContentFontSize(15)
                .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        lemonView.hide();
                        if (type != -1) {
                            context.processDialogOK(type, tag);
                        }
                    }
                }))
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        lemonView.hide();
                        if (type != -1) {
                            context.processDialogCancel(type, tag);
                        }
                    }
                })).show(context);
    }

    /**
     * 自定义 按钮文字/背景色/是否显示取消按钮  对话框
     *
     * @param context
     * @param title
     * @param msg
     * @param showCancel
     * @param type
     * @param tag
     * @param background
     */
    public static void showCustomDialog(final BaseActivity context, String title, String
            msg, String sure, String cancel, int background, boolean showCancel, final int type,
                                        final Object tag) {
        LemonHelloInfo info = new LemonHelloInfo().setTitle(title).setTitleFontSize(18).setContent(msg).setContentFontSize(15);
        LemonHelloAction actionSure = new LemonHelloAction();
        actionSure.setTitle(sure);
        if (background > 0) {
            actionSure.setBackgroundColor(background);
        }
        actionSure.setDelegate(new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                if (type != -1) {
                    context.processDialogOK(type, tag);
                }
                lemonView.hide();
            }
        });
        info.addAction(actionSure);
        if (showCancel) {
            LemonHelloAction actionCancel = new LemonHelloAction();
            actionCancel.setTitle(cancel);
            if (background > 0) {
                actionCancel.setBackgroundColor(background);
            }
            actionCancel.setDelegate(new LemonHelloActionDelegate() {
                @Override
                public void onClick(LemonHelloView lemonView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                    if (type != -1) {
                        context.processDialogCancel(type, tag);
                    }
                    lemonView.hide();
                }
            });
            info.addAction(actionCancel);
        }
        info.show(context);
    }

    /**
     * 显示大图的dialog
     *
     * @param context
     * @param bitmap
     */
    public static void showImageDialog(BaseActivity context, Bitmap bitmap) {
        //放在custom中退出会出现两层界面
        final AlertDialog ad = new Builder(context).create();
        ad.show();
        Window window = ad.getWindow();
        window.setContentView(R.layout.image_dialog);
        ImageView image = (ImageView) ad.findViewById(R.id.image);
        image.setImageBitmap(bitmap);
    }


    /**
     * 用弹出窗口编辑单个文本内容
     *
     * @param title     窗口显示标题
     * @param view      要修改的主窗口文本控件
     * @param inputType 输入类型
     * @param maxLength 长度
     */
    public static void showEditDialog(BaseActivity context, String title, View view,
                                      final int inputType, final int maxLength) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.text, null);
        final TextView tv = (TextView) view;
        EditText input = dialogView.findViewById(R.id.input_text);
        input.setInputType(inputType);
        String s = tv.getText().toString();
        if (maxLength > 0) {
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    maxLength)});
            if (s.length() > maxLength) {
                s = s.substring(0, maxLength);
            }
        }
        if (inputType == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            input.setSingleLine(false);
        }
        input.setText(s);
        input.setSelection(s.length());

        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title)
                .setPositiveButton("确认", null).setNegativeButton("取消", null)
                .create();
        alertDialog.setView(dialogView);
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(UI.getEText(view, R.id.input_text));
            }
        });
    }

    public static int getId(Context context, String resCode, String type) {
        int resId = context.getResources().getIdentifier(resCode, type,
                context.getPackageName());
        return resId;
    }

    public static View getView(View view, int id) {
        return view.findViewById(id);
    }

    public static View getView(Activity a, int id) {
        return a.findViewById(id);
    }

    /**
     * 根据类型获取资源
     *
     * @param a
     * @param resCode
     * @param type    获取布局文件资源的ID("layout")  获取图片资源的ID("drawable"  或 "mipmap")   根据id获取view（"id"）
     * @return
     */
    public static View getView(Activity a, String resCode, String type) {
        return a.findViewById(getId(a, resCode, type));
    }

    public static CharSequence getText(Context context, String resCode) {
        int id = getId(context, resCode, "string");
        if (id > 0) {
            return context.getText(id);
        }
        return null;
    }

    /**
     * 获取图片(resid)的byte[]
     *
     * @param context
     * @param resId
     * @return
     */
    public static byte[] getBytes(Context context, int resId) {
        Resources resources = context.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(resources, resId);
        return getBytes(bmp);
    }

    public static byte[] getBytes(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, C.compress, stream);
        byte[] b = stream.toByteArray();
        return b;
    }

    public static int getPixel(Context context, int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static String getEText(Activity a, int resId) {
        TextView tv = a.findViewById(resId);
        if (tv != null) {
            return tv.getText().toString();
        }
        return "";
    }

    public static String getEText(View view, int resId) {
        TextView tv = view.findViewById(resId);
        if (tv != null) {
            return tv.getText().toString();
        }
        return "";
    }

    public static void setEText(Activity a, TextView tv, CharSequence value) {
        if (tv != null) {
            tv.setText(value.toString().trim());
        }
    }

    public static void setEText(Activity a, int resId, CharSequence value) {
        if (value != null) {
            TextView tv = a.findViewById(resId);
            if (tv != null) {
                tv.setText(value.toString().trim());
            }
        }
    }

    public static void setEText(View view, int resId, CharSequence value) {
        if (value != null) {
            TextView tv = view.findViewById(resId);
            if (tv != null) {
                tv.setText(value.toString().trim());
            }
        }
    }

    public static void setEText(Activity a, int resId, int resValue) {
        ((TextView) a.findViewById(resId)).setText(resValue);
    }


    public static void setEText(View view, int resId, int resValue) {
        ((TextView) view.findViewById(resId)).setText(resValue);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    //隐藏软键盘
    public static void hideSoftKeyboard(Activity a) {
        if (a.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN) {
            if (a.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) a.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //显示软键盘
    public static void showSoftKeyboard(Activity a) {
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 获取屏幕高度（除掉顶部状态栏的高度）
     *
     * @param context
     * @return in pixels
     */
    public static int getPureHeight(Context context) {
        int heightPixels = 0;
        try {
            Class<?> cl = null;
            Object obj = null;
            Field field = null;
            int x = 0, sbar = 0;
            cl = Class.forName("com.android.internal.R$dimen");
            obj = cl.newInstance();
            field = cl.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            // 获取屏幕高度和宽度
            DisplayMetrics dm = new DisplayMetrics();
            ((BaseActivity) context).getWindow().getWindowManager()
                    .getDefaultDisplay().getMetrics(dm);
            heightPixels = dm.heightPixels - sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return heightPixels;
    }

    /**
     * 获取屏幕像素宽度 px
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕像素高度 px
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 设置全屏
     */
    protected void setFullScreen(Activity activity, boolean isFullScreen) {
        if (isFullScreen == true) {
            //隐藏状态栏
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        }
    }

    /**
     * 隐藏虚拟按键
     */
    protected void setHideNavigationBar(Activity activity, boolean isHide) {
        if (android.os.Build.VERSION.SDK_INT >= 19) {//修改View.SYSTEM_UI_FLAG_FULLSCREEN导致的屏幕无法获取焦点问题
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 页面回到顶部（初始化界面后焦点不在顶部的问题）
     * 在界面最后一个listview高度设置完,界面完全绘制好之后，使用该方法
     * 在OnCreate中调用无效
     *
     * @param scroll
     */
    public static void scrollToTop(final ScrollView scroll) {

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                scroll.smoothScrollTo(0, 0);
            }
        });
    }

}
