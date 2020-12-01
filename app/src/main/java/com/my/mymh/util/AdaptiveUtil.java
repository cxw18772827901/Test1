package com.my.mymh.util;

/**
 * PackageName  com.dave.project.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/23.
 */

public class AdaptiveUtil {
    private static final int UI_SIZE_HEIGHT = 1080;
    private static final int UI_SIZE_WIDTH = 720;
    private static int currentScreenHeight = ScreenUtil.getScreenHei();
    private static int currentScreenWidth = ScreenUtil.getScreenWid();
    private static float scaleH = currentScreenHeight / UI_SIZE_HEIGHT;
    private static float scaleW = currentScreenWidth / UI_SIZE_WIDTH;

    public static int getAdaptiveSizeHeight(int originalSize) {
        return (int) (originalSize * scaleH);
    }

    public static int getAdaptiveSizeHeightDp(int originalSize) {
        int mySize = (int) (originalSize * scaleH);
        return UIUtil.px2dip(mySize);
    }

    public static int getAdaptiveSizeWidth(int originalSize) {
        return (int) (originalSize * scaleW);
    }

    public static int getAdaptiveSizeWidthDP(int originalSize) {
        int mySize = (int) (originalSize * scaleW);
        return UIUtil.px2dip(mySize);
    }
}
