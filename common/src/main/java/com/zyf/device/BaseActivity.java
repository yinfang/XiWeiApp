package com.zyf.device;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.widget.NestedScrollView;

import com.zyf.common.R;
import com.zyf.domain.C;
import com.zyf.model.ContactInfo;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.util.ACache;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.dialog.ListDialog;
import com.zyf.util.dialog.MListDialog;
import com.zyf.util.dialog.MyDateDialog;
import com.zyf.util.dialog.MyDatePickerDialog;
import com.zyf.util.dialog.MyTimeDialog;
import com.zyf.util.dialog.MyTimePickerDialog;
import com.zyf.util.image.GlideCacheUtil;
import com.zyf.util.image.GlideUtil;
import com.zyf.util.statusBarUtil.StatusBarUtil;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.view.lemonhello.LemonHelloView;
import com.zyf.view.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.ArrayList;
import java.util.Calendar;

public class BaseActivity extends BaseCheckPermissionActivity implements OnSuccessAndFaultListener {
    protected final String TAG = getClass().getSimpleName();
    public MyApplication app;
    protected String[] menus;
    protected int[] menuImages;

    /**
     * 要填充的图片控件
     */
    protected ImageView iv;
    private OnClickListener clickListener;

    private Calendar ca = Calendar.getInstance();
    private GlideUtil glideUtil = GlideUtil.getInstance();
    protected boolean animator = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.goneNavigationBar(this);
        app = (MyApplication) getApplication();
        clickListener = new MyOnClickListener();
        app.addActivity(this);
        AppUtil.setScreenOrientation(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    protected void onStart() {
        super.onStart();
    }

    private void setOnClickListener(String res) {
        int resId = UI.getId(this, res, "id");
        if (resId > 0) {
            View v = findViewById(resId);
            if (v != null) {
                v.setOnClickListener(clickListener);
            }
        }
    }

    public void openIntent(Class<?> clazz, CharSequence title) {
        openIntent(clazz, title, null);
    }

    public void openIntent(Class<?> clazz, CharSequence title, Bundle extras) {
        openIntent(clazz, title, extras, 0);
    }

    public void openIntent(Class<?> clazz, CharSequence title, Bundle extras,
                           int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!animator) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("title", title.toString().replaceAll("\n", " "));
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        if (animator) {
            overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);
        }
    }

    public void openIntent(Class<?> clazz, int resId) {
        openIntent(clazz, getText(resId), null);
    }

    public void openIntent(Class<?> clazz, int resId, int requestCode) {
        openIntent(clazz, getText(resId), null, requestCode);
    }

    public void openIntent(Class<?> clazz, int resId, Bundle extras,
                           int requestCode) {
        openIntent(clazz, getText(resId), extras, requestCode);
    }

    /**
     * 打开新Activity
     *
     * @param clazz Activity
     */
    public void openIntent(Class<?> clazz, int resId, Bundle b) {
        openIntent(clazz, getText(resId), b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnClickListener("menu");
        setOnClickListener("ic_home");
        setOnClickListener("header_back");
        Bundle bundle = getIntent().getExtras();
        String title = null;
        if (bundle != null) {
            title = bundle.getString("title");
        }
        if (title != null) {
            setHeaderTitle(title);
        }
        restoreVars();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        app.removeActivity(this);
    }

    protected void setHeaderTitle(CharSequence title) {
        View tv = findViewById(R.id.header_title);
        if (title != null && tv instanceof TextView) {
            ((TextView) tv).setText(title);
        }
    }

    protected void setHeaderTitle(int resId) {
        setHeaderTitle(getText(resId));
    }

    public String getEText(int resId) {
        return UI.getEText(this, resId);
    }

    public void setEText(TextView tv, CharSequence value) {
        UI.setEText(this, tv, value);
    }

    public void setEText(int resId, CharSequence value) {
        UI.setEText(this, resId, value);
    }

    public void setEText(int resId, int resValue) {
        UI.setEText(this, resId, resValue);
    }

    public void setEText(View view, int resId, CharSequence value) {
        UI.setEText(view, resId, value);
    }

    public void setEText(View view, int resId, int resValue) {
        UI.setEText(view, resId, resValue);
    }

    public void setEColor(int resId, int color) {
        TextView tv = (TextView) findViewById(resId);
        if (tv != null)
            tv.setTextColor(getResources().getColor(color));
    }

    public void setVColor(int resId, int color) {
        View v = (View) findViewById(resId);
        if (v != null)
            v.setBackgroundColor(getResources().getColor(color));
    }

    public void setEColor(View view, int resId, int color) {
        TextView tv = (TextView) view.findViewById(resId);
        if (tv != null)
            tv.setTextColor(getResources().getColor(color));
    }

    public void setImage(ImageView iv, Object model) {
        glideUtil.setImage(this, iv, model);
    }

    /**
     * 加载图片的字节数组
     *
     * @param model string可以为一个文件路径、uri或者url
     *              uri类型
     *              文件
     *              资源Id,R.drawable.xxx或者R.mipmap.xxx
     *              byte[]类型
     */
    public void setImage(int ImageViewId, Object model) {
        glideUtil.setImage(this, ImageViewId, model);
    }

    public void setImage(ImageView iv, Object model, boolean isCircle) {
        glideUtil.setImage(this, iv, model, isCircle);
    }

    public void setImage(int ImageViewId, Object model, boolean isCircle) {
        glideUtil.setImage(this, ImageViewId, model, isCircle);
    }

    public void setImage(int ImageViewId, Object model, int loadingImage, boolean isCircle) {
        glideUtil.setImage(this, ImageViewId, model, loadingImage, isCircle);
    }

    public void setImage(int ImageViewId, Object model, boolean isCircle, int rounded) {
        glideUtil.setImage(this, ImageViewId, model, false, rounded);
    }

    public void setImage(int ImageViewId, Object model, int loadingImage) {
        glideUtil.setImage(this, ImageViewId, model, loadingImage);
    }

    /**
     * 调出打电话界面
     *
     * @param phoneNo 要打的电话
     */
    public void callPhone(String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNo));
        startActivity(intent);
    }

    /**
     * 退出app
     */
    public void logout() {
        exit(false);
    }


    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param captions 列表文本
     */
    public void showListDialog(View view, String[] captions) {
        showListDialog(view, 0, captions, null);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param resTitle 列表的显示标题
     * @param resArray 资源内定义的字符串数组
     */
    public void showListDialog(View view, int resTitle, int resArray) {
        showListDialog(view, resTitle, getResources().getStringArray(resArray),
                null);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view     触发控件
     * @param captions 列表文本
     * @param images   列表图标, 数量必须与captions保持一致
     */
    public void showListDialog(View view, String[] captions, int[] images) {
        showListDialog(view, 0, captions, images);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view       触发控件
     * @param resTitleId 显示标题栏文字，0 不显示标题栏
     * @param captions   列表文本
     * @param images     列表图标, 数量必须与captions保持一致
     */
    protected void showListDialog(View view, int resTitleId, String[] captions,
                                  int[] images) {
        ListDialog dialog = new ListDialog(this);
        dialog.setOnSelectedListener(new ListDialog.OnSelectedListener() {
            public void onSelected(View view, int index) {
                listSelected(view, index);
            }
        });
        dialog.show(view, resTitleId, captions, images);
    }

    /**
     * 显示普通列表窗口，子类覆盖回调方法listSelected
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     */
    protected void showListDialog(View view, int resTitleId, String[] captions) {
        showListDialog(view, resTitleId, captions, null);
    }

    /**
     * 弹出多选列表
     *
     * @param view     触发控件
     * @param captions 列表文本
     * @param selected 初始值，长度必须与captions相同
     */
    public void showMListDialog(final View view, String[] captions,
                                boolean[] selected) {
        showMListDialog(view, 0, captions, selected, true);
    }

    /**
     * 弹出多选列表
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     * @param selected   初始值，长度必须与captions相同
     */
    protected void showMListDialog(final View view, int resTitleId,
                                   String[] captions, boolean[] selected) {
        showMListDialog(view, resTitleId, captions, selected, true);
    }

    /**
     * 弹出多选列表
     *
     * @param view           触发控件
     * @param resTitleId     标题文字，0 不显示标题
     * @param captions       列表文本
     * @param selected       初始值，长度必须与captions相同
     * @param showAllSelecte 是否显示全选按钮
     */
    protected void showMListDialog(final View view, int resTitleId,
                                   String[] captions, boolean[] selected, boolean showAllSelecte) {
        MListDialog dialog = new MListDialog(this);
        dialog.setOnSelectedListener((view1, selected1) -> mlistSelected(view1, selected1));
        dialog.show(view, resTitleId, captions, selected, showAllSelecte);
    }

    private PopupWindow pop;

    /**
     * Show a pop up window
     *
     * @param resId  layout file in pop up window
     * @param anchor show pop up window below this control
     */
    @SuppressWarnings("deprecation")
    protected void showPopup(int resId, View anchor) {
        if (pop == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate(resId, null);
            pop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, false);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
        }
        if (pop.isShowing()) {
            pop.dismiss();
            pop = null;
        } else {
            pop.showAsDropDown(anchor);
        }
    }

    /**
     * 弹出Popup菜单，位置就在控件附近
     *
     * @param view  触发控件
     * @param pos   第几个菜单，1 开始
     * @param total 屏幕分为几列菜单，决定菜单宽度，3表示菜单宽度为屏幕的1/3
     * @param menus 菜单文本内容
     */
    public void showMenu(final View view, int pos, int total, String[] menus) {
        showMenu(view, pos, total, menus, new int[menus.length]);
    }

    /**
     * 弹出Popup菜单，位置就在控件附近
     *
     * @param view   触发控件
     * @param pos    第几个菜单，1 开始
     * @param total  屏幕分为几列菜单，决定菜单宽度，3表示菜单宽度为屏幕的1/3
     * @param menus  菜单文本内容
     * @param images 菜单前面显示的图标资源
     */
    @SuppressWarnings("deprecation")
    public void showMenu(final View view, int pos, int total, String[] menus,
                         int[] images) {
        if (menus == null || menus.length == 0) {
            /* 文本内容为空，忽略 */
            return;
        }
        View v = getLayoutInflater().inflate(R.layout.popwindow_light, null);
        ListView listView1 = (ListView) v.findViewById(R.id.PopWindow_lv);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();// deprecated in API 13, ignore it.
        int height = display.getHeight() / 2;

        final PopupWindow popupWindow = new PopupWindow(v, width / total,
                height);

        /* 加上这行可以确保点击外面后让弹出窗口消失 */
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);

        MyData data = new MyData();
        for (int i = 0; i < menus.length; i++) {
            MyRow row = new MyRow();
            if (images != null) {
                row.put("imageResId", images[i]);
            }
            row.put("name", menus[i]);
            data.add(row);
        }
//        MenuAdapter adapter = new MenuAdapter(this, data);
//        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                menuSelected(view, arg2);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown((View) view.getParent(), (width / total)
                * (pos - 1), 0);
    }

    @SuppressWarnings("deprecation")
    public void showTipsMenu(final View view, double factor, String[] menus,
                             int[] images) {
        if (menus == null || menus.length == 0) {
            /* 文本内容为空，忽略 */
            return;
        }
        View v = getLayoutInflater().inflate(R.layout.popwindow_light, null);
        ListView listView1 = (ListView) v.findViewById(R.id.PopWindow_lv);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();// deprecated in API 13, ignore it.
        int height = display.getHeight() / 2;

        final PopupWindow popupWindow = new PopupWindow(v,
                (int) (width * factor), height);

        /* 加上这行可以确保点击外面后让弹出窗口消失 */
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);

        MyData data = new MyData();
        for (int i = 0; i < menus.length; i++) {
            MyRow row = new MyRow();
            if (images != null) {
                row.put("imageResId", images[i]);
            }
            row.put("name", menus[i]);
            data.add(row);
        }
