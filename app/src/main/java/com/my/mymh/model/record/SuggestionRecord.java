package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 标签下数据列表
 */
public class SuggestionRecord {
    public static final String URL_END = "userAdvice/add";

    public static class Input extends BaseInput<SuggestionRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, SuggestionRecord.class);
        }

        @InputKey(name = "content")
        private String content;

        public static BaseInput<SuggestionRecord> buildInput(String content) {
            Input input = new Input();
            input.content = content;
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
