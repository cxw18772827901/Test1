package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

import java.io.Serializable;

/**
 * 热门或新作作品期列表
 */
public class UserInfoRecord {
    public static final String URL_END = "user/detail";

    public static class Input extends BaseInput<UserInfoRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, UserInfoRecord.class);
        }

        @InputKey(name = "userId")
        private String userId;

        public static BaseInput<UserInfoRecord> buildInput(String userId) {
            Input input = new Input();
            input.userId = userId;
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

    public static class Result implements Serializable {
        @Expose
        @SerializedName(value = "goodCounts")
        public int goodCounts;

        @Expose
        @SerializedName(value = "attentionCounts")
        public int attentionCounts;
    }

}
