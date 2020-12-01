package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.RetroActionHost;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/11/19.
 */
public class RetroActionActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RetroActionActivity";
    @ViewInject(id = R.id.retro_action_host)
    private RetroActionHost retro_action_host;
    @ViewInject(id = R.id.fl_container)
    private FrameLayout fl_container;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.retro_action_activity);
        init();
        initView(bundle);
        initData();
    }

    private void init() {

    }

    private void initView(Bundle bundle) {
        retro_action_host.init(this, getSupportFragmentManager(), R.id.fl_container, bundle);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
