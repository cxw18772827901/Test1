package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.base.Constants;
import com.my.mymh.request.volley.BaseInput;

/**
 * 验证token的数据模型
 * Created by Dave on 2017/2/20.
 */
public class VerifyTokenRecord {
    public static String URL_HEAD = "https://api.weixin.qq.com/sns/auth";

    public static class Input extends BaseInput<VerifyTokenRecord> {

        public Input() {
            super(Constants.URL_BLANK_HOLDER, Request.Method.GET, VerifyTokenRecord.class);
        }

        public static BaseInput<VerifyTokenRecord> buildInput(String access_token, String openid) {
            Input input = new Input();
            input.url = URL_HEAD + "?access_token=" + access_token + "&openid=" + openid;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "jsonHeader")
    public String jsonHeader;
}
