package com.kunbo.xiwei.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import com.kunbo.xiwei.activity.agent.InspectionManageActivity;
import com.kunbo.xiwei.activity.agent.StationOndutyActivity;
import com.kunbo.xiwei.activity.monitor.CivilizedServiceActivity;
import com.kunbo.xiwei.activity.monitor.ClassOndutyActivity;
import com.kunbo.xiwei.activity.monitor.ComplaintManageActivity;
import com.kunbo.xiwei.activity.monitor.OverHighActivity;
import com.kunbo.xiwei.activity.monitor.SecurityCheckActivity;
import com.kunbo.xiwei.activity.monitor.SpecialDoneActivity;
import com.kunbo.xiwei.db.entity.DaoMaster;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.service.SubmitDataService;
import com.kunbo.xiwei.view.SyncAndSettingUtils;
import com.kunbo.xiwei.view.UploadPasswordDialog;
import com.zyf.device.BaseActivity;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.statusBarUtil.StatusBarUtil;
import com.zyf.view.pageMenuLayout.IndicatorView;
import com.zyf.view.pageMenuLayout.PageMenuLayout;
import com.zyf.xiweiapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kunbo.xiwei.view.SyncAndSettingUtils.checkHasData;

public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.page_menu)
    PageMenuLayout mPageMenuLayout;
    @BindView(R.id.page_indicator)
    IndicatorView page_Indicator;
    private MyData data = new MyData();

    Class[] acs = {ClassOndutyActivity.class, SpecialDoneActivity.class, CivilizedServiceActivity.class,
            OverHighActivity.class, SecurityCheckActivity.class, ComplaintManageActivity.class, InspectionManageActivity.class};
    String[] titles = {"值班记录", "特情处理", "文明服务", "治超管理", "安全巡检", "投诉管理", "稽查管理"};
    int[] bgs = {R.mipmap.bg_yellow, R.mipmap.bg_pink, R.mipmap.bg_blue, R.mipmap.bg_darkblue, R.mipmap.bg_orange, R.mipmap.bg_purple, R.mipmap.bg_pink};
    int[] imgs = {R.mipmap.cycle_yellow, R.mipmap.cycle_pink, R.mipmap.cycle_blue, R.mipmap.cycle_darkblue, R.mipmap.cycle_orange, R.mipmap.cycle_purple, R.mipmap.cycle_pink1};

    Class[] agentAcs = {StationOndutyActivity.class, InspectionManageActivity.class};
    String[] agentTitles = {"值班记录", "稽查管理"};
    int[] agentBgs = {R.mipmap.bg_yellow, R.mipmap.bg_pink};

    int[] agentImgs = {R.mipmap.cycle_yellow, R.mipmap.cycle_pink1};

    private long clickTime = 0; // 第一次点击返回键的时间
    private boolean isMonitor = true;
    private UploadDataBroadCastReceiver mUploadBroadCastReceiver;
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarImageIn(this);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(User.getInstance().getRole()))
            isMonitor = User.getInstance().getRole().equals("monitor");

        //注册一个广播
        mUploadBroadCastReceiver = new UploadDataBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.kunbo.xiwei.uploatdata");
        registerReceiver(mUploadBroadCastReceiver, intentFilter);

        //启动后台 数据查询 服务
        //Intent service = new Intent(this, SubmitDataService.class);
        //startService(service);
        initView();
    }

    @Override
    protected void onResume() {//检测是否有未上传数据
        super.onResume();
        int count = checkHasData();
        if (count > 0) {
            ivUpload.setImageResource(R.mipmap.upload_waitting);
            tvCount.setText(count + "");
            tvCount.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
        } else {
            tvCount.setText("0");
            ivUpload.setImageResource(R.mipmap.upload_default);
            tvCount.setTextColor(ResourcesCompat.getColor(getResources(), R.color.gray_23, null));
        }

        if (!TextUtils.isEmpty(User.getInstance().getPhoto())) {
            setImage(R.id.user_photo, User.getInstance().getPhoto(), R.mipmap.headimg_default, true);
        }
        if (!TextUtils.isEmpty(User.getInstance().getName())) {
            setEText(R.id.user_name, User.getInstance().getName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getAccount())) {
            setEText(R.id.user_no, User.getInstance().getAccount());
        }
        if (!TextUtils.isEmpty(User.getInstance().getStationName())) {
            setEText(R.id.user_station, User.getInstance().getStationName());
        }
        if (!TextUtils.isEmpty(User.getInstance().getTeamName())) {
            setEText(R.id.user_role, User.getInstance().getTeamName() + User.getInstance().getJob());
        }
    }

    private void initView() {
        mPageMenuLayout.setItemLayout(R.layout.item_page_menu);
        if (!isMonitor) {//站长角色
            for (int i = 0; i < agentImgs.length; i++) {
                MyRow row = new MyRow();
                row.put("bg", agentBgs[i]);
                row.put("name", agentTitles[i]);
                row.put("path", agentImgs[i]);
                data.add(row);
            }
        } else {
            for (int i = 0; i < imgs.length; i++) {
                MyRow row = new MyRow();
                row.put("bg", bgs[i]);
                row.put("name", titles[i]);
                row.put("path", imgs[i]);
                data.add(row);
            }
        }
        mPageMenuLayout.setPageDatas(data);
        page_Indicator.setIndicatorCount(mPageMenuLayout.getPageCount());
        mPageMenuLayout.setOnPageListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                page_Indicator.setCurrentIndicator(position);
            }
        });
    }

    @Override
    public void onPageMenuItemClick(View view, Object o, int position) {
        gotoActivity(position);
    }

    private void gotoActivity(int position) {
        if (!isMonitor)  //站长角色
            openIntent(agentAcs[position], agentTitles[position]);
        else
            openIntent(acs[position], titles[position]);
    }

    @OnClick({R.id.user_photo, R.id.upload_ll, R.id.exchange_ll, R.id.sycn_ll})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_photo:
                UI.showToast(this, "当前软件版本为：" + AppUtil.getVersionName(this) + "\n数据库版本为：" + DaoMaster.SCHEMA_VERSION);
                break;
            case R.id.upload_ll://上传数据
                if (checkHasData() > 0) {
                    UploadPasswordDialog pwdDialog = new UploadPasswordDialog(this);
                    pwdDialog.show();
                } else {
                    UI.showToast(this, "没有未上传的数据！");
                }
                break;
            case R.id.exchange_ll:
                showExitTip();
                break;
            case R.id.sycn_ll:
                SyncAndSettingUtils dialog = new SyncAndSettingUtils(this);
                dialog.sycnData(this, false);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitTip();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showExitTip() {
        if (checkHasData() > 0) {
            UI.showOKCancelDialog(this, "温馨提示", "检测到本地有数据未上传，交班后数据将清除，确认交班？",1005,null);
        } else {
            UI.showOKCancelDialog(this, "温馨提示", "确认退出登录？", 1005, null);
        }
    }

    @Override
    public void processDialogOK(int type, Object tag) {
        super.processDialogOK(type, tag);
        C.TOKEN = "";
        C.TOKEN_TYPE = "";
        SyncAndSettingUtils.clearSaves();
        openIntent(LoginActivity.class, "");
        finish();
     /*   if (type == 1008) {
            appLoginOut(C.TOKEN, new OnSuccessAndFaultSub(this, "backLogin", "正在注销登录...", this));
        } else {
            appLoginOut(C.TOKEN, new OnSuccessAndFaultSub(this, "exitApp", "正在注销登录...", this));
        }*/
    }

    /*
    private void exitApp() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            UI.showToast(this, "亲，再按一次就退出应用了哦 ^__^ ");
            clickTime = System.currentTimeMillis();
        } else {
            exit(false);
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播
        if (mUploadBroadCastReceiver != null) {
            unregisterReceiver(mUploadBroadCastReceiver);
        }
    }

    /**
     * 数据上传广播
     */
    public class UploadDataBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isStart = intent.getBooleanExtra("isStart", true);//是否开始上传
            if (action != null && action.equals("com.kunbo.xiwei.uploatdata")) {
                int count = checkHasData();
                if (count == 0) {//未上传0
                    if (anim != null) {
                        anim.stop();
                    }
                    tvCount.setText("0");
                    ivUpload.setImageResource(R.mipmap.upload_default);
                    tvCount.setTextColor(ResourcesCompat.getColor(getResources(), R.color.gray_23, null));
                    return;
                }
                //未上传>0
                if (isStart) {
                    ivUpload.setImageResource(R.drawable.upload_anim);
                    anim = (AnimationDrawable) ivUpload.getDrawable();
                    anim.start();
                } else {//是否报错停止上传
                    ivUpload.setImageResource(R.mipmap.upload_waitting);
                }
                tvCount.setText(count + "");
                tvCount.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            }
        }
    }
}