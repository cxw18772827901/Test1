package com.my.mymh.model.record;

import com.android.volley.Request;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.util.debugLog.MyDebugUtil;

import java.util.List;

/**
 * 标签下数据列表
 */
public class SearchRecord {
    public static final String URL_END = "searchCartoon";

    public static class Input extends BaseInput<SearchRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, SearchRecord.class);
        }

        @InputKey(name = "searchParam")
        private String searchParam;

        @InputKey(name = "pageSize")
        private String pageSize;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "cartoonType")
        private String cartoonType=  MyDebugUtil.getCartoonType();

        public static BaseInput<SearchRecord> buildInput(String searchParam, String pageSize, String lastId) {
            Input input = new Input();
            input.searchParam = searchParam;
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
    public List<LastUpdateCartoonListRecord.Result> result;

}
