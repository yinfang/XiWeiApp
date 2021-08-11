package com.kunbo.xiwei.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.activity.MainActivity;
import com.kunbo.xiwei.db.entity.CivilizedServiceForm;
import com.kunbo.xiwei.db.entity.CivilizedTypes;
import com.kunbo.xiwei.db.entity.ComplaintForm;
import com.kunbo.xiwei.db.entity.InspectionForm;
import com.kunbo.xiwei.db.entity.LaneTypes;
import com.kunbo.xiwei.db.entity.MonitorOndutyRecorde;
import com.kunbo.xiwei.db.entity.OverHighCarIn;
import com.kunbo.xiwei.db.entity.OverHighCarOut;
import com.kunbo.xiwei.db.entity.SecurityCheckRecorde;
import com.kunbo.xiwei.db.entity.SecurityItems;
import com.kunbo.xiwei.db.entity.SpecialForm;
import com.kunbo.xiwei.db.entity.SpecialTypes;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.entity.StationOndutyRecorde;
import com.kunbo.xiwei.db.entity.TeamUsers;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.SPUtil;
import com.zyf.util.UI;
import com.zyf.view.lemonbubble.LemonBubble;

import java.util.List;

import static com.zyf.net.api.ApiSubscribe.getDeptPerson;
import static com.zyf.net.api.ApiSubscribe.getLaneInfo;
import static com.zyf.net.api.ApiSubscribe.getSecurityItems;
import static com.zyf.net.api.ApiSubscribe.getServiceType;

/**
 * 交班  注销 同步人员信息 工具类弹出框
 */
public class SyncAndSettingUtils implements View.OnClickListener, OnSuccessAndFaultListener {
    private Context context;
    private boolean isFirst = false;
    private static int uploadNum = 0;

