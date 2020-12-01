package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 标签列表
 */
public class ClumDataListRecord {
    public static final String URL_END = "getLabelList";

    public static class Input extends BaseInput<ClumDataListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, ClumDataListRecord.class);
        }


        public static BaseInput<ClumDataListRecord> buildInput() {
            return new Input();
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
    public List<String> result;

}
