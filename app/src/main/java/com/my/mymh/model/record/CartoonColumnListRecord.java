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
public class CartoonColumnListRecord {
    //    public static final String URL_END = "getCartoonListByClassify";
    public static final String URL_END = "getCartoonListByClassifyWithOutOrderIndex";

    public static class Input extends BaseInput<CartoonColumnListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CartoonColumnListRecord.class);
        }

        @InputKey(name = "lzStatus")
        private String lzStatus;

        @InputKey(name = "address")
        private String address;

        @InputKey(name = "label")
        private String label;

        @InputKey(name = "pageSize")
        private String pageSize;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "cartoonType")
        private String cartoonType = MyDebugUtil.getCartoonType();

        public static BaseInput<CartoonColumnListRecord> buildInput(String lzStatus, String address, String label, String pageSize, String lastId) {
            Input input = new Input();
            input.lzStatus = lzStatus;
            input.address = address;
            input.label = label;
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
