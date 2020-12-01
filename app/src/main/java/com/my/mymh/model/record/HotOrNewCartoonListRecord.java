package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.model.InputKey;
import com.my.mymh.util.debugLog.MyDebugUtil;

import java.util.List;

/**
 * 热门或新作作品期列表
 */
public class HotOrNewCartoonListRecord {
    public static final String URL_END = "listCartoon";

    public static class Input extends BaseInput<HotOrNewCartoonListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, HotOrNewCartoonListRecord.class);
        }

        @InputKey(name = "pageSize")
        private String pageSize;

        @InputKey(name = "lastId")
        private String lastId;

        @InputKey(name = "type")
        private String type;

        @InputKey(name = "cartoonType")
        private String cartoonType = MyDebugUtil.getCartoonType();

        public static BaseInput<HotOrNewCartoonListRecord> buildInput(String pageSize, String lastId, String type) {
            Input input = new Input();
            input.pageSize = pageSize;
            input.lastId = lastId;
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
    @SerializedName(value = "result")
    public List<LastUpdateCartoonListRecord.Result> result;

}
