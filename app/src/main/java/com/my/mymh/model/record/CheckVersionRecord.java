package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.base.Constants;
import com.my.mymh.request.volley.BaseInput;

/**
 * 检测更新的数据模型
 * Created by Dave on 2017/1/6.
 */

public class CheckVersionRecord {

    public static class Input extends BaseInput<CheckVersionRecord> {

        public Input() {
            super(Constants.URL_BLANK_HOLDER, Request.Method.GET, CheckVersionRecord.class);
        }

        public static BaseInput<CheckVersionRecord> buildInput(String appid, String version) {
            Input input = new Input();
//            input.url = "http://update.laiyue.mobi/appInfo/api/getAppVersion.do?appid=" + appid + "&version=" + version;
//            input.url = "http://update.laiyue.mobi/appInfo/api/getAppVersion.do?appid=" + appid + "&version=" + version;
            input.url = "http://update.zishupai.cn/appInfo/api/getAppVersion.do?appid=" + appid + "&version=" + version;
            return input;
        }
    }


    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "appInfo")
    public AppInfo appInfo;

    public static class AppInfo {

        @Expose
        @SerializedName(value = "apkUrl")
        public String apkUrl;

        @Expose
        @SerializedName(value = "appid")
        public String appid;

        @Expose
        @SerializedName(value = "h5Url")
        public String h5Url;

        @Expose
        @SerializedName(value = "version")
        public String version;

        @Expose
        @SerializedName(value = "needUpdate")
        public int needUpdate;
    }
}
