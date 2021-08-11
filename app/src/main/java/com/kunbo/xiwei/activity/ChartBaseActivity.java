package com.kunbo.xiwei.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.zyf.net.response.OnSuccessAndFaultListener;
import com.zyf.xiweiapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 统计图表
 */
public class ChartBaseActivity extends MyBaseActivity implements OnSuccessAndFaultListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.fee_num_ll, R.id.over_check_ll})
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.fee_num_ll:
                bundle.putString("from", "feeNum");
                openIntent(ChartActivity.class, "收费额、车流量示意图", bundle);
                break;
            case R.id.over_check_ll:
                bundle.putString("from", "overHigh");
                openIntent(ChartActivity.class, "治超检测数据示意图", bundle);
                break;
        }
    }
}
