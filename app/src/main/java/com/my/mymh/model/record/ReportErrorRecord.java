package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;

/**
 * 热门或新作作品期列表
 */
public class ReportErrorRecord {
    public static final String URL_END = "dmProblem/add";

    public static class Input extends BaseInput<ReportErrorRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, ReportErrorRecord.class);
        }

        @InputKey(name = "cartoonId")
        private String cartoonId;

        @InputKey(name = "chapterId")
        private String chapterId;

        public static BaseInput<ReportErrorRecord> buildInput(String cartoonId, String chapterId) {
            Input input = new Input();
            input.cartoonId = cartoonId;
            input.chapterId = chapterId;
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
