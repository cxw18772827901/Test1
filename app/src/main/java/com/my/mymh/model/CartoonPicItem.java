package com.my.mymh.model;

/**
 * PackageName  com.dave.project.model
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/17.
 */

public class CartoonPicItem {
    public String cartoonId;
    public String articleId;
    public String currentPicUrl;
    public String articleName;
    public int picTotalCount;
    public int picIndex;
    public int catalogIndex;

    public CartoonPicItem(String cartoonId, String articleId, String currentPicUrl, String articleName, int totalCount, int picIndex, int catalogIndex) {
        this.cartoonId = cartoonId;
        this.articleId = articleId;
        this.currentPicUrl = currentPicUrl;
        this.articleName = articleName;
        this.picTotalCount = totalCount;
        this.picIndex = picIndex;
        this.catalogIndex = catalogIndex;
    }
}
