package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

import java.io.Serializable;
import java.util.List;

/**
 * 热门或新作作品期列表
 */
public class CareUsersRecord {
    public static final String URL_END = "attention/listUser";

    public static class Input extends BaseInput<CareUsersRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CareUsersRecord.class);
        }

       /* @InputKey(name = "cartoonId")
        private String cartoonId;*/

        @InputKey(name = "counts")
        private String counts;

        @InputKey(name = "lastAttentionId")
        private String lastAttentionId;

        public static BaseInput<CareUsersRecord> buildInput(/*String cartoonId,*/ String counts, String lastAttentionId) {
            Input input = new Input();
//            input.cartoonId = cartoonId;
            input.counts = counts;
            input.lastAttentionId = lastAttentionId;
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
    public List<Result> result;

    public static class Result implements Serializable {

        @Expose
        @SerializedName(value = "attentionId")
        public String attentionId;

        @Expose
        @SerializedName(value = "userId")
        public String userId;

        @Expose
        @SerializedName(value = "sex")
        public int sex;

        @Expose
        @SerializedName(value = "nickName")
        public String nickName;

        @Expose
        @SerializedName(value = "headImg")
        public String headImg;

    }

}
