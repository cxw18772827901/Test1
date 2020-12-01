package com.my.mymh.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * PackageName  com.dave.project.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/16.
 */

public class MyTextChangedListener implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        changed(s.toString());
    }

    protected void changed(String str) {

    }
}
