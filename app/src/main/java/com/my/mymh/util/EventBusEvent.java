package com.my.mymh.util;

import com.my.mymh.model.record.LastUpdateCartoonListRecord;

/**
 * Package  com.dave.project.util.eventbus.busevent
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/11/15.
 */

public class EventBusEvent {

    public static class SelectorEvent {
        public String str1;
        public String str2;
        public String str3;

        public SelectorEvent(String str1, String str2, String str3) {
            this.str1 = str1;
            this.str2 = str2;
            this.str3 = str3;
        }
    }

    public static class CartoonInfoStickyEvent {
        public LastUpdateCartoonListRecord.Result result;

        public CartoonInfoStickyEvent(LastUpdateCartoonListRecord.Result result) {
            this.result = result;
        }
    }

    public static class FreshOrDeleteEvent {
        public static int TYPE_DELETE = 0;
        public static int TYPE_FRESH = 1;
        public int type;

        public FreshOrDeleteEvent(int type) {
            this.type = type;
        }
    }

    public static class ShareEvent {
        public static final int TYPE_SHARE = 0;
        public static final int TYPE_SIGN_OK = 1;
        public int type;

        public ShareEvent(int type) {
            this.type = type;
        }
    }

    public static class LoginAndOutEvent {
        public static final int TYPE_LOGIN_QQ = 0;//qq登录成功
        public static final int TYPE_LOGIN_WX = 1;//wx登录成功
        public static final int TYPE_GET_WX_CODE = 2;//获取微信code,即将开始获取token
        public static final int TYPE_OUT = -1;//退出登录
        public static final int TYPE_GET_INFO_ERROR = -2;//微信登录失败
        //事件类型
        public int loginEventType;
        //登录类型
        public String typeWxOrQq;
        //qq信息
        public String account;
        public Object userInfo;//QQUserBean,WxInfoBean
        //微信信息
        public String wxCode;//获取微信code,即将开始获取token

        //退出
        public LoginAndOutEvent(int loginEventType) {
            this.loginEventType = loginEventType;
        }

        //qq登录
        public LoginAndOutEvent(int loginEventType, String typeWxOrQq, String account, Object userInfo) {
            this.loginEventType = loginEventType;
            this.typeWxOrQq = typeWxOrQq;
            this.account = account;
            this.userInfo = userInfo;
        }

        public LoginAndOutEvent(int loginEventType, String wxCode) {
            this.loginEventType = loginEventType;
            this.wxCode = wxCode;
        }
    }

    public static class CollectEvent {
        public static final int TYPE_FRESH=0;
        public static final int TYPE_OUT=1;
        public static final int TYPE_MANAGER=2;
        public int type;

        public CollectEvent(int type) {
            this.type = type;
        }
    }

    public static class NewDataEvent {

    }
}
