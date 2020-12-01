package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 目录列表
 */
public class CatalogListRecord {
    public static final String URL_END = "getCartoonCapterList";

    public static class Input extends BaseInput<CatalogListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CatalogListRecord.class);
        }

        @InputKey(name = "cartoonId")
        private String cartoonId;

        public static BaseInput<CatalogListRecord> buildInput(String cartoonId) {
            Input input = new Input();
            input.cartoonId = cartoonId;
            return input;
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
    public List<Result> result;

    public static class Result {
        @Expose
        @SerializedName(value = "imgUrl")
        public String imgUrl;

        @Expose
        @SerializedName(value = "imgIndex")
        public String imgIndex;

        @Expose
        @SerializedName(value = "articleName")
        public String articleName;

        @Expose
        @SerializedName(value = "articleId")
        public String articleId;
    }
}
