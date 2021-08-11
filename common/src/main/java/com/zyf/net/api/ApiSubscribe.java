package com.zyf.net.api;

import android.util.Log;

import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.net.OkHttpUtil;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 具体接口调用
 * 建议：把功能模块来分别存放不同的请求方法，比如登录注册类LoginSubscribe、电影类MovieSubscribe
 */
public class ApiSubscribe {
    /**
     * 登录
     */
    public static void appLogin(String username, String password, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().appLogin(username, password);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 注销登录
     */
    public static void appLoginOut(String token, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().appLoginOut(token);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取用户信息
     */
    public static void getUserInfo(DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getUserInfo();
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取人员信息
     */
    public static void getDeptPerson(String deptid, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getDeptPerson(deptid);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 站值班表（每月）
     */
    public static void getOndutyList(String stationId, String month, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getOndutyList(stationId, month);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 统计表
     */
    public static void getChartData(String stationId, String month, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getChartData(stationId, month);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 检测新版本
     */
    public static void checkNewVersion(DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().checkNewVersion();
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 提交班长值班记录
     */
    public static void uploadMonitorRecorde(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadMonitorRecorde(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 编辑班长值班记录
     */
    public static void editMonitorRecorde(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().editMonitorRecorde(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 站长值班历史记录列表
     */
    public static void getMonitorHistoryList(String page, String limit, String stationId, String personId, String startDate, String endDate, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getMonitorHistoryList(page, limit, stationId, personId, startDate, endDate);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }


    /**
     * 提交站长值班记录
     */
    public static void uploadStationRecorde(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadStationRecorde(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 提交站长值班记录班组信息
     */
    public static void uploadStationTeams(MyData request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadStationTeams(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 站长值班历史记录列表
     */
    public static void getStationHistoryList(String page, String limit, String stationId, String personId, String startDate, String endDate, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getStationHistoryList(page, limit, stationId, personId, startDate, endDate);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 特情记录
     */
    public static void uploadSpecailForm(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadSpecialForm(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 安全巡检提交
     */
    public static void uploadSecurityCheck(MyData request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadSecurityCheck(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 稽查登记提交
     */
    public static void uploadInspectionForm(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadInspectionForm(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 特情历史记录列表
     */
    public static void getSpecialHistoryList(String page, String limit, String stationId, String personId, String startDate, String endDate, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getSpecialHistoryList(page, limit, stationId, personId, startDate, endDate);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取安全巡检项目
     */
    public static void getSecurityItems(String stationId, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getSecurityItems(stationId);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取文明服务类型
     */
    public static void getServiceType(String code, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getServiceType(code);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }
    /**
     * 获取文明服务类型
     */
    public static void getLaneInfo(String code, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().getLaneInfo(code);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }
    /**
     * 文明服务
     */
    public static void uploadCivilizedService(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadCivilizedService(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 投诉管理
     */
    public static void uploadComplaintForm(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadComplaintForm(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 治超管理 入口
     */
    public static void uploadOverHighCarIn(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadOverHighCarIn(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 治超管理 入口记录查询
     */
    public static void queryOverInList(String carNo, String passNo, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().queryOverInList(carNo, passNo);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 治超管理 出口
     */
    public static void uploadOverHighCarOut(MyRow request, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable = OkHttpUtil.getInstance().getApiService().uploadOverHighCarOut(request);
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 公共图片提交
     */
    public static void uploadPhotos(MultipartBody.Builder imgRequest, DisposableObserver<ResponseBody> subscriber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.baseUrl)
                .client(uploadImgClient())
                .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())// 支持RxJava
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Observable<ResponseBody> observable = service.uploadPhotos(imgRequest.build().parts());
        OkHttpUtil.getInstance().toSubscribe(observable, subscriber);
    }

    public static OkHttpClient uploadImgClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("————Retrofit————: ", "————网络访问————OkHttp: " + message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .addHeader("Authorization", C.TOKEN_TYPE + C.TOKEN)
                            .addHeader("Accept", "application/json")
                            .build();
                    return chain.proceed(request);
                })
                .build();
        return httpClient;
    }
}

