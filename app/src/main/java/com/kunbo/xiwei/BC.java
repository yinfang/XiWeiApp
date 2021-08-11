package com.kunbo.xiwei;

import android.annotation.SuppressLint;

@SuppressLint("all")
public class BC {
    public static final String heFenKey = "60303571025548ad8285db5fc2a224a7";//和风天气官网申请key
    public static final int PACKAGE_TYPE =1;//1正式网  2开发网
    public static final String BASE_URL_INTERNET;//服务端地址
    public static final String BASE_URL_IMAGE;//图片地址
    public static String keyA = "SEIN2K"; // NFC keyA 默认初始密码FFFFFFFFFFFF
    public static int PAGE_INDEX = 1;//初始页码
    public static int PAGE_SIZE = 15;//每页数量
    public static final String UNSPECIFIED_IMAGE = "image/*";
    public static final int SUCCESS = 0;// 操作成功
    public static final int FAIL = 1;// 请求失败
    public static final String API_DATA_KEY = "data";
    public static final String API_MSG_KEY = "message";
    public static final String API_STATE_KEY = "code";

    public static final String AGENT_KEY = "SFZZZ";//站长
    public static final String AGENT_SUB_KEY = "SFZZZ-F";//副站长
    public static final String MONITOR_KEY = "SFBZ";//班长
    public static final String FEEMANKEY = "SFY";//站收费员
    public static final String TICKET_KEY = "PZY";//票证
    public static final String OFFICE_KEY = "NQ";//内勤

    public static final String SERVICE_TYPE = "BMFWLX";//便民服务类型
    public static final String SPECIAL_TYPE = "TQLX";//特情类型
    public static final String CAR_TYPE = "CXLX";//车型类型
    public static final String COMPLAINT_TYPE = "TSLB";//投诉类别
    public static final String COMPLAINT_FROM = "TSLY";//投诉来源

    public static final String LANE_IN = "RKCD";//入口车道
    public static final String LANE_OUT = "CKCD";//出口车道

    public static final String STATION_ONDUTY = "ZZDBJCFL";//站长记录当班期间
    public static boolean HAS_DONE = false;//数据是否同步完成
    public static boolean CAN_UPLAOD = true;//数据是否可以正常上传，登录过期时等待手动上传

    static {
        if (PACKAGE_TYPE == 1) {//正式网
            BASE_URL_INTERNET = "http://39.98.206.226:8888/";
            BASE_URL_IMAGE = "http://39.98.206.226:8888";
        } else if (PACKAGE_TYPE == 2) {//开发网
            /*BASE_URL_INTERNET = "http://192.168.0.183:8888/";//whg
            BASE_URL_IMAGE = "http://192.168.0.183:8888";*/
            BASE_URL_INTERNET = "http://192.168.0.170:8888/";//线下服务器测试环境
            BASE_URL_IMAGE = "http://192.168.0.170:8888";
        }
    }
}
