package com.my.mymh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 获取到的qq用户信息的数据模型
 * Created by Dave on 2017/2/17.
 */
public class QQUserBean {
    @Expose
    @SerializedName(value = "ret")
    public String ret;

    @Expose
    @SerializedName(value = "msg")
    public String msg;

    @Expose
    @SerializedName(value = "is_lost")
    public String is_lost;

    @Expose
    @SerializedName(value = "nickname")
    public String nickname;

    @Expose
    @SerializedName(value = "gender")
    public String gender;

    @Expose
    @SerializedName(value = "province")
    public String province;

    @Expose
    @SerializedName(value = "city")
    public String city;

    @Expose
    @SerializedName(value = "figureurl")
    public String figureurl;

    @Expose
    @SerializedName(value = "figureurl_1")
    public String figureurl_1;

    @Expose
    @SerializedName(value = "figureurl_2")
    public String figureurl_2;

    @Expose
    @SerializedName(value = "figureurl_qq_1")
    public String figureurl_qq_1;

    @Expose
    @SerializedName(value = "figureurl_qq_2")
    public String figureurl_qq_2;

    @Expose
    @SerializedName(value = "is_yellow_vip")
    public String is_yellow_vip;

    @Expose
    @SerializedName(value = "vip")
    public String vip;

    @Expose
    @SerializedName(value = "yellow_vip_level")
    public String yellow_vip_level;

    @Expose
    @SerializedName(value = "level")
    public String level;

    @Expose
    @SerializedName(value = "is_yellow_year_vip")
    public String is_yellow_year_vip;
}
