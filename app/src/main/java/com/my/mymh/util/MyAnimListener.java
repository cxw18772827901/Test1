package com.my.mymh.util;

import android.view.animation.Animation;

/**
 * PackageName  com.dave.project.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/19.
 */

public class MyAnimListener implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        end();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    protected void end() {

    }
}
