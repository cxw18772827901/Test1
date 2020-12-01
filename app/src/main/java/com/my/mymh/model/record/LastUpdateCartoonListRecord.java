package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.util.debugLog.MyDebugUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 最近更新列表
 */
public class LastUpdateCartoonListRecord {
    public static final String URL_END = "getLastestCartoonList";

    public static class Input extends BaseInput<LastUpdateCartoonListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, LastUpdateCartoonListRecord.class);
        }

        @InputKey(name = "pageSize")
        private String pageSize;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "cartoonType")
        private String cartoonType =  MyDebugUtil.getCartoonType();

        public static BaseInput<LastUpdateCartoonListRecord> buildInput(String pageSize, String lastId) {
            Input input = new Input();
            input.pageSize = pageSize;
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
        @SerializedName(value = "imgUrl")
        public String imgUrl;

        @Expose
        @SerializedName(value = "author")
        public String author;

        @Expose
        @SerializedName(value = "longDesc")
        public String longDesc;

        @Expose
        @SerializedName(value = "id")
        public String id;

        @Expose
        @SerializedName(value = "updateTime")
        public String updateTime;

        @Expose
        @SerializedName(value = "name")
        public String name;

        @Expose
        @SerializedName(value = "articleName")
        public String articleName;

        @Expose
        @SerializedName(value = "lzStatus")
        public String lzStatus;

        @Expose
        @SerializedName(value = "label")
        public String label;

        @Override
        public String toString() {
            return "id=" + id;
        }
    }
}
