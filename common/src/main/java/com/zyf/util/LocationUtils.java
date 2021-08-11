package com.zyf.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.zyf.model.CityEntity;
import com.zyf.model.ProvinceAndCityDatas;
import com.zyf.model.ProvinceEntity;
import com.zyf.device.MyApplication;
import com.zyf.model.Point;
import com.zyf.view.lemonhello.LemonHelloAction;
import com.zyf.view.lemonhello.LemonHelloInfo;
import com.zyf.view.lemonhello.LemonHelloView;
import com.zyf.view.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 安卓内置sdk，高德地图定位服务
 *
 * @author zyf
 */
public class LocationUtils {
    private static LocationManager locationManager;
    private volatile static LocationUtils uniqueInstance;
    private static Context mContext;
    private Location mlocation = null;

    private LocationUtils(Context context) {
        mContext = context;
        initLocation();
    }

    //采用Double CheckLock(DCL)实现单例
    public static LocationUtils getInstance(Context context) {
        if (uniqueInstance == null) {
            synchronized (LocationUtils.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new LocationUtils(context);
                }
            }
        }
        return uniqueInstance;
    }

    /**
     * android 内置sdk中提供了locationmanager来获得系统提供的定位服务，可以通过gps、network等定位。
     * * 通过下面的代码能够获取location对象，而通过location可以获得经纬度信息。
     */
    private void initLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //Criteria.ACCURACY_COARSE 低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        // 获取所有可用的位置提供器
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {   //GPS 定位的精准度比较高，但是非常耗电。
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {//Google服务被墙不可用,网络定位的精准度稍差，但耗电量比较少。
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (providerList.contains(LocationManager.PASSIVE_PROVIDER)) {//直取缓存
            provider = LocationManager.PASSIVE_PROVIDER;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    new LemonHelloInfo().setTitle("温馨提示").setContent("当前应用缺少定位权限，定位功能暂时无法使用。请前往设置中心进行权限授权。")
                            .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                }
                            }))
                            .addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                                @Override
                                public void onClick(LemonHelloView helloView, LemonHelloInfo helloInfo, LemonHelloAction helloAction) {
                                    helloView.hide();
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                                    mContext.startActivity(intent);
                                }
                            })).show(mContext);
                }
            } else {
                UI.showToast(mContext, "当前应用缺少定位权限，请前往设置中心进行权限授权!");
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                mContext.startActivity(intent);
            }
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            // 显示当前设备的位置信息
            showLocation(location);
        } else {//当GPS信号弱没获取到位置的时候可从网络获取
            getLngAndLatWithNetwork();//Google服务被墙的解决办法
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        //LocationManager 每隔 5 秒钟会检测一下位置的变化情况，当移动距离超过 10 米的时候，
        // 就会调用 LocationListener 的 onLocationChanged() 方法，并把新的位置信息作为参数传入。
        locationManager.requestLocationUpdates(provider, 5000, 10, locationListener);
    }

    //从网络获取经纬度
    @SuppressLint("MissingPermission")
    private void getLngAndLatWithNetwork() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        showLocation(location);
    }

    //获取经纬度
    public void showLocation(Location location) {
        this.mlocation = location;
    }

    //获取经纬度
    public Location getLocation() {
        return mlocation;
    }

    /**
     * 获取具体位置信息
     * 应异步调用
     */
    public String getAddress(Location location) {
        String result = "";
        double latitude = location.getLatitude();//纬度
        double longitude = location.getLongitude();//经度
        //Geocoder通过经纬度获取具体信息
        Geocoder gc = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> locationList = gc.getFromLocation(latitude, longitude, 1);

            locationList = gc.getFromLocation(latitude, longitude, 1);
            if (locationList.size() > 0) {
                Address address = locationList.get(0);
                result = address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
          /*  if (locationList != null) {
                Address address = locationList.get(0);
                String countryName = address.getCountryName();//国家
                String countryCode = address.getCountryCode();
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subAdminArea = address.getSubAdminArea();//区
                String featureName = address.getFeatureName();//街道

                for (int i = 0; address.getAddressLine(i) != null; i++) {
                    String addressLine = address.getAddressLine(i);
                    //街道名称:广东省深圳市罗湖区蔡屋围一街深圳瑞吉酒店
                    System.out.println("addressLine=====" + addressLine);
                }

                String currentPosition = "latitude is " + latitude//22.545975
                        + "\n" + "longitude is " + longitude//114.101232
                        + "\n" + "countryName is " + countryName//null
                        + "\n" + "countryCode is " + countryCode//CN
                        + "\n" + "adminArea is " + adminArea//广东省
                        + "\n" + "locality is " + locality//深圳市
                        + "\n" + "subAdminArea is " + subAdminArea//null
                        + "\n" + "featureName is " + featureName;//蔡屋围一街深圳瑞吉酒店

                System.out.println(currentPosition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        //当GPS状态发生改变的时候调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    UI.showToast(mContext, "当前GPS可用!");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    UI.showToast(mContext, "当前GPS不在服务区内!");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    UI.showToast(mContext, "当前GPS暂停服务!");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            UI.showToast(mContext, "GPS已开启!");
        }

        @Override
        public void onProviderDisabled(String provider) {
            UI.showToast(mContext, "GPS已关闭!");
        }
    };

    /**
     * 根据城市code查名称,APK内已经内置了省份数据
     * 需查询数据库
     *
     * @param code
     * @return
     */
    public static String getCityName(String code) {
        if (code != null) {
            CityEntity entity = ProvinceAndCityDatas.queryCityByCode(code);
            if (entity != null) {
                return entity.getName();
            }
        }
        return null;
    }

    /**
     * 获得坐标最近的省份。 APK内已经内置了省份数据
     * 需查询数据库
     *
     * @param app
     * @param point 坐标
     * @return
     */
    public static ProvinceEntity getProvinceByPoint(MyApplication app, Point point) {
        if (point == null) {
            return null;
        }
        ProvinceEntity result = null;
        double d0 = Double.MAX_VALUE;
        List<ProvinceEntity> entitys = ProvinceAndCityDatas.getProvincesData();
        for (ProvinceEntity entity : entitys) {
            double latitude = entity.getLatitude();
            double longitude = entity.getLongitude();
            double distance = getDistance(point.latitude, point.longitude,
                    latitude, longitude);
            if (distance < d0) {
                result = entity;
                d0 = distance;
            }
        }
        return result;
    }

    /**
     * 获得坐标最近的城市。 APK内已经内置了城市数据
     * 需查询数据库
     *
     * @param app
     * @param point 坐标
     * @return
     */
    public static CityEntity getCityByPoint(MyApplication app, Point point) {
        if (point == null) {
            return null;
        }
        CityEntity result = null;
        double d0 = Double.MAX_VALUE;
        List<CityEntity> entitys = ProvinceAndCityDatas.getCityData();
        for (CityEntity entity : entitys) {
            double latitude = entity.getLatitude();
            double longitude = entity.getLongitude();
            double distance = getDistance(point.latitude, point.longitude,
                    latitude, longitude);
            if (distance < d0) {
                result = entity;
                d0 = distance;
            }
        }
        return result;
    }

    /**
     * 判断2点之间的距离
     *
     * @param lat1 维度
     * @param lon1 经度
     * @param lat2 维度
     * @param lon2 经度
     * @return
     */
    public static double getDistance(double lat1, double lon1, double lat2,
                                     double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    /**
     * 定位界面销毁时停止定位服务
     */
    public void removeLocManager() {
        if (locationManager != null) {
            uniqueInstance = null;
            locationManager.removeUpdates(locationListener);
        }
    }
}