package com.my.mymh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取到的微信的token的数据模型
 * Created by Dave on 2017/2/20.
 */
public class WxTokenBean {

    @Expose
    @SerializedName(value = "access_token")
    public String access_token;

    @Expose
    @SerializedName(value = "expires_in")
    public String expires_in;

    @Expose
    @SerializedName(value = "refresh_token")
    public String refresh_token;

    @Expose
    @SerializedName(value = "openid")
    public String openid;

    @Expose
    @SerializedName(value = "scope")
    public String scope;

    @Expose
    @SerializedName(value = "unionid")
    public String unionid;

    @Expose
    @SerializedName(value = "errcode")
    public String errcode;

    @Expose
    @SerializedName(value = "errmsg")
    public String errmsg;

}
