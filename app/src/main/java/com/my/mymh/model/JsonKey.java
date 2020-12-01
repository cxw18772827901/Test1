package com.my.mymh.model;

import com.google.gson.annotations.Expose;

/**
 * 自定义类型,手动添加header
 * Created by Dave on 2017/2/9.
 */

public class JsonKey {

    public JsonKey(String jsonHeader) {
        this.jsonHeader = jsonHeader;
    }

    @Expose
    public String jsonHeader;
}
