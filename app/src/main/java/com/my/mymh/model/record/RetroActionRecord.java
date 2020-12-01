package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 标签下数据列表
 */
public class RetroActionRecord {
    public static final String URL_END = "produceComplete/add";

    public static class Input extends BaseInput<RetroActionRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, RetroActionRecord.class);
        }

        @InputKey(name = "bookName")
        private String bookName;

        @InputKey(name = "author")
        private String author;

        @InputKey(name = "remark")
        private String remark;

        public static BaseInput<RetroActionRecord> buildInput(String bookName, String author, String remark) {
            Input input = new Input();
            input.bookName = bookName;
            input.author = author;
            input.remark = remark;
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
