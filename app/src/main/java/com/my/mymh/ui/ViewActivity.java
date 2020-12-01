package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2017/12/22.
 */

public class ViewActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.view_activity);
    }
}
