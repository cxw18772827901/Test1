package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 热门或新作作品期列表
 */
public class CareRecord {
    public static final String URL_END = "attention/operate";

    public static class Input extends BaseInput<CareRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CareRecord.class);
        }

        @InputKey(name = "attentionUserId")
        private String attentionUserId;

        @InputKey(name = "operate")
        private int operate;

        public static BaseInput<CareRecord> buildInput(String attentionUserId, int operate) {
            Input input = new Input();
            input.attentionUserId = attentionUserId;
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
