package com.my.mymh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;


/**
 * Package  com.dave.project.ui
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/10/30.
 */

public class ImgActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.img_activity);
        findViewById(R.id.iv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(null);
            }
        });
        findViewById(R.id.iv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImgActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    public void send(View view) {
        Log.e("time", "time_send_img=" + System.currentTimeMillis());
//        EventBus.getDefault().post(new EventBusEvent.TestEvent("img"));
    }
}
