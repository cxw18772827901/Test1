package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;

/**
 * 热门或新作作品期列表
 */
public class CollectUpdateRecord {
    public static final String URL_END = "favorite/read";

    public static class Input extends BaseInput<CollectUpdateRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CollectUpdateRecord.class);
        }

        @InputKey(name = "favoriteId")
        private String favoriteId;


        public static BaseInput<CollectUpdateRecord> buildInput(String favoriteId) {
            Input input = new Input();
            input.favoriteId = favoriteId;
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
