package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 评论列表
 */
public class NextChapterRecord {
    public static final String URL_END = "queryPerOrNext";

    public static class Input extends BaseInput<NextChapterRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, NextChapterRecord.class);
        }

        @InputKey(name = "currentArticleIndex")
        private String currentArticleIndex;

        @InputKey(name = "cartoonId")
        private String cartoonId;

        @InputKey(name = "type")
        private String type;

        public static BaseInput<NextChapterRecord> buildInput(String currentArticleIndex, String cartoonId, String type) {
            Input input = new Input();
            input.currentArticleIndex = currentArticleIndex;
            input.cartoonId = cartoonId;
            input.type = type;
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
