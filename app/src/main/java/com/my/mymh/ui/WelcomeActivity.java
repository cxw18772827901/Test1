package com.my.mymh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.inject.ViewInject;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 */

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = "WelcomeActivity";
    @ViewInject(id = R.id.ll_root)
    private RelativeLayout ll_root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.welcome_activity);
        init();
        initData();
    }

    private void init() {
        TitleBarUtil.initImmersiveBar(this, R.color.cl_no_color);
        needFinishAnim = false;
    }

    private void initData() {
        ll_root.postDelayed(this::finishGG, 1000);
    }

    private void finishGG() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_pre_in, /*R.anim.fade_pre_out*/0);
        finish();
    }

}
