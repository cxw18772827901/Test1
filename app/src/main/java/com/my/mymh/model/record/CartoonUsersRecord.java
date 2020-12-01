package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

import java.io.Serializable;
import java.util.List;

/**
 * 热门或新作作品期列表
 */
public class CartoonUsersRecord {
    public static final String URL_END = "favorite/listUser";

    public static class Input extends BaseInput<CartoonUsersRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CartoonUsersRecord.class);
        }

        @InputKey(name = "cartoonId")
        private String cartoonId;

        @InputKey(name = "counts")
        private String counts;

        @InputKey(name = "lastId")
        private String lastId;

        public static BaseInput<CartoonUsersRecord> buildInput(String cartoonId, String counts, String lastId) {
            Input input = new Input();
            input.cartoonId = cartoonId;
            input.counts = counts;
            input.lastId = lastId;
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

    public static class Result implements Serializable {
        @Expose
        @SerializedName(value = "favoriteId")
        public String favoriteId;

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

        @Expose
        @SerializedName(value = "readChapterId")
        public String readChapterId;

        @Expose
        @SerializedName(value = "readChapterTitle")
        public String readChapterTitle;
    }

}
