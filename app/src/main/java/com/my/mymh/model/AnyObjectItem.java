package com.my.mymh.model;


import com.my.mymh.wedjet.AutoRollLayout;

/**
 * 轮播图的数据模型
 * Created by Dave on 2017/2/14.
 */

public class AnyObjectItem implements AutoRollLayout.IShowItem {
    private String imageUrl;
    private String title;
    private String id;
    private String hrefUrl;

    public AnyObjectItem() {
    }

    public AnyObjectItem(String imageUrl, String title, String id, String hrefUrl) {
        super();
        this.imageUrl = imageUrl;
        this.title = title;
        this.id = id;
        this.hrefUrl = hrefUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }
}
