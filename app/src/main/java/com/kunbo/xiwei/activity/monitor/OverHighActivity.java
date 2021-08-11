/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunbo.xiwei.activity.monitor;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zyf.device.BaseActivity;
import com.zyf.device.CamareAndCropFragment;
import com.zyf.xiweiapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 治超管理
 */
public class OverHighActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_high);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.car_in_ll, R.id.car_out_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.car_in_ll:
                openIntent(OverHighInActivity.class, "大件持证超限车辆入口查验登记表");
                break;
            case R.id.car_out_ll:
                openIntent(OverHighOutActivity.class, "大件持证超限车辆出口查验登记表");
                break;
        }
    }
}
