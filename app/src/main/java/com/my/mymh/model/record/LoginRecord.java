package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;

/**
 * 登录的数据模型
 * Created by Dave on 2017/2/20.
 */
public class LoginRecord {
    public static String URL_END = "user/login";

    public static class Input extends BaseInput<LoginRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, LoginRecord.class);
        }

        @InputKey(name = "userAvatar")
        private String userAvatar;

        @InputKey(name = "accessToken")
        private String accessToken;

        @InputKey(name = "unionid")
        private String unionid;

        @InputKey(name = "tposType")
        private String tposType;

        @InputKey(name = "pf")
        private String pf;

        public static BaseInput<LoginRecord> buildInput(String userAvatar, String accessToken,
                                                        String unionid, String tposType,
                                                        String pf) {
            Input input = new Input();
            input.userAvatar = userAvatar;
            input.accessToken = accessToken;
            input.unionid = unionid;
            input.tposType = tposType;
            input.pf = pf;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "info")
    public String info;

    @Expose
    @SerializedName(value = "result")
    public Result result;

    public static class Result {
        @Expose
        @SerializedName(value = "headImgurl")
        public String headImgurl;

        @Expose
        @SerializedName(value = "sex")
        public String sex;

        @Expose
        @SerializedName(value = "nickName")
        public String nickName;

        @Expose
        @SerializedName(value = "sessionId")
        public String sessionId;

        @Expose
        @SerializedName(value = "userId")
        public String userId;

        @Expose
        @SerializedName(value = "updateFavoriteCartoonCount")
        public int updateFavoriteCartoonCount;
    }
}