    public SyncAndSettingUtils(@NonNull Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
            BC.HAS_DONE = false;
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void sycnData(Context context, boolean isFirst) {
        this.isFirst = isFirst;
        getDeptPerson(User.getInstance().getStationId(), new OnSuccessAndFaultSub(this, "getDeptPerson", "正在同步站点相关信息...", false, context));
    }

    /**
     * 查询数据库是否有未提交的数据
     *
     * @return
     */
    public static int checkHasData() {
        uploadNum = 0;
        List<MonitorOndutyRecorde> list = DaoUtils.getMonitorOndutyInstance().loadAllData();
        List<StationOndutyRecorde> list1 = DaoUtils.getStationOndutyInstance().loadAllData();
        List<CivilizedServiceForm> list2 = DaoUtils.getCivilizedServiceInstance().loadAllData();
        List<SpecialForm> list3 = DaoUtils.getSpecialDoneInstance().loadAllData();
        List<ComplaintForm> list4 = DaoUtils.getComplaintFormInstance().loadAllData();
        List<OverHighCarIn> list5 = DaoUtils.getOverHighInInstance().loadAllData();
        List<OverHighCarOut> list6 = DaoUtils.getOverHighOutInstance().loadAllData();
        List<SecurityCheckRecorde> list7 = DaoUtils.getSecurityCheckInstance().loadAllData();
        List<InspectionForm> list8 = DaoUtils.getInspectionFormInstance().loadAllData();
        if (list.size() > 0) {
            for (MonitorOndutyRecorde recorde0 : list) {
                if (recorde0.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list1.size() > 0) {
            for (StationOndutyRecorde recorde1 : list1) {
                if (recorde1.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list2.size() > 0) {
            for (CivilizedServiceForm recorde2 : list2) {
                if (recorde2.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list3.size() > 0) {
            for (SpecialForm recorde3 : list3) {
                if (recorde3.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list4.size() > 0) {
            for (ComplaintForm recorde4 : list4) {
                if (recorde4.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list5.size() > 0) {
            for (OverHighCarIn recorde5 : list5) {
                if (recorde5.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list6.size() > 0) {
            for (OverHighCarOut recorde6 : list6) {
                if (recorde6.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        if (list7.size() > 0) {
            for (SecurityCheckRecorde recorde7 : list7) {
                uploadNum++;
            }
        }
        if (list8.size() > 0) {
            for (InspectionForm recorde8 : list8) {
                if (recorde8.getIsSubmit()) {
                    uploadNum++;
                }
            }
        }
        return uploadNum;
    }

    /*注销  交班 清空本地数据*/
    public static void clearSaves() {
        DaoUtils.getMonitorOndutyInstance().deleteAll();
        DaoUtils.getStationOndutyInstance().deleteAll();
        DaoUtils.getCivilizedServiceInstance().deleteAll();
        DaoUtils.getSpecialDoneInstance().deleteAll();
        DaoUtils.getComplaintFormInstance().deleteAll();
        DaoUtils.getOverHighInInstance().deleteAll();
        DaoUtils.getOverHighOutInstance().deleteAll();
        DaoUtils.getSecurityCheckInstance().deleteAll();
        DaoUtils.getInspectionFormInstance().deleteAll();
    }

    @Override
    public void onSuccess(Result result, String method) {
        switch (method) {
            case "getDeptPerson"://人员信息
                DaoUtils.getStationEmployeeInstance().deleteAll();//先删除所有站人员信息再插入
                MyRow ro = (MyRow) result.obj;
                //收费员信息
                MyData feeMans = (MyData) ro.get("tollList");
                if (feeMans != null && feeMans.size() > 0) {
                    insertData(feeMans);
                }
                //班长信息
                MyData monitors = (MyData) ro.get("monitorList");
                if (monitors != null && monitors.size() > 0) {
                    insertData(monitors);
                }
                //票证信息
                MyData tickets = (MyData) ro.get("ticketList");
                if (tickets != null && tickets.size() > 0) {
                    insertData(tickets);
                }
                //站长信息
                MyData agents = (MyData) ro.get("siteAgentList");
                if (agents != null && agents.size() > 0) {
                    insertData(agents);
                }
                //内勤信息
                MyData offices = (MyData) ro.get("secretaryList");
                if (offices != null && offices.size() > 0) {
                    insertData(offices);
                }
                //班人员信息
                MyRow teamRow = (MyRow) ro.get("teamPersonList");
                if (teamRow != null) {
                    MyData teamPersons = new MyData();
                    for (String ss : teamRow.keySet()) {
                        if (ss.equals(User.getInstance().getTeamName())) {
                            teamPersons = (MyData) teamRow.get(ss);
                        }
                    }
                    if (teamPersons != null && teamPersons.size() > 0) {
                        DaoUtils.getTeamUsersInstance().deleteAll();//先删除班组人员数据
                        for (MyRow rr : teamPersons) {
                            TeamUsers en = new TeamUsers(Long.parseLong(rr.getString("id")),
                                    rr.getString("deptId"), rr.getString("postId"),
                                    rr.getString("name"), rr.getString("teamId"),
                                    rr.getString("nation"), rr.getString("idCard"),
                                    rr.getString("nativePlace"), rr.getString("personType"),
                                    rr.getString("contactInformation"));
                            DaoUtils.getTeamUsersInstance().insertData(en);
                        }
                    }
                }
                getLaneInfo(User.getInstance().getStationId(), new OnSuccessAndFaultSub(this, "getLaneInfo", context, false));
                break;
            case "getLaneInfo"://车道信息
                MyData lanes = (MyData) result.getObj();
                if (lanes != null && lanes.size() > 0) {
                    insertTypes(lanes, 0);
                }
                getServiceType(BC.SPECIAL_TYPE, new OnSuccessAndFaultSub(this, "getSpecialType", context, false));
                break;
            case "getSpecialType"://特情类型
                MyData specialTypes = (MyData) result.getObj();
                if (specialTypes != null && specialTypes.size() > 0) {
                    insertTypes(specialTypes, 1);
                }
                getServiceType(BC.SERVICE_TYPE, new OnSuccessAndFaultSub(this, "getServiceType", context, false));
                break;
            case "getServiceType"://服务类型
                DaoUtils.getCivilizedTypesInstance().deleteAll();//先删除所有再插入
                MyData serviceTypes = (MyData) result.getObj();
                if (serviceTypes != null && serviceTypes.size() > 0) {
                    insertTypes(serviceTypes, 2);
                }
                getServiceType(BC.COMPLAINT_FROM, new OnSuccessAndFaultSub(this, "getComplaintFrom", context, false));
                break;
            case "getComplaintFrom"://投诉来源
                MyData complaintFrom = (MyData) result.getObj();
                if (complaintFrom != null && complaintFrom.size() > 0) {
                    insertTypes(complaintFrom, 3);
                }
                getServiceType(BC.COMPLAINT_TYPE, new OnSuccessAndFaultSub(this, "getComplaintType", context, false));
                break;
            case "getComplaintType"://投诉类别
                MyData complaintTypes = (MyData) result.getObj();
                if (complaintTypes != null && complaintTypes.size() > 0) {
                    insertTypes(complaintTypes, 4);
                }
                getSecurityItems(User.getInstance().getStationId(), new OnSuccessAndFaultSub(this, "getSecurityItems", context, false));
                break;
            case "getSecurityItems"://安全巡检项目
                DaoUtils.getSecurityItemInstance().deleteAll();
                MyData data = (MyData) result.obj;
                if (data != null && data.size() > 0) {
                    for (MyRow rr : data) {
                        SecurityItems en = new SecurityItems(Long.parseLong(rr.getString("id")), false,
                                rr.getString("examinePeriodDesc"), rr.getString("examineTime"), rr.getString("examineSite"),
                                rr.getString("examineLocation"), rr.getString("examineProject"),
                                rr.getString("examineContext"), "", User.getInstance().getName(), User.getInstance().getPersonId(), "");
                        DaoUtils.getSecurityItemInstance().insertData(en);
                    }
                }
                getServiceType(BC.STATION_ONDUTY, new OnSuccessAndFaultSub(this, "getOndutyList", context, false));
                break;
            case "getOndutyList"://站长值班记录 当班期间数据
                MyData ondutyList = (MyData) result.getObj();
                if (ondutyList != null && ondutyList.size() > 0) {
                    insertTypes(ondutyList, 6);
                }
                getServiceType(BC.CAR_TYPE, new OnSuccessAndFaultSub(this, "getCarType", context, false));
                break;
            case "getCarType"://车型信息
                MyData carTypes = (MyData) result.getObj();
                if (carTypes != null && carTypes.size() > 0) {
                    insertTypes(carTypes, 5);
                }
                if (isFirst) {
                    LemonBubble.hide();
                    SPUtil.saveSetting("isFirst", false);
                    goMain();
                } else {
                    BC.HAS_DONE = true;
                    LemonBubble.showRight(context, "数据已刷新！", 2000);
                }
        }
    }

    public void goMain() {
        Intent in = new Intent(context, MainActivity.class);
        context.startActivity(in);
        ((BaseActivity) context).overridePendingTransition(com.zyf.common.R.anim.forward_enter, com.zyf.common.R.anim.forward_exit);
        ((BaseActivity) context).finish();
    }

    @Override
    public void onFault(Result result, String method) {
        UI.showToast(context, result.msg);
    }

    /**
     * 插入站人员信息
     *
     * @param data
     */
    private void insertData(MyData data) {
        for (MyRow rr : data) {
            StationEmployee en = new StationEmployee(Long.parseLong(rr.getString("id")),
                    rr.getString("deptId"), rr.getString("deptName"),
                    rr.getString("postId"), rr.getString("postDesc"),
                    rr.getString("postCode"), rr.getString("name"),
                    rr.getString("teamId"), rr.getString("teamName"));
            DaoUtils.getStationEmployeeInstance().insertData(en);
        }
    }

    /**
     * 插入特情类型，车型类型，便民服务类型，车道信息
     *
     * @param data
     */
    private void insertTypes(MyData data, int type) {

        switch (type) {
            case 0://车道信息
                DaoUtils.getLaneTypesInstance().deleteAll();
                for (MyRow rr : data) {
                    LaneTypes en = new LaneTypes(Long.parseLong(rr.getString("id")),
                            rr.getString("code"), rr.getString("groupId"),
                            rr.getString("name"));
                    DaoUtils.getLaneTypesInstance().insertData(en);
                }
                break;
            case 1://特情类型
                DaoUtils.getSpecialTypesInstance().deleteAll();
                for (MyRow rr : data) {
                    SpecialTypes en = new SpecialTypes(Long.parseLong(rr.getString("id")),
                            rr.getString("code"), rr.getString("groupId"),
                            rr.getString("name"));
                    DaoUtils.getSpecialTypesInstance().insertData(en);
                }
                break;
            case 2://服务类型
            case 3://投诉来源
            case 4://投诉类别
            case 5://车型
            case 6://站长值班 当班期间
                for (MyRow rr : data) {
                    CivilizedTypes en = new CivilizedTypes(Long.parseLong(rr.getString("id")),
                            rr.getString("code"), rr.getString("groupId"),
                            rr.getString("name"));
                    DaoUtils.getCivilizedTypesInstance().insertData(en);
                }
                break;
        }
    }
}
