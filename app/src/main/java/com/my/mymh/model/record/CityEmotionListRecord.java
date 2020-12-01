package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.my.mymh.util.debugLog.MyDebugUtil;

import java.util.List;

/**
 * 标签下数据列表
 */
public class CityEmotionListRecord {
    //    public static final String URL_END = "getCartoonListByClassify";
    public static final String URL_END = "getCartoonListByClassifyWithOutOrderIndex";

    public static class Input extends BaseInput<CityEmotionListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CityEmotionListRecord.class);
        }

        @InputKey(name = "lzStatus")
        private String lzStatus = "1";

        @InputKey(name = "address")
        private String address = "";

        @InputKey(name = "label")
        private String label = "深夜漫画";

        @InputKey(name = "pageSize")
        private String pageSize;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "cartoonType")
        private String cartoonType = MyDebugUtil.getCartoonType();

        public static BaseInput<CityEmotionListRecord> buildInput(String lastId, String pageSize) {
            Input input = new Input();
            input.lastId = lastId;
            input.pageSize = pageSize;
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
