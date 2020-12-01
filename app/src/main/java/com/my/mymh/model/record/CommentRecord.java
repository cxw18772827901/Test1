package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;

/**
 * 评论提交
 */
public class CommentRecord {
    //    public static final String URL_END = "submitComment";
    public static final String URL_END = "comment/add";

    public static class Input extends BaseInput<CommentRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CommentRecord.class);
        }

        @InputKey(name = "commentContent")
        private String commentContent;

        @InputKey(name = "cartoonId")
        private String cartoonId;

        public static BaseInput<CommentRecord> buildInput(String commentContent, String cartoonId) {
            Input input = new Input();
            input.commentContent = commentContent;
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

}
