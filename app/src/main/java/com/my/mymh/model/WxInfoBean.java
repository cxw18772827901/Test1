package com.my.mymh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取到的微信用户信息的数据模型
 * Created by Dave on 2017/2/20.
 */
public class WxInfoBean {

    @Expose
    @SerializedName(value = "openid")
    public String openid;

    @Expose
    @SerializedName(value = "nickname")
    public String nickname;

    @Expose
    @SerializedName(value = "sex")
    public int sex;

    @Expose
    @SerializedName(value = "province")
    public String province;

    @Expose
    @SerializedName(value = "city")
    public String city;

    @Expose
    @SerializedName(value = "country")
    public String country;

    @Expose
    @SerializedName(value = "headimgurl")
    public String headimgurl;

    @Expose
    @SerializedName(value = "privilege")
    public String[] privilege;

    @Expose
    @SerializedName(value = "unionid")
    public String unionid;

}
