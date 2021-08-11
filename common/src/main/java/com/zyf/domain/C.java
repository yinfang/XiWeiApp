package com.zyf.domain;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class C {

    //---------------------------------------------数字格式化--------------------------------------------------
    public static final DecimalFormat nf_i = new DecimalFormat("#,##0");// integer
    public static final DecimalFormat nf_a = new DecimalFormat("###0.00");// amount
    public static final DecimalFormat nf_l = new DecimalFormat("###0.000000");// GPS
    public static final DecimalFormat nf_s = new DecimalFormat("#,###.#");// 1位小数

    //----------------------------------------------日期格式化--------------------------------------------------
    public static Locale loc = Locale.getDefault();
    public static final DateFormat df_y = new SimpleDateFormat("yyyy", loc);
    public static final DateFormat df_yM = new SimpleDateFormat("yyyy-MM", loc);
    public static final DateFormat df_yMd = new SimpleDateFormat("yyyy-MM-dd", loc);
    public static final DateFormat df_ydotMdotd = new SimpleDateFormat("yyyy.MM.dd", loc);
    public static final DateFormat dff_yMdHm = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss", loc);
    public static final DateFormat df_yMdHm = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", loc);
    public static final DateFormat df_yMdH = new SimpleDateFormat(
            "yyyy-MM-dd HH:00", loc);
    public static final DateFormat df_yMdHms = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", loc);
    public static final DateFormat df_dot_yMdHms = new SimpleDateFormat(
            "yyyy.MM.dd HH:mm:ss", loc);
    public static final DateFormat dfs_yMd = new SimpleDateFormat(
            "yyyy年MM月dd日", loc);
    public static final DateFormat df_Hm = new SimpleDateFormat("HH:mm", loc);
    public static final DateFormat df_Hms = new SimpleDateFormat("HH:mm:ss", loc);
    //----------------------------------------------网络及app配置相关--------------------------------------------------
    public static final String DB_NAME = "xiwei.db";
    public static final int large = 500;// 裁剪的头像大图
    public static final int small = 150;// 裁剪的头像小图
    public static final int compress = 80;//质量压缩比例
    public static final String UNSPECIFIED_IMAGE = "image/*";
    public static final String IMAGE_CACHE_DIR = "images";

    //url不存在
    public static int URL_NO_EXIST = 404;//url不存在
    public static int NET_ERROR = 504;// 网络错误
    public static int SUCCESS = 200;// 操作成功

    public static final int REQUEST_SELECT_CONTACTS = 10002;
    public static final int REQUEST_FROM_CAMERA = 10003;
    public static final int REQUEST_FROM_PICTURE = 10004;
    public static final int REQUEST_HANDLE_PICTURE = 10005;

    public static final String APP_ID = "APP_ID";
    public static int dialogTheme = -1;//自定义dialog样式默认白色（android.R.style.Theme_Holo_DialogWhenLarge 灰色）
    public static String TOKEN_TYPE = "";
    public static String TOKEN = "";
    public static String baseUrl = "http://111.231.220.201:9080";
    public static String baseImageUrl;
    public static int pageIndex = 1;
    public static int pageSize = 10;
    public static String apiDataKey = "data";//json 数据对象的key可在项目中配置
    public static String apiState = "code";
    public static String apiMsg = "message";
    public static final String IS_REPEAT_REQUEST= "REPEAT_REQUEST";//取消重复的网络请求

}
