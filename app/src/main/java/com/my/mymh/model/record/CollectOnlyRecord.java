package com.my.mymh.model.record;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.mymh.model.InputKey;
import com.my.mymh.request.volley.BaseInput;

/**
 * 热门或新作作品期列表
 */
public class CollectOnlyRecord {
    public static final String URL_END = "favorite/operate";

    public static class Input extends BaseInput<CollectOnlyRecord> {

        public Input() {
            super(URL_END, Request.Method.POST, CollectOnlyRecord.class);
        }

        @InputKey(name = "cartoonId")
        private String cartoonId;

        @InputKey(name = "operate")
        private String operate;

//        @InputKey(name = "chapterId")
//        private String chapterId;
//
//        @InputKey(name = "chapterTitle")
//        private String chapterTitle;

        public static BaseInput<CollectOnlyRecord> buildInput(String cartoonId, String operate/*,
                                                              String chapterId, String chapterTitle*/) {
            Input input = new Input();
            input.cartoonId = cartoonId;
            input.operate = operate;
//            input.chapterId = chapterId;
//            input.chapterTitle = chapterTi、tle;
            return input;
        }
    }

    @Expose
    @SerializedName(value = "code")
    public int code;

    @Expose
    @SerializedName(value = "info")
    public String info;

}
