package com.my.mymh.db.bean;

/**
 * PackageName  com.dave.project.db.bean
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/17.
 */

public class ReadPositionBean {
    public String cartoonId;
    public String articleId;
    public String articleName;
    public String currentIndex;

    public ReadPositionBean() {

    }

    public ReadPositionBean(String cartoonId, String articleId, String articleName, String currentIndex) {
        this.cartoonId = cartoonId;
        this.articleId = articleId;
        this.articleName = articleName;
        this.currentIndex = currentIndex;
    }
}
