package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 热门或新作作品期列表
 */
public class GoodRecord {
    public static final String URL_END = "good/operate";

    public static class Input extends BaseInput<GoodRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, GoodRecord.class);
        }

        @InputKey(name = "likeUserId")
        private String likeUserId;

        @InputKey(name = "operate")
        private int operate;

        public static BaseInput<GoodRecord> buildInput(String likeUserId, int operate) {
            Input input = new Input();
            input.likeUserId = likeUserId;
            input.operate = operate;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "info")
    public String info;

}
