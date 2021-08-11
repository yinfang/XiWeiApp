package com.zyf.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;
import com.zyf.domain.C;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.OkHttpUtil;
import com.zyf.net.api.ApiService;
import com.zyf.net.response.OnSuccessAndFaultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

/**
 * 下载文件
 */
public class DownloadAndOpenFile {
    private static ProgressDialog pdialog;
    private static ProgressInfo mLastDownloadingInfo;
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;
    private Context context;
    private MyRow row;

    public DownloadAndOpenFile(Context context, MyRow row, OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.context = context;
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.row = row;
        downloadFile(context);
    }

    public void downloadFile(Context context) {
        pdialog = new ProgressDialog(context);
        // dialog.setTitle("Indeterminate");
        pdialog.setMessage(context.getText(com.zyf.common.R.string.downloading_please_wait));
        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdialog.setIndeterminate(false);
        pdialog.setMax(100);
        pdialog.incrementProgressBy(1);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setCancelable(false);
        pdialog.show();

        Retrofit ref = OkHttpUtil.getInstance().getRetrofit();
        if (TextUtils.isEmpty(row.getString("filePath"))) {
            UI.showToast(context, "无效的文件地址！");
            return;
        }
        String url = C.baseUrl + "/star_evaluation" + row.getString("filePath").replace("\\", "/");
        ProgressManager.getInstance().addResponseListener(url, getDownloadListener());
        ref.create(ApiService.class)
                .downloadNewApk(url)
                .subscribeOn(Schedulers.io())//请求网络 在调度者的io线程
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, File>() {
                         @Override
                         public File apply(ResponseBody responseBody) {
                             return FileUtil.saveFile(context, responseBody.byteStream(), row.getString("fileName"));
                         }
                     }
                )
                .subscribe(new DisposableObserver<File>() {
                    @Override
                    public void onNext(File file) {
                        if (pdialog != null) {
                            pdialog.dismiss();
                        }
                        Result result = new Result();
                        result.obj = file;
                        mOnSuccessAndFaultListener.onSuccess(result, "download");
                    }

                    @Override
                    public void onError(Throwable e) {
                        UI.showToast(context, "文件下载失败," + e.getMessage());
                        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof HttpException) {//请求超时
                            UI.showToast(context, "网络连接超时,请检查您的网络设置！");
                        } else if (e instanceof UnknownHostException) {//域名解析失败
                            UI.showToast(context, "域名解析失败！");
                        } else {
                            UI.showToast(context, "文档下载失败！\n" + e.getMessage());
                        }
                        if (pdialog != null) {
                            pdialog.dismiss();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (pdialog != null) {
                            pdialog.dismiss();
                        }
                    }
                });
    }

    private static ProgressListener getDownloadListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                // 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
                // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
                // 这里我就取最新的下载进度用来展示,顺便展示下 id 的用法
                if (mLastDownloadingInfo == null) {
                    mLastDownloadingInfo = progressInfo;
                }
                //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
                if (progressInfo.getId() < mLastDownloadingInfo.getId()) {
                    return;
                } else if (progressInfo.getId() > mLastDownloadingInfo.getId()) {
                    mLastDownloadingInfo = progressInfo;
                }
                int progress = mLastDownloadingInfo.getPercent();
                pdialog.setProgress(progress);
            }

            @Override
            public void onError(long id, Exception e) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (pdialog != null) {
                            pdialog.dismiss();
                        }
                    }
                });
            }
        };
    }

    /**
     * QbSdk打开文档
     * 弹出覆盖页面查看文档，适用于嵌套Viewpager 滑动切换时点击查看文档
     */
    public static void openFileWithQbsdk(Context context, File file) {
        HashMap<String, String> params = new HashMap<>();
        params.put("local", "true");//“true”表示是进入文件查看器，如果不设置或设置为“false”，则进入 miniqb 浏览器模式,非必填项
        JSONObject Object = new JSONObject();
        try {
            Object.put("pkgName", context.getApplicationContext().getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("style", "1");//“1”表示文件查看器使用微信的 UI 样式。不设置此 key或设置错误值，则为默认 UI 样式
        params.put("menuData", Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, file.getAbsolutePath(), params, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
        if (ret == -1 || ret == -6) {
            UI.showToast(context, "文档加载失败，路径错误！");
        }
    }

    /**
     * TBS打开文档
     * 在本页面查看，单个文件查看，new TbsReaderView只能执行一次，退出界面时需销毁mTbsReaderView
     *
     * @param file
     * @param docName
     */
    public static void openFileWithTbs(Context context, TbsReaderView mTbsReaderView, File file, String docName) {
        mTbsReaderView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //加载asserts资源文件
//        String path = FileUtil.saveAssets(this, "3a3a.doc").getAbsolutePath();
        String path = file.getAbsolutePath();
        String tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";
        //解决没有TbsReaderTemp文件夹存在导致加载文件失败
        File bsReaderTempFile = new File(path);
        if (!bsReaderTempFile.exists()) {
            boolean mkdir = bsReaderTempFile.mkdir();
            if (!mkdir) {
                UI.showToast(context, "创建/TbsReaderTemp失败！！！！！");
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        bundle.putString("tempPath", tbsReaderTemp);
        boolean result = mTbsReaderView.preOpen(getFileType(docName), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        } else {
            UI.showToast(context, "预览失败,暂不支持" + getFileType(docName) + "文件格式!");
        }
    }

    /**
     * 文件后缀名的判断
     *
     * @param paramString
     * @return
     */
    private static String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }
}
