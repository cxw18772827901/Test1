package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;

public class SwitchRecord {
    public static final String URL_END = "searchCartoon";

    public static class Input extends BaseInput<SwitchRecord> {

        public Input() {
            super("", Request.Method.GET, SwitchRecord.class);
        }


        public static BaseInput<SwitchRecord> buildInput() {
            Input input = new Input();
            input.url = "https://wxstory.zishupai.cn/upload/mp/getConfig.do?appid=400";
            return input;
        }
    }

    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "dsc")
    public String dsc;

    @Expose
    @SerializedName(value = "s")
    public int s;

}
