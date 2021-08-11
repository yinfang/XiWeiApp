package com.zyf.net.response;

import android.content.Context;
import android.content.DialogInterface;

import com.zyf.domain.C;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.util.JsonUtil;
import com.zyf.util.Utils;
import com.zyf.view.lemonbubble.LemonBubble;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class OnSuccessAndFaultSub extends DisposableObserver<ResponseBody> implements DialogInterface.OnCancelListener {
    //是否需要显示默认Loading
    private boolean showWaiting = true;
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;
    private Context context;
    private Result result = new Result();
    private String method;
    private boolean hideLemon = true;

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, String method, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.method = method;
        this.context = context;
        LemonBubble.showRoundProgress(this.context, "加载中...");
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param waitingTitle  进度圈显示 文字
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, String method, String waitingTitle, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.method = method;
        this.context = context;
        LemonBubble.showRoundProgress(this.context, Utils.checkNullString(waitingTitle) ? "加载中..." : waitingTitle);
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param waitingTitle  进度圈显示 文字
     * @param hideLemon  执行完是否隐藏进度圈
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, String method, String waitingTitle, boolean hideLemon, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.method = method;
        this.context = context;
        this.hideLemon = hideLemon;
        LemonBubble.showRoundProgress(this.context, Utils.checkNullString(waitingTitle) ? "加载中..." : waitingTitle);
    }

    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param showWaiting                是否需要显示默认Loading
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, String method, Context context, boolean showWaiting) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.method = method;
        this.context = context;
        this.showWaiting = showWaiting;
        if (showWaiting)
            LemonBubble.showRoundProgress(this.context, "加载中...");
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }

    /**
     * 当result等于1回调给调用者，否则自动显示错误信息。
     * 如果在onNext()方法中抛出异常会调用onError()方法
     * ResponseBody  body = response.body();//获取响应体
     * InputStream inputStream = body.byteStream();//获取输入流
     * byte[] bytes = body.bytes();//获取字节数组
     * String str = body.string();//获取字符串数据
     */
    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            String res = responseBody.string();
            if (!Utils.checkNullString(res)) {
                MyRow rowRes = JsonUtil.getRow(res);
                if (rowRes != null) {
                    result.obj = rowRes.get(C.apiDataKey);
                    result.code = rowRes.getInt(C.apiState);
                    result.msg = rowRes.getString(C.apiMsg);
                    if (result.getCode() == C.SUCCESS) {
                        mOnSuccessAndFaultListener.onSuccess(result, method);
                    } else {
                        mOnSuccessAndFaultListener.onFault(result, method);
                        dismissProgressDialog();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.msg = "error:" + e.getMessage();
            mOnSuccessAndFaultListener.onFault(result, method);
        } finally {
            if (hideLemon)
                dismissProgressDialog();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof SocketTimeoutException || e instanceof ConnectException) {//请求超时
                result.msg = "网络连接超时";
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                result.msg = "安全证书异常";
            } else if (e instanceof HttpException) {
                int code = ((HttpException) e).code();
                if (code == C.NET_ERROR) {
                    result.msg = "网络异常，请检查您的网络设置！";
                } else if (code == C.URL_NO_EXIST) {
                    result.msg = "请求的地址不存在";
                } else if (code == 500) {
                    result.msg = "服务器繁忙,请稍后再试!";
                } else if (code == 403) {
                    result.msg = "权限不足,拒绝访问!";
                } else {
                    result.msg = e.getMessage();
                    result.code = code;
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                result.msg = "域名解析失败";
            } else {
                if (Utils.checkNullString(result.msg))
                    result.msg = "error:" + e.getMessage();
            }
            mOnSuccessAndFaultListener.onFault(result, method);
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            dismissProgressDialog();
        }
    }

    @Override
    public void onComplete() {
    }

    private void dismissProgressDialog() {
        if (showWaiting) {
            LemonBubble.hide();
        }
    }
}
