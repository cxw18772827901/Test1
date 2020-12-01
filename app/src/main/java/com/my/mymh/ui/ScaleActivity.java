package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/2/7.
 */

public class ScaleActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG="ScaleActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.scale_activity);
    }

    @Override
    public void onClick(View v) {

    }
}
