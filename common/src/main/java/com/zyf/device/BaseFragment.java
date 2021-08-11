package com.zyf.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.zyf.common.R;
import com.zyf.domain.C;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.util.KeyboardUtil;
import com.zyf.util.dialog.ListDialog;
import com.zyf.util.dialog.MListDialog;
import com.zyf.util.dialog.MyDateDialog;
import com.zyf.util.dialog.MyDatePickerDialog;
import com.zyf.util.dialog.MyTimeDialog;
import com.zyf.util.dialog.MyTimePickerDialog;
import com.zyf.util.image.GlideUtil;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.view.lemonhello.LemonHelloView;
import com.zyf.view.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.Date;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, OnSuccessAndFaultListener {
    private GlideUtil glideUtil = GlideUtil.getInstance();
    // 要填充的图片控件
    protected ImageView iv;
    private KeyboardUtil keyboardUtil;

    /**
     * 隐藏对象 onCreateView之后调用才有用
     *
     * @param resId
     */
    public void hide(int resId) {
        if (getView() != null) {
            setV(getView().findViewById(resId), View.GONE);
        }
    }

    public void hide(View view, int resId) {
        setV(view.findViewById(resId), View.GONE);
    }

    /**
     * 次此方法在onCreateView之后调用才有用
     *
     * @param resId
     */
    public void show(int resId) {
        if (getView() != null) {
            setV(getView().findViewById(resId), View.VISIBLE);
        }
    }

    public void show(View view, int resId) {
        setV(view.findViewById(resId), View.VISIBLE);
    }

    private void setV(View v, int value) {
        if (v != null) {
            v.setVisibility(value);
        }
    }

    public void setHeaderTitle(int resId) {
        setHeaderTitle(getString(resId));
    }

    public void setHeaderTitle(CharSequence title) {
        if (title != null) {
            if (getView() != null) {
                TextView tvTitle = getView().findViewById(R.id.header_title);
                if (tvTitle != null) {
                    tvTitle.setText(title);
                }
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
        Intent intent = new Intent(getContext(), clazz);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("title", title.toString().replaceAll("\n", " "));
        if (requestCode > 0) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
        getActivity().overridePendingTransition(R.anim.forward_enter, R.anim.forward_exit);

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

    public void onClick(View view) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onHide();
        } else {
            onShow();
        }
    }

    protected void onHide() {
    }

    protected void onShow() {
    }

    public void setImage(ImageView imageView, Object res) {
        glideUtil.setImage(getContext(), imageView, res, 0);
    }


    public void setImage(ImageView imageView, Object model, boolean isCircle) {
        glideUtil.setImage(getContext(), imageView, model, isCircle);
    }

    public void setImage(ImageView imageView, Object model, boolean isCircle, int rounded) {
        glideUtil.setImage(getContext(), imageView, model, false, rounded);
    }

    public void setOverrideImage(ImageView imageView, Object model, int width, int height, boolean isCircle, int rounded) {
        glideUtil.loadOverrideImage(getContext(), imageView, model, width, height, isCircle, rounded);
    }

    public void setEText(int id, String ss) {
        View view = getView().findViewById(id);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(ss);
            }
            if (view instanceof EditText) {
                ((EditText) view).setText(ss);
            }
        }
    }

    public String getEText(int id) {
        View et = getView().findViewById(id);
        String ss = "";
        if (et != null) {
            if (et instanceof EditText) {
                ss = String.valueOf(((EditText) et).getText());
            } else {
                ss = String.valueOf(((TextView) et).getText());
            }
        }
        return ss;
    }

    public void setEColor(int resId, int color) {
        TextView tv = getView().findViewById(resId);
        if (tv != null)
            tv.setTextColor(getResources().getColor(color));
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
        showMListDialog(view, 0, captions, selected);
    }

    /**
     * 弹出普通单选列表，子类覆盖回调方法listSelected
     *
     * @param view       触发控件
     * @param resTitleId 显示标题栏文字，0 不显示标题栏
     * @param captions   列表文本
     * @param images     列表图标, 数量必须与captions保持一致
     */
    protected void showListDialog(View view, int resTitleId, String[] captions,
                                  int[] images) {
        ListDialog dialog = new ListDialog(getContext());
        dialog.setOnSelectedListener(new ListDialog.OnSelectedListener() {
            public void onSelected(View view, int index) {
                listSelected(view, index);
            }
        });
        dialog.show(view, resTitleId, captions, images);
    }

    /**
     * 弹出多选列表，子类覆盖回调方法mlistSelected
     *
     * @param view       触发控件
     * @param resTitleId 标题文字，0 不显示标题
     * @param captions   列表文本
     * @param selected   初始值，长度必须与captions相同
     */
    protected void showMListDialog(final View view, int resTitleId,
                                   String[] captions, boolean[] selected) {
        final MListDialog dialog = new MListDialog(getContext());
        dialog.setOnSelectedListener(new MListDialog.OnSelectedListener() {
            public void onSelected(View view, boolean[] selected) {
                mlistSelected(view, selected);
            }
        });
        dialog.show(view, resTitleId, captions, selected,true);
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

    /**
     * 弹出时间选择框，子类需要实现回调方法 timeSet
     *
     * @param view   时间控件，必须是TextView /Button/EditText 等
     * @param hour
     * @param minute
     */
    public void showTimeDialog(final View view, String title, int hour, int minute) {
        MyTimeDialog d = new MyTimeDialog(getContext());
        d.setOnTimeSetListener(new MyTimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker v, int hour, int minute) {
                /*if (view instanceof TextView) {
                    String time = Utils.getTimeString(hour, minute);
                    ((TextView) view).setText(time);
                }*/
                String h = hour < 10 ? "0" + hour : String.valueOf(hour);
                String m = minute < 10 ? "0" + minute : String.valueOf(minute);
                timeSet(view, h, m);// 回调子类方法
            }
        });
        d.show(title, hour, minute);
    }

    /**
     * 弹出日期选择框,子类需要实现回调方法 dateSet
     *
     * @param view    日期控件，必须是TextView /Button/EditText 等，格式2014-12-30
     * @param year    传0时默认获取当前日期
     * @param month
     * @param day
     * @param showday 是否显示天
     */
    public void showDateDialog(final View view, String title, int year, int month, int day,
                               boolean showday) {
        if (year == 0) {
            String date = C.df_yMd.format(new Date());
            String[] dats = date.split("-");
            year = Integer.parseInt(dats[0]);
            month = Integer.parseInt(dats[1]);
            day = Integer.parseInt(dats[2]);
        }
        MyDateDialog d = new MyDateDialog(getContext());
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

    /**
     * 时间设置回调方法，子类中覆盖此方法
     */
    public void timeSet(View view, String hour, String minute) {
    }

    /**
     * 日期设置回调方法，子类中覆盖此方法
     */
    public void dateSet(View view, int year, int month, int day) {
    }

    /**
     * 上传图片方法，供子类调用
     *
     * @param view    触发此按钮弹出菜单“拍照/选图片”
     * @param options 触发此按钮弹出菜单“拍照/选图片”
     * @param images  菜单图标
     * @param isCrop  是否剪裁图片
     */
    protected void getPicture(View view, String[] options, int[] images, ImageView iv, boolean isCrop) {
        this.iv = iv;
        ListDialog dialog = new ListDialog(getContext());
        dialog.setOnSelectedListener(new ListDialog.OnSelectedListener() {
            public void onSelected(View view, int index) {
                picListSelected(view, index, isCrop);
            }
        });
        dialog.show(view, 0, options, images);
    }

    public void picListSelected(View view, int index, boolean isCrop) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        onCreateView(view, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onShow();
    }

    public void onCreateView(View view, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    protected abstract int getLayoutId();

    @Override
    public void onSuccess(Result result, String method) {

    }

    @Override
    public void onFault(Result result, String method) {
        if (result.msg.contains("Unauthorized")) {//登陆过期
            new LemonHelloInfo().setTitle("温馨提示").setTitleFontSize(18).setContent("登陆已过期，请重新登录！").setContentFontSize(15)
                    .addAction(new LemonHelloAction("立即登陆", new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                            helloView.hide();
                            try {
                                C.TOKEN = "";
                                C.TOKEN_TYPE = "";
                                Class cla = Class.forName("com.kunbo.xiwei.activity.LoginActivity");
                                openIntent(cla, "");
                                MyApplication.getInstance().finishAllActivites();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }))
                    .addAction(new LemonHelloAction("稍后登录", new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                            helloView.hide();
                        }
                    })).show(getContext());
        }
    }


    /**
     * 页面滚动回到顶部
     * @param sc
     */
    public void scrollToTop(View sc) {
        if (sc instanceof ScrollView)
            ((ScrollView)sc).fullScroll(ScrollView.FOCUS_UP);
        else
            ((NestedScrollView)sc).fullScroll(ScrollView.FOCUS_UP);
    }
}
