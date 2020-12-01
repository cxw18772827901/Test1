package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.RankView;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/9/17.
 */
public class RankActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RankActivity";
    @ViewInject(id = R.id.tv_title_back, needClick = true)
    private TextView tv_title_back;
    @ViewInject(id = R.id.rank_view)
    private RankView rank_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.rank_activity);
        init();
        initView();
        initData();
    }

    private void init() {
        TitleBarUtil.initImmersiveBar(this, R.color.cl_main_red_head);
    }

    private void initView() {
        rank_view.initView(getSupportFragmentManager());
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
        }
    }
}
