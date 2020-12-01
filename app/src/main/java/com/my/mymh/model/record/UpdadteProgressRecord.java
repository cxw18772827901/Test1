package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 热门或新作作品期列表
 */
public class UpdadteProgressRecord {
    public static final String URL_END = "favorite/updateReadProgress";

    public static class Input extends BaseInput<UpdadteProgressRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, UpdadteProgressRecord.class);
        }

        @InputKey(name = "params")
        private String params;

        public static BaseInput<UpdadteProgressRecord> buildInput(String params) {
            Input input = new Input();
            input.params = params;
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
