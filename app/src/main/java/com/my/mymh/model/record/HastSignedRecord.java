package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;
import com.my.mymh.util.debugLog.MyDebugUtil;

/**
 * 获取签到码
 */
public class HastSignedRecord {
    static final String HOST_RELEASE_NAME = "https://novel.999manhua.com/upload/app/sign/sign.action";
    static final String HOST_DEBUG_NAME = "http://192.168.100.252/upload/app/sign/sign.action";

    public static class Input extends BaseInput<HastSignedRecord> {

        public Input() {
            super("", Request.Method.POST, HastSignedRecord.class);
        }

        public static BaseInput<HastSignedRecord> buildInput(String code) {
            Input input = new Input();
            input.url = MyDebugUtil.isDebug ? HOST_DEBUG_NAME + "?sign=" + code : HOST_RELEASE_NAME + "?sign=" + code;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "info")
    public String info;

    //(1:未签到,2：已签到)
    @Expose
    @SerializedName(value = "signState")
    public int signState;

    @Expose
    @SerializedName(value = "signCode")
    public String signCode;

}
