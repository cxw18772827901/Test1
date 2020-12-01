package com.my.mymh.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.my.mymh.base.BaseApplication;

/**
 * PackageName  com.hgd.hgdnovel.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/19.
 */

public class StatusBarHeightUtil {
    private static int statusBarHeight = 0;

    public static int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0;
        }
        if (0 != statusBarHeight) {
            return statusBarHeight;
        }
        //获取status_bar_height资源的ID
        Context context = BaseApplication.getContext();
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            try {
                @SuppressLint("PrivateApi")
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
                statusBarHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public static void setToolBarTopMargin(Toolbar toolBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 == statusBarHeight) {
                statusBarHeight = getStatusBarHeight();
            }
            if (statusBarHeight > 0) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolBar.getLayoutParams();
                layoutParams.topMargin = statusBarHeight;
                toolBar.setLayoutParams(layoutParams);
            }
        }
    }

    //适配全面屏
    public static boolean isFullScreen() {
        return ScreenUtil.getScreenHei() / ScreenUtil.getScreenWid() > (float) 16 / 9;
    }
}
