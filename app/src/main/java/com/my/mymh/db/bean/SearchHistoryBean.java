package com.my.mymh.db.bean;

import java.io.Serializable;

/**
 * 搜索记录的bean类
 * Created by Dave on 2016/1/21.
 */
public class SearchHistoryBean implements Serializable {
    public String content;
    public String searchTime;
    public String createTime;
    public String count;

    public SearchHistoryBean() {
    }

    public SearchHistoryBean(String content, String searchTime, String createTime, String count) {
        this.content = content;
        this.searchTime = searchTime;
        this.createTime = createTime;
        this.count = count;
    }
}
