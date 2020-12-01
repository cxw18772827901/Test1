package com.my.mymh.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;


/**
 * Package  com.dave.project.ui
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/11/15.
 */

public class ThirdActivity extends BaseActivity {
    public static final String TAG = "ThirdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.third_activity);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("time", "time_send_thi=" + System.currentTimeMillis());
//                EventBus.getDefault().post(new EventBusEvent.TestEvent("third"));
            }
        });
    }
}
