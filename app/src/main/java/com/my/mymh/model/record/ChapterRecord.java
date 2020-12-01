package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 评论列表
 */
public class ChapterRecord {
    public static final String URL_END = "startRead";

    public static class Input extends BaseInput<ChapterRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, ChapterRecord.class);
        }

        @InputKey(name = "articleId")
        private String articleId;

        @InputKey(name = "cartoonId")
        private String cartoonId;

        public static BaseInput<ChapterRecord> buildInput(String articleId, String cartoonId) {
            Input input = new Input();
            input.articleId = articleId;
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
    @SerializedName(value = "imgUrl")
    public String imgUrl;

    @Expose
    @SerializedName(value = "imgIndex")
    public int imgIndex;

    @Expose
    @SerializedName(value = "articleName")
    public String articleName;

    @Expose
    @SerializedName(value = "articleId")
    public String articleId;

}
