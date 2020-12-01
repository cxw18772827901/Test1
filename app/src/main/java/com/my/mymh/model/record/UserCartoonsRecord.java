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
public class UserCartoonsRecord {
    public static final String URL_END = "favorite/listDm";

    public static class Input extends BaseInput<UserCartoonsRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, UserCartoonsRecord.class);
        }

        @InputKey(name = "userId")
        private String userId;

        @InputKey(name = "counts")
        private String counts;

        @InputKey(name = "lastId")
        private String lastId;

        public static BaseInput<UserCartoonsRecord> buildInput(String userId, String counts, String lastId) {
            Input input = new Input();
            input.userId = userId;
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
        @SerializedName(value = "favoriteTime")
        public String favoriteTime;

        @Expose
        @SerializedName(value = "updateTime")
        public String updateTime;

        @Expose
        @SerializedName(value = "cartoonId")
        public String cartoonId;

        @Expose
        @SerializedName(value = "name")
        public String name;

        @Expose
        @SerializedName(value = "imgUrl")
        public String imgUrl;

        @Expose
        @SerializedName(value = "articleName")
        public String articleName;

        @Expose
        @SerializedName(value = "author")
        public String author;

        @Expose
        @SerializedName(value = "lzStatus")
        public int lzStatus;

        @Expose
        @SerializedName(value = "longDesc")
        public String longDesc;

        @Expose
        @SerializedName(value = "readId")
        public String readId;

        @Expose
        @SerializedName(value = "title")
        public String title;
    }

}
