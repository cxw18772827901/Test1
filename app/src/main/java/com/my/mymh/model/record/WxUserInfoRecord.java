package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.base.Constants;
import com.my.mymh.request.volley.BaseInput;

/**
 * 获取微信用户信息的数据模型
 * Created by Dave on 2017/3/7.
 */
public class WxUserInfoRecord {

    public static String URL_HEAD = "https://api.weixin.qq.com/sns/userinfo";

    public static class Input extends BaseInput<WxUserInfoRecord> {

        public Input() {
            super(Constants.URL_BLANK_HOLDER, Request.Method.GET, WxUserInfoRecord.class);
        }

        public static BaseInput<WxUserInfoRecord> buildInput(String access_token, String openid) {
            Input input = new Input();
            input.url = URL_HEAD + "?access_token=" + access_token + "&openid=" + openid;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "jsonHeader")
    public String jsonHeader;
}
