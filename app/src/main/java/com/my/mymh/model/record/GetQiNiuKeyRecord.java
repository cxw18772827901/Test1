package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.request.volley.BaseInput;

/**
 * 七牛获取token数据模型
 * Created by Dave on 2017/3/10.
 */

public class GetQiNiuKeyRecord {
    //    public static final String URL_END = "/cli/storage/getToken.do";
    public static final String URL_END = "qiniu/uploadToken";

    public static class Input extends BaseInput<GetQiNiuKeyRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, GetQiNiuKeyRecord.class);
        }


        public static BaseInput<GetQiNiuKeyRecord> buildInput() {
            return new Input();
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
    public String result;

//    public static class Result {
//
//        @Expose
//        @SerializedName(value = "uploadToken")
//        public String uploadToken;
//    }
}
