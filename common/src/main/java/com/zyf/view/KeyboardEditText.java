package com.zyf.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.zyf.common.R;
import com.zyf.util.UI;
import com.zyf.util.Utils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 自定义键盘类KeyboardEditText
 */
@SuppressLint("AppCompatCustomView")
public class KeyboardEditText extends EditText implements View.OnClickListener, TextWatcher, KeyboardView.OnKeyboardActionListener {
    private KeyboardView mKeyboardView;
    private Keyboard mKeyboard;
    private PopupWindow mKeyboardWindow;
    private View mDecorView;
    /**
     * 省份简称键盘
     */
    private Keyboard provinceKeyboard;
    /**
     * 数字与大写字母键盘
     */
    private Keyboard numberKeyboard;
    /**
     * 是否是数字键盘
     */
    private boolean isNumber;

    public KeyboardEditText(Context context) {
        this(context, null);
    }

    public KeyboardEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initKeyboardView(context, attrs);
    }

    public void initKeyboardView(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Keyboard);
        if (!array.hasValue(R.styleable.Keyboard_xml)) {
            throw new IllegalArgumentException("you need add keyboard_xml argument!");
        }
        int xmlId = array.getResourceId(R.styleable.Keyboard_xml, 0);
        mKeyboard = new Keyboard(context, xmlId);
        mKeyboardView = (KeyboardView) LayoutInflater.from(context).inflate(R.layout.car_keyboard_view, null);
        //键盘关联keyboard对象

        mKeyboardView.setKeyboard(mKeyboard);
        //关闭键盘按键预览效果，如果按键过小可能会比较适用。
        mKeyboardView.setPreviewEnabled(false);
        //设置键盘事件
        mKeyboardView.setOnKeyboardActionListener(this);
        //添加文字输入动态监听
        this.addTextChangedListener(this);
        //将keyboardview放入popupwindow方便显示以及位置调整。
        mKeyboardWindow = new PopupWindow(mKeyboardView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        array.recycle();
        //设置点击事件，点击后键盘弹起，系统键盘收起。
        setOnClickListener(this);
        //屏蔽当前edittext的系统键盘
        notSystemSoftInput();

        provinceKeyboard = new Keyboard(context, R.xml.province_abbreviation);
        numberKeyboard = new Keyboard(context, R.xml.number_or_letters);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
      /*  if (editable.length() == 1) {
            if (!isChar(editable.toString())) {
                editable.delete(0, 1);
                UI.showToast(getContext(), "请输入省份");
                return;
            }
        }

        if (editable.length() == 2) {
            String str = editable.toString();
            char[] stringArr = str.toCharArray();
            if (!isUpperCase(stringArr[1] + "")) {
                editable.delete(1, 2);
                UI.showToast(getContext(), "请输入大写字母");
                return;
            }
        }
        if (editable.length() == 3) {
            String str = editable.toString();
            char[] stringArr = str.toCharArray();
            if (isChar(stringArr[2] + "")) {
                editable.delete(editable.length() - 1, editable.length());
            }
        }*/
        if (editable.length() > 6) {
            if (!checkNum()) {
                UI.showToast(getContext(), "无效车牌号！");
                requestFocus();
                requestFocusFromTouch();
                hideSysInput();
                showKeyboard();
            }
        }
    }

    /**
     * 判断是否是中文
     */
    public boolean isChar(String str) {
        char[] chars = str.toCharArray();
        boolean isGB2312 = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 3) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isGB2312 = true;
                    break;
                }
            }
        }
        return isGB2312;
    }

    /**
     * 判断字符为大写字母
     */
    public boolean isUpperCase(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    private void changeKeyboard(boolean isNumber) {
        this.isNumber = isNumber;
        if (isNumber) {
            mKeyboardView.setKeyboard(numberKeyboard);
        } else {
            mKeyboardView.setKeyboard(provinceKeyboard);
        }
    }

    /**
     * 根据key code 获取 Keyboard.Key 对象
     *
     * @param primaryCode
     * @return
     */
    private Keyboard.Key getKeyByKeyCode(int primaryCode) {
        if (null != mKeyboard) {
            List<Keyboard.Key> keyList = mKeyboard.getKeys();
            for (int i = 0, size = keyList.size(); i < size; i++) {
                Keyboard.Key key = keyList.get(i);
                int codes[] = key.codes;
                if (codes[0] == primaryCode) {
                    return key;
                }
            }
        }

        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mKeyboardWindow) {
                if (mKeyboardWindow.isShowing()) {
                    mKeyboardWindow.dismiss();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (!focused) {
            hideKeyboard();
        } else {
            hideSysInput();
            showKeyboard();
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    /**
     * 验证车牌
     *
     * @return
     */
    public boolean checkNum() {
        return Utils.isCarNum(this.getText().toString());
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mDecorView = ((Activity) getContext()).getWindow().getDecorView();
        hideSysInput();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideKeyboard();
        mKeyboardWindow = null;
        mKeyboardView = null;
        mKeyboard = null;
        mDecorView = null;
    }

    /**
     * 显示自定义键盘
     */
    private void showKeyboard() {
        if (null != mKeyboardWindow) {
            if (!mKeyboardWindow.isShowing()) {
                mKeyboardView.setKeyboard(mKeyboard);
                mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM, 0, 0);
            }
        }
    }

    /**
     * 屏蔽系统输入法
     */
    private void notSystemSoftInput() {
        if (Build.VERSION.SDK_INT <= 10) {
            setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏自定义键盘
     */
    private void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSysInput() {
        if (this.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        requestFocus();
        requestFocusFromTouch();
        hideSysInput();
        showKeyboard();
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = this.getText();
        //获取光标偏移量下标
        int start = this.getSelectionStart();
        if (primaryCode == -1) {// 省份简称与数字键盘切换
            if (isNumber) {
                changeKeyboard(false);
            } else {
                changeKeyboard(true);
            }
        } else if (primaryCode == -3) {
            if (editable != null && editable.length() > 0) {
                //没有输入内容时软键盘重置为省份简称软键盘
                if (editable.length() == 1) {
                    changeKeyboard(false);
                }
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else {
            if (editable.length() < 8) {
                editable.insert(start, Character.toString((char) primaryCode));
            }
            // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
            if (isChar(editable.toString())) {
                changeKeyboard(true);
            }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
