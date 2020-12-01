package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 标签下数据列表
 */
public class ReportRecord {
    public static final String URL_END = "feedback/add";

    public static class Input extends BaseInput<ReportRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, ReportRecord.class);
        }

        @InputKey(name = "cartoonId")
        private String cartoonId;

        @InputKey(name = "problemType")
        private String problemType;

        @InputKey(name = "remark")
        private String remark;

        @InputKey(name = "photos")
        private String photos;

        public static BaseInput<ReportRecord> buildInput(String cartoonId, String problemType,
                                                         String remark, String photos) {
            Input input = new Input();
            input.cartoonId = cartoonId;
            input.problemType = problemType;
            input.remark = remark;
            input.photos = photos;
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
