package com.kunbo.xiwei.db.manager;

import android.content.Context;

public class DaoUtils {
    public static Context context;
    private static StationOndutyManager stationOndutyManager;
    private static MonitorOndutyManager monitorOndutyManager;
    private static SpecialDoneManager mspecialDoneManager;
    private static CivilizedServiceManager mcivilizedServiceManager;
    private static ComplaintFormManager mcomplaintFormManager;
    private static OverHighCarInManager moverHighInManager;
    private static OverHighCarOutManager moverHighOutManager;
    private static StationEmployeeManager mstationEmployeeManager;
    private static TeamUsersManager moteamUsersManager;
    private static SpecialTypesManager mspecialTypesManager;
    private static CivilizedTypesManager mcivilizedTypesManager;
    private static LaneTypesManager mlaneTypesManager;
    private static SecurityCheckManager mSecurityCheckManager;
    private static SecurityItemsManager mlSecurityItemsManager;
    private static InspectionFormManager mInspectionFormManager;

    public static void init(Context context) {
        DaoUtils.context = context.getApplicationContext();
    }

    /**
     * 单列模式获取站长值班记录表对象
     *
     * @return
     */
    public static StationOndutyManager getStationOndutyInstance() {
        if (stationOndutyManager == null) {
            stationOndutyManager = new StationOndutyManager(context);
        }
        return stationOndutyManager;
    }

    /**
     * 单列模式获取班长值班记录表对象
     *
     * @return
     */
    public static MonitorOndutyManager getMonitorOndutyInstance() {
        if (monitorOndutyManager == null) {
            monitorOndutyManager = new MonitorOndutyManager(context);
        }
        return monitorOndutyManager;
    }

    /**
     * 单列模式获取特情记录表对象
     *
     * @return
     */
    public static SpecialDoneManager getSpecialDoneInstance() {
        if (mspecialDoneManager == null) {
            mspecialDoneManager = new SpecialDoneManager(context);
        }
        return mspecialDoneManager;
    }

    /**
     * 单列模式获取文明服务记录表对象
     *
     * @return
     */
    public static CivilizedServiceManager getCivilizedServiceInstance() {
        if (mcivilizedServiceManager == null) {
            mcivilizedServiceManager = new CivilizedServiceManager(context);
        }
        return mcivilizedServiceManager;
    }

    /**
     * 单列模式获取投诉管理记录表对象
     *
     * @return
     */
    public static ComplaintFormManager getComplaintFormInstance() {
        if (mcomplaintFormManager == null) {
            mcomplaintFormManager = new ComplaintFormManager(context);
        }
        return mcomplaintFormManager;
    }

    /**
     * 单列模式获取治超入口记录表对象
     *
     * @return
     */
    public static OverHighCarInManager getOverHighInInstance() {
        if (moverHighInManager == null) {
            moverHighInManager = new OverHighCarInManager(context);
        }
        return moverHighInManager;
    }

    /**
     * 单列模式获取治超出口记录表对象
     *
     * @return
     */
    public static OverHighCarOutManager getOverHighOutInstance() {
        if (moverHighOutManager == null) {
            moverHighOutManager = new OverHighCarOutManager(context);
        }
        return moverHighOutManager;
    }

    /**
     * 单列模式获取安全巡检记录表对象
     *
     * @return
     */
    public static SecurityCheckManager getSecurityCheckInstance() {
        if (mSecurityCheckManager == null) {
            mSecurityCheckManager = new SecurityCheckManager(context);
        }
        return mSecurityCheckManager;
    }

    /**
     * 单列模式获取站人员信息表对象
     *
     * @return
     */
    public static StationEmployeeManager getStationEmployeeInstance() {
        if (mstationEmployeeManager == null) {
            mstationEmployeeManager = new StationEmployeeManager(context);
        }
        return mstationEmployeeManager;
    }

    /**
     * 单列模式获取班组人员信息表对象
     *
     * @return
     */
    public static TeamUsersManager getTeamUsersInstance() {
        if (moteamUsersManager == null) {
            moteamUsersManager = new TeamUsersManager(context);
        }
        return moteamUsersManager;
    }

    /**
     * 单列模式获取特请类型表对象
     *
     * @return
     */
    public static SpecialTypesManager getSpecialTypesInstance() {
        if (mspecialTypesManager == null) {
            mspecialTypesManager = new SpecialTypesManager(context);
        }
        return mspecialTypesManager;
    }

    /**
     * 单列模式获取文明服务类型表对象
     *
     * @return
     */
    public static CivilizedTypesManager getCivilizedTypesInstance() {
        if (mcivilizedTypesManager == null) {
            mcivilizedTypesManager = new CivilizedTypesManager(context);
        }
        return mcivilizedTypesManager;
    }

    /**
     * 单列模式获取车道类型表对象
     *
     * @return
     */
    public static LaneTypesManager getLaneTypesInstance() {
        if (mlaneTypesManager == null) {
            mlaneTypesManager = new LaneTypesManager(context);
        }
        return mlaneTypesManager;
    }

    /**
     * 单列模式获取安全巡检项目表对象
     *
     * @return
     */
    public static SecurityItemsManager getSecurityItemInstance() {
        if (mlSecurityItemsManager == null) {
            mlSecurityItemsManager = new SecurityItemsManager(context);
        }
        return mlSecurityItemsManager;
    }

    /**
     * 单列模式获取稽查登记表对象
     *
     * @return
     */
    public static InspectionFormManager getInspectionFormInstance() {
        if (mInspectionFormManager == null) {
            mInspectionFormManager = new InspectionFormManager(context);
        }
        return mInspectionFormManager;
    }
}
