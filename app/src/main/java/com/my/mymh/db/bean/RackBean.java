package com.my.mymh.db.bean;

/**
 * PackageName  com.dave.project.db.bean
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/19.
 */

public class RackBean {
    //    @Expose
    public String cartoonId;
    //    @Expose
    public String cartoonContent;
    //    @Expose
    public String cartoonReadTime;

    public RackBean() {
    }

    public RackBean(String cartoonId, String cartoonContent, String cartoonReadTime) {
        this.cartoonId = cartoonId;
        this.cartoonContent = cartoonContent;
        this.cartoonReadTime = cartoonReadTime;
    }
}
