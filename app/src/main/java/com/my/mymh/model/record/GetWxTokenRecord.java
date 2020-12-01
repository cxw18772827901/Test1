package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.base.Constants;
import com.my.mymh.request.volley.BaseInput;

/**
 * 获取微信token的数据模型,接口是微信提供借口
 * Created by Dave on 2017/2/20.
 */
public class GetWxTokenRecord {
    public static String URL_HEAD = "https://api.weixin.qq.com/sns/oauth2/access_token";

    public static class Input extends BaseInput<GetWxTokenRecord> {

        public Input() {
            super(Constants.URL_BLANK_HOLDER, Request.Method.GET, GetWxTokenRecord.class);
        }

        public static BaseInput<GetWxTokenRecord> buildInput(String appid, String secret, String code, String grant_type) {
            Input input = new Input();
            input.url = URL_HEAD + "?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=" + grant_type;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "jsonHeader")
    public String jsonHeader;

}
