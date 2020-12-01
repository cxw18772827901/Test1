package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.AppUtil;
import com.my.mymh.util.inject.ViewInject;

/**
 * Package  com.dave.project.ui
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/12/1.
 */

public class AboutUsActivity extends BaseActivity /*implements View.OnClickListener */{
    public static final String TAG = "AboutUsActivity";
    @ViewInject(id = R.id.tv_version)
    private TextView tv_version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.about_us_activity);
        setTitleName("关于");
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        tv_version.setText("版本号 : " + AppUtil.getVersionName(this));
    }

//    @Override
//    public void onClick(View v) {
//
//    }
}
