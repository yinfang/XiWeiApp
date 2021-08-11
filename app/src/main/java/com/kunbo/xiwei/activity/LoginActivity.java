package com.kunbo.xiwei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.db.entity.StationEmployee;
import com.kunbo.xiwei.db.entity.TeamUsers;
import com.kunbo.xiwei.db.manager.DaoUtils;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.AppUtil;
import com.zyf.util.SPUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.statusBarUtil.StatusBarUtil;
import com.zyf.xiweiapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zyf.net.api.ApiSubscribe.appLogin;
import static com.zyf.net.api.ApiSubscribe.checkNewVersion;
import static com.zyf.net.api.ApiSubscribe.getDeptPerson;
import static com.zyf.net.api.ApiSubscribe.getUserInfo;


public class LoginActivity extends BaseActivity implements OnSuccessAndFaultListener {
    @BindView(R.id.etAccount)
    EditText etAccount;
    private long clickTime = 0; // 第一次点击返回键的时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusBarImageIn(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String account = SPUtil.getString("account", "");
        etAccount.setText(account);
        etAccount.setSelection(account.length());//将光标移至文字末尾
        etAccount.requestFocus();
        checkNewVersion(new OnSuccessAndFaultSub(this, "checkNewVersion", "正在检测新版本...", this));
    }

    @OnClick(R.id.btn_login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (Utils.checkNullString(getEText(R.id.etAccount)) || Utils.checkNullString(getEText(R.id.etPw))) {
                    UI.showToast(this, "账号或密码不能为空！");
                    return;
                }
                C.TOKEN_TYPE = "";
                C.TOKEN = "";
                appLogin(getEText(R.id.etAccount), getEText(R.id.etPw), new OnSuccessAndFaultSub(this, "appLogin", "登录中...", false, this));
                break;
        }
    }

    @Override
    public void onSuccess(Result result, String method) {
        MyRow ro;
        switch (method) {
            case "checkNewVersion":
                ro = (MyRow) result.obj;
                if (!Utils.checkNullString(ro.getString("versionCode"))) {
                    int newCode = (int) Float.parseFloat(ro.getString("versionCode"));
                    if (AppUtil.shouldUpdate(this, newCode)) {
                        showNewVersionAlert(BC.BASE_URL_IMAGE + ro.getString("accessPath"), ro.getString("remark"));
                    } else {
                        UI.showToast(this, "当前已是最新版本");
                    }
                }
                break;
            case "appLogin":
                SPUtil.saveSetting("account", getEText(R.id.etAccount));
                ro = (MyRow) result.obj;
                C.TOKEN_TYPE = ro.getString("token_type") + " ";
                C.TOKEN = ro.getString("access_token");
                getUserInfo(new OnSuccessAndFaultSub(this, "getUserInfo", "正在加载个人信息...", false, this));
                break;
            case "getUserInfo":
                ro = (MyRow) result.obj;
                if (ro != null) {
                    //个人信息
                    MyRow info = (MyRow) ro.get("person");
                    User.getInstance().setUserId(ro.getString("userId"));
                    User.getInstance().setAccount(ro.getString("userName"));
                    if (info != null) {//个人信息
                        User.getInstance().setPersonId(info.getString("id"));
                        User.getInstance().setName(info.getString("name"));
                        User.getInstance().setPhoto(BC.BASE_URL_IMAGE + info.getString("imgUrl"));
                        User.getInstance().setTeamName(info.getString("teamName"));
                        User.getInstance().setTeamId(info.getString("teamId"));
                        User.getInstance().setStationId(info.getString("deptId"));
                        User.getInstance().setStationName(info.getString("deptName"));
                        if (!TextUtils.isEmpty(info.getString("signatureFileUrl")))
                            User.getInstance().setSignature(BC.BASE_URL_IMAGE + info.getString("signatureFileUrl"));
                    }
                    //角色信息
                    MyData roles = (MyData) ro.get("role");
                    if (roles != null && roles.size() > 0) {//角色信息
                        User.getInstance().setRole(roles.get(0).getString("roleCode"));
                        User.getInstance().setJob(roles.get(0).getString("roleName"));
                    }
                    if (!TextUtils.isEmpty(User.getInstance().getRole())) {//有角色才可登录
                        getDeptPerson(User.getInstance().getStationId(), new OnSuccessAndFaultSub(this, "getDeptPerson", "正在同步站点相关信息...", this));
                    } else {
                        UI.showToast(this, "当前账号未获取登录权限，请联系管理员！");
                    }
                }
                break;
            case "getDeptPerson"://人员信息
                DaoUtils.getStationEmployeeInstance().deleteAll();//先删除所有站人员信息再插入
                ro = (MyRow) result.obj;
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
                if (SPUtil.getBoolean("isFirst", true)) {
                    SyncAndSettingUtils dialog = new SyncAndSettingUtils(this);
                    dialog.sycnData(this, true);
                } else {
                    SPUtil.saveSetting("isFirst", false);
                    goMain();
                }
                break;
        }
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

    public void goMain() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        overridePendingTransition(com.zyf.common.R.anim.forward_enter, com.zyf.common.R.anim.forward_exit);
        finish();
    }

    @Override
    public void onFault(Result result, String method) {
        UI.showToast(this, result.msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //exitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            UI.showToast(this, "再按一次就退出应用了哦 (*￣▽￣*)");
            clickTime = System.currentTimeMillis();
        } else {
            exit(false);
        }
    }
}

