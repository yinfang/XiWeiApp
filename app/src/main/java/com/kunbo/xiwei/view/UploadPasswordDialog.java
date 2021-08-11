package com.kunbo.xiwei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.modle.User;
import com.kunbo.xiwei.service.SubmitDataService;
import com.zyf.device.RecyclerCommonAdapter;
import com.zyf.domain.C;
import com.zyf.model.MyData;
import com.zyf.model.MyRow;
import com.zyf.model.Result;
import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.net.response.OnSuccessAndFaultSub;
import com.zyf.util.UI;
import com.zyf.view.lemonbubble.LemonBubble;
import com.zyf.view.recyclerview.ViewHolder;
import com.zyf.xiweiapp.R;

import java.util.ArrayList;

import static com.zyf.net.api.ApiSubscribe.appLogin;
import static com.zyf.net.api.ApiSubscribe.getUserInfo;


/**
 * 上传数据密码输入弹出框
 */
public class UploadPasswordDialog implements OnSuccessAndFaultListener, View.OnClickListener {
    private Context context;
    private RecyclerView recyclerKeyboard;
    private TextView tvZero;
    private RelativeLayout rlUndo;
    private CheckBox ivShowPwd;
    private TextView tvPwd;
    private TextView tvSubmit;
    private ArrayList<String> keyboardList;//数字列表
    private String psw = "";//密码
    private AlertDialog d;

    public UploadPasswordDialog(@NonNull Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void show() {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.dialog_upload_password, null);

        ivShowPwd = v.findViewById(R.id.iv_showpwd);//显示密码
        tvZero = v.findViewById(R.id.tv_zero);//数字0
        rlUndo = v.findViewById(R.id.rl_undo);//后退键
        tvPwd = v.findViewById(R.id.tv_password);//密码框
        tvSubmit = v.findViewById(R.id.submit);//确认

        recyclerKeyboard = v.findViewById(R.id.list_keyboard);
        keyboardList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            keyboardList.add(i + "");
        }
        RcyCommonAdapter adapter = new RcyCommonAdapter(context, keyboardList);
        recyclerKeyboard.setAdapter(adapter);

        tvZero.setOnClickListener(this);
        rlUndo.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        ivShowPwd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //如果选中，显示密码
                tvPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //否则隐藏密码
                tvPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        ad.setView(v);
        d = ad.create();
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    private class RcyCommonAdapter extends RecyclerCommonAdapter {

        public RcyCommonAdapter(Context context, ArrayList<String> datas) {
            super(context, R.layout.item_upload_password, datas);
        }

        @Override
        protected void convert(ViewHolder holder, Object o, int position) {
            TextView tvNum = holder.getView(R.id.tv_num);
            tvNum.setText(String.valueOf(position + 1));
            holder.getItemView().setOnClickListener(v -> {
                String num = String.valueOf(position + 1);
                tvPwd.append(num);
            });
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit://确认
                C.TOKEN = "";
                C.TOKEN_TYPE = "";
                appLogin(User.getInstance().getAccount(), tvPwd.getText().toString(), new OnSuccessAndFaultSub(this, "appLogin", "重新登录获取token中....", false, context));
                break;
            case R.id.tv_zero://输入0
                tvPwd.append("0");
                break;
            case R.id.rl_undo://删除输入的密码
                String text = tvPwd.getText().toString();
                if (text.length() > 0)
                    tvPwd.setText(text.substring(0, text.length() - 1));
                break;
        }
    }

    @Override
    public void onSuccess(Result result, String method) {
        MyRow ro;
        if (method.equals("appLogin")) {
            ro = (MyRow) result.obj;
            C.TOKEN_TYPE = ro.getString("token_type") + " ";
            C.TOKEN = ro.getString("access_token");
            getUserInfo(new OnSuccessAndFaultSub(this, "getUserInfo", "更新个人信息中....", false, context));
        } else if (method.equals("getUserInfo")) {
            ro = (MyRow) result.obj;
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
            BC.CAN_UPLAOD = true;
            Intent service = new Intent(context, SubmitDataService.class);
            context.startService(service);
            LemonBubble.hide();
            d.dismiss();
        }
    }

    @Override
    public void onFault(Result result, String method) {
        UI.showToast(context, result.msg);
    }
}
