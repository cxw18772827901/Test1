package com.my.mymh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取到的微信的token的数据模型
 * Created by Dave on 2017/2/20.
 */
public class WxTokenVerifyBean {

    @Expose
    @SerializedName(value = "errcode")
    public int errcode;

    @Expose
    @SerializedName(value = "errmsg")
    public String errmsg;

}
