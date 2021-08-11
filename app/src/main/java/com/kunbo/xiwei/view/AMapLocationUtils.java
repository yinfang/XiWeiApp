package com.kunbo.xiwei.view;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zyf.util.UI;

/**
 * 高德地图定位服务
 *
 * @author zyf
 */
public class AMapLocationUtils {
    private AMapLocationClient mLocationClient = null;
    private volatile static AMapLocationUtils uniqueInstance;
    private static Context mContext;
    private AMapLocation mlocation = null;

    private AMapLocationUtils(Context context) {
        mContext = context;
        initLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static AMapLocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (AMapLocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AMapLocationUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    private void initLocation() {
        //声明定位回调监听器
        AMapLocationListener mLocationListener = aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    showLocation(aMapLocation);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    UI.showToast(mContext, aMapLocation.getErrorInfo());
                }
            }
        };
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //声明AMapLocationClientOption对象
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //Hight_Accuracy，高精度模式,会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //Battery_Saving，低功耗模式,不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）。
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，需要在室外环境下才可以成功定位。注意，自 v2.9.0 版本之后，仅设备定位模式下支持返回地址描述信息。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            //获取一次定位结果：默认为false。
            mLocationOption.setOnceLocation(false);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(1000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否允许模拟位置,默认为true，允许模拟位置
            mLocationOption.setMockEnable(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //缓存机制，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //获取经纬度
    public void showLocation(AMapLocation location) {
        this.mlocation = location;
    }

    //获取经纬度
    public AMapLocation getLocation() {
        return mlocation;
    }

    /**
     * 定位界面销毁时停止定位服务
     */
    public void removeLocManager() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        }
    }
}