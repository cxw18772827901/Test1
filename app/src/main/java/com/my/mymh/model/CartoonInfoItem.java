package com.my.mymh.model;

/**
 * PackageName  com.dave.project.model
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/15.
 */

public class CartoonInfoItem {
    public static final int ITEM_TITLE = 0;
    public static final int ITEM_DESC = 1;
    public static final int ITEM_COMMENT = 2;
    public static final int ITEM_COMMENT_NONE = 3;
    public static final int ITEM_COMMENT_MORE = 4;
    public int itemType;
    public Object object;
    public boolean shouldShow;
}
