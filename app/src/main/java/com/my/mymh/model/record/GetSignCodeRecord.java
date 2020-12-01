package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.util.debugLog.MyDebugUtil;

/**
 * 获取签到码
 */
public class GetSignCodeRecord {
    static final String HOST_RELEASE_NAME = "https://novel.999manhua.com/upload/app/sign/sign.action";
    static final String HOST_DEBUG_NAME = "http://192.168.100.252/upload/app/sign/sign.action";

    public static class Input extends BaseInput<GetSignCodeRecord> {

        public Input() {
            super("", Request.Method.POST, GetSignCodeRecord.class);
        }

        public static BaseInput<GetSignCodeRecord> buildInput() {
            Input input = new Input();
            input.url = MyDebugUtil.isDebug ? HOST_DEBUG_NAME : HOST_RELEASE_NAME;
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
    @SerializedName(value = "signState")
    public String signState;

    @Expose
    @SerializedName(value = "signCode")
    public String signCode;

}
