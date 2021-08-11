package com.zyf.net.api;

import com.zyf.model.MyData;
import com.zyf.model.MyRow;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @Headers({"Content-type:application/json;charset=UTF-8"}) 1. JSON  格式 请求体
 * RequestBody body = RequestBody.create(JSON, "json格式的字符串");
 * <p>
 * 2.  单文件上传
 * <p>
 * RequestBody requestBody = new MultipartBody.Builder()
 * .setType(MultipartBody.FORM)
 * .addFormDataPart("file", file.getName(), RequestBody.create(PNG, file))
 * .build();
 * 多文件上传
 * MultipartBody.Builder imgRequest = new MultipartBody.Builder().setType(MultipartBody.FORM);
 * for（File file：files）
 * RequestBody requestBody = OkHttpUtil.toRequestBodyOfImage(file);
 * imgRequest.addFormDataPart("mFile", file.getName(), requestBody);
 * <p>
 * 3. 表单      把接口参数通过Map的形式来提交。使用@Body，@POST注解
 * FormBody body = new FormBody.Builder()
 * .add("limit", String.valueOf(LIMIT))
 * .add("page", String.valueOf(pageValue))
 * .build();
 * 4.Json提交  Retrofit会帮我们把对象转换成Json，然后传递给后台服务器。使用@Body ，@POST注解
 * <p>
 * 定义接口
 */
public interface ApiService {
    /**
     * 登录
     */
    @POST("auth/login/android/token")
    Observable<ResponseBody> appLogin(@Query("username") String username, @Query("password") String password);

    /**
     * 注销登录
     */
    @POST("auth/logout/token")
    Observable<ResponseBody> appLoginOut(@Query("token") String token);

    /**
     * 值班表（每月）
     */
    @GET("android/arrange/duty/manage")
    Observable<ResponseBody> getOndutyList(@Query("stationId") String stationId,
                                           @Query("dutyDate") String month);

    /**
     * 统计表数据
     */
    @GET("android/team/duty/list")
    Observable<ResponseBody> getChartData(@Query("stationId") String stationId,
                                          @Query("month") String month);

    /**
     * 获取用户信息
     */
    @GET("android/webapp/user")
    Observable<ResponseBody> getUserInfo();

    /**
     * 获取人员信息
     */
    @GET("android/webapp/getDeptPerson")
    Observable<ResponseBody> getDeptPerson(@Query("deptId") String deptId);

    /**
     * 检测新版本
     */
    @GET("android/version/latestVersion")
    Observable<ResponseBody> checkNewVersion();

    /**
     * 下载apk
     *
     * @param apkUrl
     */
    @GET()
    @Streaming
    Observable<ResponseBody> downloadNewApk(@Url String apkUrl);

    /**
     * 和风天气api
     *
     * @param url
     */
    @GET()
    @Streaming
    Observable<ResponseBody> getWeather(@Url String url);

    /**
     * 班长值班记录提交
     */
    @POST("android/team/duty/save")
    Observable<ResponseBody> uploadMonitorRecorde(@Body MyRow request);

    /**
     * 编辑班长值班记录提交
     */
    @POST("android/team/duty/update")
    Observable<ResponseBody> editMonitorRecorde(@Body MyRow request);

    /**
     * 班长值班历史记录列表
     */
    @GET("android/team/duty/list")
    Observable<ResponseBody> getMonitorHistoryList(@Query("page") String page,
                                                   @Query("limit") String limit,
                                                   @Query("stationId") String stationId,
                                                   @Query("personId") String personId,
                                                   @Query("startDate") String startDate,
                                                   @Query("endDate") String endDate);

    /**
     * 站长值班记录提交
     */
    @POST("android/site/duty/save")
    Observable<ResponseBody> uploadStationRecorde(@Body MyRow request);

    /**
     * 站长值班记录班组信息提交
     */
    @POST("android/site/duty/group/save")
    Observable<ResponseBody> uploadStationTeams(@Body MyData request);

    /**
     * 站长值班历史记录列表
     */
    @GET("android/site/duty/list")
    Observable<ResponseBody> getStationHistoryList(@Query("page") String page,
                                                   @Query("limit") String limit,
                                                   @Query("stationId") String stationId,
                                                   @Query("personId") String personId,
                                                   @Query("startDate") String startDate,
                                                   @Query("endDate") String endDate);

    /**
     * 特情记录提交
     */
    @POST("android/secret/record/save")
    Observable<ResponseBody> uploadSpecialForm(@Body MyRow request);

    /**
     * 安全巡检提交
     */
    @POST("android/safety/audit/save")
    Observable<ResponseBody> uploadSecurityCheck(@Body MyData request);

    /**
     * 稽查登记提交
     */
    @POST("android/audit/work/save")
    Observable<ResponseBody> uploadInspectionForm(@Body MyRow request);

    /**
     * 特情历史记录列表
     */
    @GET("android/secret/record/list")
    Observable<ResponseBody> getSpecialHistoryList(@Query("page") String page,
                                                   @Query("limit") String limit,
                                                   @Query("stationId") String stationId,
                                                   @Query("secretPersonId") String personId,
                                                   @Query("startDate") String startDate,
                                                   @Query("endDate") String endDate);

    /**
     * 获取文明服务类型
     */
    @GET("/android/webapp/dataDictProfiles")
    Observable<ResponseBody> getServiceType(@Query("code") String code);

    /**
     * 获取部门车道信息
     */
    @GET("/android/webapp/queryLaneByDept")
    Observable<ResponseBody> getLaneInfo(@Query("deptId") String code);

    /**
     * 获取安全巡检项目
     */
    @GET("/android/safety/siteconfig/querySiteConfigInfo")
    Observable<ResponseBody> getSecurityItems(@Query("stationId") String stationId);

    /**
     * 文明服务提交
     */
    @POST("/android/civil/convenience/save")
    Observable<ResponseBody> uploadCivilizedService(@Body MyRow request);

    /**
     * 投诉管理提交
     */
    @POST("/android/complainRegistry/save")
    Observable<ResponseBody> uploadComplaintForm(@Body MyRow request);

    /**
     * 治超管理 入口提交
     */
    @POST("android/controlExceed/zulassungsschein/entr/save")
    Observable<ResponseBody> uploadOverHighCarIn(@Body MyRow request);

    /**
     * 治超管理 入口记录查询
     */
    @GET("android/controlExceed/zulassungsschein/findEntrInfoByParam")
    Observable<ResponseBody> queryOverInList(@Query("basicCarLicencePlate") String page,
                                             @Query("sitePassageNumber") String limit);

    /**
     * 治超管理 出口
     */
    @POST("android/controlExceed/zulassungsschein/exit/save")
    Observable<ResponseBody> uploadOverHighCarOut(@Body MyRow request);

    /**
     * 公共图片提交
     */
    @Multipart
    @POST("base/upload/files")
    Observable<ResponseBody> uploadPhotos(@Part List<MultipartBody.Part> partLis);
}