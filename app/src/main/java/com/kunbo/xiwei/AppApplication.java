package com.kunbo.xiwei;

import com.kunbo.xiwei.db.manager.DaoManager;
import com.kunbo.xiwei.view.AMapLocationUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zyf.device.MyApplication;
import com.zyf.domain.C;

public class AppApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化高德地图定位服务
        AMapLocationUtils.getInstance(getContext());
        //注册应用到bugly
        CrashReport.initCrashReport(getApplicationContext(), "f7517b1bbd", false);
        //初始化数据库
        initGreenDao();
        /*//初始化腾讯X5内核
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.d("Application", "X5内核加载是否成功" + b);
            }
        });*/
        C.baseUrl = BC.BASE_URL_INTERNET;
        C.baseImageUrl = BC.BASE_URL_IMAGE;
        C.pageIndex = BC.PAGE_INDEX;
        C.pageSize = BC.PAGE_SIZE;
        C.apiDataKey = BC.API_DATA_KEY;
        C.apiMsg = BC.API_MSG_KEY;
        C.apiState = BC.API_STATE_KEY;
        C.SUCCESS = BC.SUCCESS;
    }

    private void initGreenDao() {
        DaoManager.getInstance(context).init(context);
        /*//插入省份和城市数据
        DaoManager.getCityManagerInstance().insertDatas(ProvinceAndCityDatas.getCityData());
        DaoManager.getProvinceManagerInstance().insertDatas(ProvinceAndCityDatas.getProvincesData());*/
    }
}
