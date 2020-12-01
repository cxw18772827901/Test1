package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.util.debugLog.MyDebugUtil;

import java.util.List;

/**
 * 评论列表
 */
public class CommonListRecord {
    //    public static final String URL_END = "getCommentlList";
//    public static final String URL_END = "comment/list";
    public static final String URL_END = "getCartoonListByClassifyWithOutOrderIndex";

    public static class Input extends BaseInput<CommonListRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CommonListRecord.class);
        }


        @InputKey(name = "label")
        private String label;

        @InputKey(name = "pageSize")
        private String pageSize = "10";

        @InputKey(name = "cartoonType")
        private String cartoonType = MyDebugUtil.getCartoonType();

        public static BaseInput<CommonListRecord> buildInput(String label) {
            Input input = new Input();
            input.label = label;
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
