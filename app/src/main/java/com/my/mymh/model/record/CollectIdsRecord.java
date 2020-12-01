package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;

import java.util.List;

/**
 * 热门或新作作品期列表
 */
public class CollectIdsRecord {
    public static final String URL_END = "favorite/listCtIds";

    public static class Input extends BaseInput<CollectIdsRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CollectIdsRecord.class);
        }


        public static BaseInput<CollectIdsRecord> buildInput() {
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
