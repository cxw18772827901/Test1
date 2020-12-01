package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 评论列表
 */
public class CommentListRecord {
    //    public static final String URL_END = "getCommentlList";
    public static final String URL_END = "comment/list";

    public static class Input extends BaseInput<CommentListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CommentListRecord.class);
        }

        @InputKey(name = "counts")
        private String counts;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "cartoonId")
        private String cartoonId;

        public static BaseInput<CommentListRecord> buildInput(String pageSize, String lastId, String cartoonId) {
            Input input = new Input();
            input.counts = pageSize;
            input.lastId = lastId;
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
    public List<Result> list;

    public static class Result {
        @Expose
        @SerializedName(value = "commentId")
        public String commentId;

        @Expose
        @SerializedName(value = "commentContent")
        public String commentContent;

        @Expose
        @SerializedName(value = "ip")
        public String ip;

        @Expose
        @SerializedName(value = "commentTime")
        public String commentTime;

        @Expose
        @SerializedName(value = "userId")
        public String userId;

        @Expose
        @SerializedName(value = "sex")
        public int sex;

        @Expose
        @SerializedName(value = "nickName")
        public String nickName;

        @Expose
        @SerializedName(value = "headImg")
        public String headImg;
    }
}
