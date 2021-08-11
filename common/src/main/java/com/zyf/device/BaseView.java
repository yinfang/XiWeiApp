package com.zyf.device;

import com.zyf.model.Result;


/**
 * 类描述:    描述该类的功能
 * 创建人:    wzg
 * 创建时间:  2017/3/9
 * 修改时间:  2017/3/9 20:39
 * 修改备注:  说明本次修改内容
 */
public interface BaseView {
    void showToast(Result result);

    void showProgress();

    void showProgressNoCancel();

    void showProgress(String msg);

    void dismissProgress();

    void onSuccess(Result result);

    /**
     * 请求成功 返回错误
     *
     * @param errorCode
     */
    void onCodeError(int errorCode, String message, int action);

    void showProgressHorizontal();

    /**
     * 请求失败
     */
    void onError(int action);

    String getResString(int resId);

    boolean isLogin();
}
