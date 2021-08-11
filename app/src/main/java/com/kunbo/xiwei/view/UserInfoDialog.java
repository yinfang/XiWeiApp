package com.kunbo.xiwei.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kunbo.xiwei.BC;
import com.kunbo.xiwei.modle.User;
import com.zyf.domain.C;
import com.zyf.util.AppUtil;
import com.zyf.util.UI;
import com.zyf.util.Utils;
import com.zyf.util.image.GlideUtil;
import com.zyf.xiweiapp.R;

/**
 * 个人信息弹框
 */
public class UserInfoDialog {
    private Context context;

    public UserInfoDialog(@NonNull Context context) {
        this.context = context;
        if (C.dialogTheme != -1) {
            context.setTheme(C.dialogTheme);
        }
    }

    public void show() {
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.user_info_dialog, null);
        ImageView headImg = v.findViewById(R.id.head_photo);
        TextView name = v.findViewById(R.id.user_name);
        TextView account = v.findViewById(R.id.user_account);
        TextView station = v.findViewById(R.id.station_name);
        TextView team = v.findViewById(R.id.team_name);
        TextView role = v.findViewById(R.id.user_role);
        GlideUtil.getInstance().setImage(context, headImg, User.getInstance().getPhoto(), true);
        headImg.setOnClickListener(v1 -> UI.showToast(context, "当前应用VersionCode"
                + AppUtil.getVersionCode(context) + "\t"
                + "VersionName" + AppUtil.getVersionName(context)
                + "\n服务地址" + BC.BASE_URL_INTERNET));

        name.setText(User.getInstance().getName());
        account.setText(User.getInstance().getAccount());
        station.setText(User.getInstance().getStationName());
        if (!Utils.checkNullString(User.getInstance().getTeamName())) {
            team.setVisibility(View.VISIBLE);
            team.setText(User.getInstance().getTeamName());
        } else {
            team.setVisibility(View.GONE);
        }
        role.setText(User.getInstance().getJob());
        ad.setView(v);
        AlertDialog d = ad.create();
        d.show();
    }
}