//        MenuAdapter adapter = new MenuAdapter(this, data);
//        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                menuSelected(view, arg2);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown((View) view.getParent(), 0, 0);
    }

    /**
     * 弹出日期选择框,子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    protected void showDateDialog(final View view, String title, int year, int month, int day,
                                  boolean showday) {
        MyDateDialog d = new MyDateDialog(this);
        d.setOnDateSetListener(new MyDatePickerDialog.OnDateSetListener() {
            boolean fired;

            public void onDateSet(DatePicker v, int year, int month, int day) {
                if (!fired) {
                    if (view instanceof TextView) {
                        ((TextView) view).setText(year + "-" + month + "-" + day);
                    }
                    dateSet(view, year, month, day);// 回调子类方法
                }
                fired = true;
            }
        });
        d.show(title, year, month, day, showday);
    }

    protected void showDateDialog(final View view, String title, int year, int month, int day,
                                  long minDate,
                                  long maxDate, boolean showDay) {

        MyDateDialog dialog = new MyDateDialog(this);
        dialog.setOnDateSetListener(new MyDatePickerDialog.OnDateSetListener() {
            boolean fired;

            @Override
            public void onDateSet(DatePicker v, int year, int month, int dayOfMonth) {
                if (!fired) {
                    if (view instanceof TextView) {
                        ((TextView) view).setText(year + "-" + month + "-" + dayOfMonth);
                    }
                    dateSet(view, year, month, dayOfMonth);// 回调子类方法
                }
                fired = true;
            }
        });

        dialog.show(title, year, month, day, minDate, maxDate, showDay);
    }


    /**
     * 填出日期选择框，子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     * @param date 显示已有日期，格式 yyyy-MM-dd
     */
    public void showDateDialog(View view, String title, String date) {
        String[] sdate = date.toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, title, year, month, day, true);
    }


    public void showDateDialog(View view, String title, String date, long minDate, long maxDate) {
        String[] sdate = date.toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, title, year, month, day, minDate, maxDate, true);
    }

    /**
     * 只展示年月的日期控件
     * <p/>
     * 填出日期选择框，子类需要实现回调方法 dateSet
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     * @param date 显示已有日期，格式 yyyy-MM-dd
     */
    public void showYearsDialog(View view, String title, String date) {
        String[] sdate = date.split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        int day;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = ca.get(Calendar.DAY_OF_MONTH);
        }
        showDateDialog(view, title, year, month, day, false);
    }

    /**
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showDateDialog(View view, String title) {
        int day;
        String[] sdate = ((TextView) view).getText().toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]) - 1;
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, title, year, month, day, true);
    }

    /**
     * 显示年或月控件
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showYearOrMonthDialog(View view, String title, String date) {

        if (date.length() > 2) {//显示年
            int month = ca.get(Calendar.MONTH);
            int day = ca.get(Calendar.DAY_OF_MONTH);
            int year = Integer.parseInt(date) + 1;
            showDateDialog(view, title, year, month, day, true, false, false);
        } else {//显示月
            int year = ca.get(Calendar.YEAR);
            int day = ca.get(Calendar.DAY_OF_MONTH);
            int month = Integer.parseInt(date) - 1;
            showDateDialog(view, title, year, month, day, false, true, false);
        }

    }

    /**
     * 弹出年或月选择框,子类需要实现回调方法 dateSet
     *
     * @param view  日期控件，必须是TextView /Button/EditText 等
     * @param year
     * @param month
     * @param day
     */
    protected void showDateDialog(final View view, String title, int year, int month, int day,
                                  boolean
                                          showYear, boolean showMonth,
                                  boolean showday) {
        MyDateDialog d = new MyDateDialog(this);
        d.setOnDateSetListener(new MyDatePickerDialog.OnDateSetListener() {
            boolean fired;

            public void onDateSet(DatePicker v, int year, int month, int day) {
                if (!fired) {
                    if (view instanceof TextView) {
                        ((TextView) view).setText(year + "-" + month + "-" + day);
                    }
                    dateSet(view, year, month, day);// 回调子类方法
                }
                fired = true;
            }
        });
        d.show(title, year, month, day, showYear, showMonth, showday);
    }

    /**
     * 显示年月控件
     *
     * @param view 日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     */
    public void showYearsDialog(View view, String title) {
        int day;
        String[] sdate = ((TextView) view).getText().toString().split("-");
        int year = Integer.parseInt(sdate[0]);
        int month = Integer.parseInt(sdate[1]);
        if (sdate.length > 2) {
            day = Integer.parseInt(sdate[2]);
        } else {
            day = 0;
        }
        showDateDialog(view, title, year, month, day, false);
    }


    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view   时间控件，必须是TextView /Button/EditText 等
     * @param hour
     * @param minute
     */
    protected void showTimeDialog(final View view, String title, int hour, int minute) {
        MyTimeDialog d = new MyTimeDialog(this);
        d.setOnTimeSetListener(new MyTimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker v, int hour, int minute) {
                if (view instanceof TextView) {
                    String time = Utils.getTimeString(hour, minute);
                    ((TextView) view).setText(time);
                }
                timeSet(view, hour, minute);// 回调子类方法
            }
        });
        d.show(title, hour, minute);
    }

    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view 时间控件，必须是TextView /Button/EditText 等
     * @param time 格式 19:30
     */
    public void showTimeDialog(View view, String title, String time) {
        String[] sdate = time.split(":");
        int hour = Integer.parseInt(sdate[0]);
        int minute = Integer.parseInt(sdate[1]);
        showTimeDialog(view, title, hour, minute);
    }

    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view 时间控件，必须是TextView /Button/EditText 等
     */
    public void showTimeDialog(View view, String title) {
        String[] sdate = ((TextView) view).getText().toString().split(":");
        int hour = Integer.parseInt(sdate[0]);
        int minute = Integer.parseInt(sdate[1]);
        showTimeDialog(view, title, hour, minute);
    }

    /**
     * 清除参数设置，
     */
    protected void clearSetting() {
        Editor editor = getSharedPreferences(C.APP_ID, MODE_PRIVATE).edit();
        editor.remove("SessionID");
        // editor.remove("rememberPassword");// 删除记住密码
        editor.commit();
    }

    /**
     * 通用弹出窗口点OK按钮后的回调方法
     *
     * @param type 标识，如果有多个地方触发，用于区分触发的是哪次。
     * @param tag  弹出窗口附加的自定义对象
     */
    public void processDialogOK(int type, Object tag) {
    }

    /**
     * 通用弹出窗口点取消按钮后的回调方法
     *
     * @param type 标识，如果有多个地方触发，用于区分触发的是哪次。
     * @param tag  弹出窗口附加的自定义对象
     */
    public void processDialogCancel(int type, Object tag) {
    }

    /**
     * 日期设置回调方法，子类中覆盖此方法
     *
     * @param view  控件标识，如果有多个日期时用这个标识区分。
     * @param year
     * @param month
     * @param day
     */
    public void dateSet(View view, int year, int month, int day) {
    }

    /**
     * 弹出菜单选择事件，子类中覆盖此方法
     *
     * @param view  弹出菜单窗口的控件
     * @param index 选择的菜单项位置
     */
    public void menuSelected(View view, int index) {
    }

    /**
     * 事件设置回调方法，子类中覆盖此方法
     *
     * @param hour
     * @param minute
     */
    public void timeSet(View view, int hour, int minute) {
    }

    /**
     * 列表选择的回调方法，子类覆盖此方法。
     *
     * @param view
     * @param index
     */
    public void listSelected(View view, int index) {
    }

    /**
     * 多选列表回调方法，子类覆盖此方法
     *
     * @param view     触发控件
     * @param selected 选择后的值，长度与文本数组长度相同
     */
    public void mlistSelected(View view, boolean[] selected) {
    }

    private Intent mIntent;

    /**
     * 打开系统通讯录
     */
    public void openContact() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, C.REQUEST_SELECT_CONTACTS);
    }

    /**
     * 系统级ActivityResult,如登陆，打开联系人，设置图片等，开发人员不用理会
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.REQUEST_SELECT_CONTACTS
                && resultCode == Activity.RESULT_OK) {
            mIntent = data;
            getContacts();
        }
    }

    private void getContacts() {
        Uri uri = mIntent.getData();
        Cursor c = managedQuery(uri, null, null, null, null);
        c.moveToFirst();
        ContactInfo contact = getContactPhone(c);
        if (null != contact) {
            if (contact.name != null) {
                selectedContact(contact);
            }
        } else {
            UI.showToast(this, "请开启读取联系人权限");
        }
    }

    private ContactInfo getContactPhone(Cursor cursor) {
        ContactInfo contact = new ContactInfo();
        int column = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        if (cursor.getCount() < 1) {
            return null;
        }
        int phoneNum = cursor.getInt(column);
        if (phoneNum > 0) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            Cursor cs = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            // 遍历所有的电话号码
            if (cs.moveToFirst()) {
                for (; !cs.isAfterLast(); cs.moveToNext()) {
                    int index = cs
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeIndex = cs
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phoneType = cs.getInt(typeIndex);
                    String phone = cs.getString(index);
                    String name = cs.getString(cs
                            .getColumnIndex(PhoneLookup.DISPLAY_NAME));
                    if (phoneType == 2) {
                        contact.mobilePhone = phone;
                    } else {
                        contact.phoneNumber = phone;
                    }
                    contact.name = name;
                }
                if (!cs.isClosed()) {
                    cs.close();
                }
            }
        }
        return contact;
    }

    /**
     * 打开联系人的回调方法，处理选择联系人后的动作
     *
     * @param contact 选中的联系人
     */
    protected void selectedContact(ContactInfo contact) {

    }

    /*
     * 此方法调用的前提：程序被系统销毁
     * (non-Javadoc)
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreVars();
    }

    /**
     * 恢复全局变量。因为系统会在休眠后不定时将静态变量清空， <br/>
     * 因此静态变量必须在赋值后存入硬盘。在使用时恢复。子类覆盖此方法
     */
    protected void restoreVars() {

    }

    /**
     * 上传图片方法，供子类调用
     *
     * @param view    触发此按钮弹出菜单“拍照/选图片”
     * @param options 触发此按钮弹出菜单“拍照/选图片”
     * @param images  菜单图标
     * @param iv      UI上要修改的图片控件
     * @param isCrop  是否剪裁图片
     */
    protected void getPicture(View view, String[] options, int[] images, ImageView iv, boolean isCrop) {
        this.iv = iv;
        ListDialog dialog = new ListDialog(this);
        dialog.setOnSelectedListener(new ListDialog.OnSelectedListener() {
            public void onSelected(View view, int index) {
                picListSelected(view, index, isCrop);
            }
        });
        dialog.show(view, 0, options, images);
    }

    public void picListSelected(View view, int index, boolean isCrop) {
    }

    /**
     * 退出系统关闭推送消息 这个方法在应用没有用极光推送的情况下都传false.
     */
    protected void exit(boolean exitApp) {
        ((MyApplication) getApplication()).exit(exitApp);
    }

    /**
     * 处理通用的点击
     *
     * @author Administrator
     */
    class MyOnClickListener implements OnClickListener {
        public void onClick(View view) {
            int id = view.getId();
            int menu = UI.getId(BaseActivity.this, "menu", "id");
            int ic_home = UI.getId(BaseActivity.this, "ic_home", "id");
            if (id == menu) {
                if (menus != null && menus.length > 0) {
                    showMenu(view, 2, 2, menus, menuImages);
                }
            } else if (id == ic_home) {
                goHome();
            } else if (id == R.id.header_back) {
                back();
            }
        }

    }

    /**
     * 子类重写，返回到主界面
     */
    public void goHome() {

    }

    public void back() {
        finish();
    }

    /**
     * 隐藏对象
     *
     * @param resId
     */
    public void hide(int resId) {
        setV(findViewById(resId), View.GONE);
    }

    public void hide(View view, int resId) {
        setV(view.findViewById(resId), View.GONE);
    }

    public void show(int resId) {
        setV(findViewById(resId), View.VISIBLE);
    }

    public void show(View view, int resId) {
        setV(view.findViewById(resId), View.VISIBLE);
    }

    private void setV(View v, int value) {
        if (v != null) {
            v.setVisibility(value);
        }
    }

    protected RadioButton getRadio(int resId) {
        return (RadioButton) findViewById(resId);
    }

    protected void setRadio(int resId, boolean checked) {
        ((RadioButton) findViewById(resId)).setChecked(checked);
    }

    protected CheckBox getCheck(int resId) {
        return (CheckBox) findViewById(resId);
    }

    protected void setCheck(int resId, boolean checked) {
        ((CheckBox) findViewById(resId)).setChecked(checked);
    }

    protected void copyToClipboard(CharSequence str) {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("label", str);
        cm.setPrimaryClip(cd);
    }

    /**
     * 纯文字分享
     *
     * @param contentString
     */
    public void shareText(String contentString) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, contentString);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, getTitle()));
    }

    /**
     * 分享图片列表
     *
     * @param imageList
     */
    public void shareImage(ArrayList<Uri> imageList) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageList);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, getTitle()));
    }

    /*
     * 当程序处于挂起状态被调用比如home键
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        restoreVars();
    }

    /**
     * 获取缓存大小
     * 图片缓存加数据缓存
     */
    public String getCacheSize() {
        ACache aCache = ACache.get(this);
        double cacheSize = GlideCacheUtil.getInstance().getCacheSizeDouble(this);
        if (null != aCache) {
            cacheSize += aCache.getCacheSizeDouble();
        }
        return GlideCacheUtil.getInstance().getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     */
    public void clearAllCache() {
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        ACache aCache = ACache.get(this);
        if (null != aCache) {
            aCache.clear();
        }
    }

    /**
     * 清除EditText的焦点，并隐藏软键盘
     *
     * @param parentView
     * @param a
     */
    public void clearFocusAndHideSoft(View parentView, final BaseActivity a) {
        parentView.setFocusable(true);
        parentView.setFocusableInTouchMode(true);
        parentView.requestFocus();
        parentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != a.getCurrentFocus()) {
                    InputMethodManager mInputMethodManager = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    return mInputMethodManager.hideSoftInputFromWindow(a.getCurrentFocus()
                            .getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.backward_enter, R.anim.backward_exit);
    }

    /**
     * 弹出新版本提示框
     */
    public void showNewVersionAlert(String path, String msg) {
        new LemonHelloInfo()
                .setTitle("版本更新")
                .setContent("发现新版本,是否立即更新？")
//                .setContent(msg)
                .addAction(new LemonHelloAction("下次再说", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo
                            helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("立即更新", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                        helloView.hide();
                        AppUtil.downLoadApk(BaseActivity.this, path);
                    }
                })).show(this);
    }

    /**
     * 仿美团分页切换功能按钮点击事件
     * 子类重写实现
     *
     * @param view
     * @param o
     * @param position
     */
    public void onPageMenuItemClick(View view, Object o, int position) {

    }

    /**
     * 步骤进度条点击事件
     */
    public void onStepItemClick(View view, Object o, int position, int total) {

    }

    @Override
    public void onSuccess(Result result, String method) {

    }

    @Override
    public void onFault(Result result, String method) {
        if (result.msg.contains("Unauthorized")) {//登陆过期
            new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("登陆已过期，请重新登录！").setContentFontSize(15)
                    .addAction(new LemonHelloAction("立即登陆", (helloView, helloInfo, helloAction) -> {
                        helloView.hide();
                        try {
                            C.TOKEN = "";
                            C.TOKEN_TYPE = "";
                            Class cla = Class.forName("com.kunbo.xiwei.activity.LoginActivity");
                            openIntent(cla, "");
                            app.finishAllActivites();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }))
                    .addAction(new LemonHelloAction("稍后登录", (helloView, helloInfo, helloAction) -> helloView.hide())).show(this);
        }
    }

    public void onDetailItemClickListener(View itemView, int position) {
    }

    /**
     * 页面滚动回到顶部
     *
     * @param sc
     */
    public void scrollToTop(View sc) {
        if (sc instanceof ScrollView)
            ((ScrollView) sc).fullScroll(ScrollView.FOCUS_UP);
        else
            ((NestedScrollView) sc).fullScroll(ScrollView.FOCUS_UP);
    }
}