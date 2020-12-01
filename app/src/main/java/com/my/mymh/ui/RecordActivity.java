package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.ui.mainFragment.LocalRackFragment;

import java.util.Collections;

/**
 * PackageName  com.hgd.hgdnovel.ui
 * ProjectName  NovelProject
 * Author       chenxiaowu
 * Date         2018/12/26.
 */
public class RecordActivity extends BaseActivity {
    public static final String TAG = "RecordActivity";
    private LocalRackFragment local_fragment;

//    @Override
//    public void setThemeAndLayoutId() {
//        activity_theme = APP_THEME.BLUE_THEME;
//        activity_layoutId = R.layout.record_activity;
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.record_activity);
        initView();
    }

    //    @Override
    public void initView() {
        setTitleName("阅读记录");
        local_fragment = (LocalRackFragment) getSupportFragmentManager().findFragmentById(R.id.local_fragment);
        setRightClickViews(Collections.singletonList("清空"), position -> local_fragment.shouldDeleteAll());
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            if (local_fragment.isLongClick()) {
//                local_fragment.setLongClick(false);
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
