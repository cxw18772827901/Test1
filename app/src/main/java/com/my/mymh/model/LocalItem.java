package com.my.mymh.model;

/**
 * PackageName  com.test.project.model
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/2/27.
 */

public class LocalItem {
    public static final int ITEM_BOOK = 0;
    public static final int ITEM_HOLDER = 1;
    public static final int ITEM_ALL = 2;
    public int type;
    public Object object;

    public LocalItem(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}
