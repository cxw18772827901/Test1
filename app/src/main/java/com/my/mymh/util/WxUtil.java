package com.my.mymh.util;

import android.os.Handler;
import android.text.TextUtils;

import com.my.mymh.base.BaseApplication;
import com.my.mymh.model.WxInfoBean;
import com.my.mymh.model.WxTokenBean;
import com.my.mymh.model.WxTokenVerifyBean;
import com.my.mymh.model.record.GetWxTokenRecord;
import com.my.mymh.model.record.VerifyTokenRecord;
import com.my.mymh.model.record.WxUserInfoRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.LoginActivity;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;


/**
 * 微信登录相关的工具类
 * Created by Dave on 2017/2/16.
 */
public class WxUtil {
    public static final String TAG = "login";
    private static String access_token;
    private static String openid;
    private static Handler handlers;

    //发起微信登录请求,返回的数据(code)在WXEntryActivity中,设为BaseApplication中进行操作
    public static void sendReq() {
        if (!BaseApplication.mIWXAPI.isWXAppInstalled()) {
            MyDebugUtil.toast("未检测到微信，请先安装微信");
            return;
        }
        // 判断有无网络
        int netType = JudgeNetUtil.getNetType(BaseApplication.getContext());
        if (netType == 1 || netType == 2) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "novel";
            BaseApplication.mIWXAPI.sendReq(req);
        } else {
            MyDebugUtil.toast("请检查网络后再试");
        }
    }

    //通过code获取token以及openid
    public static void getWxToken(Handler handler, String appid, String secret, String code, String grant_type) {
        handlers = handler;
        ObjectRequest<GetWxTokenRecord> reponse = new ObjectRequest<>(true, GetWxTokenRecord.Input.buildInput(appid, secret, code, grant_type),
                getWxTokenRecord -> {
                    if (null != getWxTokenRecord && !TextUtils.isEmpty(getWxTokenRecord.jsonHeader)) {
                        String jsonStr = getWxTokenRecord.jsonHeader;
                        WxTokenBean wxTokenBean = GsonUtil.getGson().fromJson(jsonStr, WxTokenBean.class);
                        if (null != wxTokenBean && !TextUtils.isEmpty(wxTokenBean.access_token)) {
                            access_token = wxTokenBean.access_token;
                            openid = wxTokenBean.openid;
                            verifyToken();
                            handler.obtainMessage(LoginActivity.WX_LOGIN_TOKEN, access_token).sendToTarget();
                            handler.obtainMessage(LoginActivity.WX_LOGIN_OPEN_ID, openid).sendToTarget();
                        } else {
                            sendErrorEvent();
                        }
                    } else {
                        sendErrorEvent();
                    }
                },
                error -> sendErrorEvent());
        NetClient.request(reponse);
    }

    //验证token和openid是否有效
    private static void verifyToken() {
        ObjectRequest<VerifyTokenRecord> reponse = new ObjectRequest<>(true, VerifyTokenRecord.Input.buildInput(access_token, openid),
                verifyTokenRecord -> {
                    if (null != verifyTokenRecord && !TextUtils.isEmpty(verifyTokenRecord.jsonHeader)) {
                        String s = verifyTokenRecord.jsonHeader;
                        WxTokenVerifyBean wxTokenVerifyBean = GsonUtil.getGson().fromJson(s, WxTokenVerifyBean.class);
                        if (null != wxTokenVerifyBean && 0 == wxTokenVerifyBean.errcode) {//正常,获取用户信息
                            getWxUserInfo();
                        } else {
                            sendErrorEvent();
                        }
                    } else {
                        sendErrorEvent();
                    }
                },
                error -> sendErrorEvent());
        NetClient.request(reponse);
    }

    //获取微信用户信息
    private static void getWxUserInfo() {
        ObjectRequest<WxUserInfoRecord> reponse = new ObjectRequest<>(true, WxUserInfoRecord.Input.buildInput(access_token, openid),
                wxUserInfoRecord -> {
                    if (null != wxUserInfoRecord && !TextUtils.isEmpty(wxUserInfoRecord.jsonHeader)) {
                        String s = wxUserInfoRecord.jsonHeader;
                        WxInfoBean getWxInfoBean = GsonUtil.getGson().fromJson(s, WxInfoBean.class);
                        if (null != getWxInfoBean && !TextUtils.isEmpty(getWxInfoBean.nickname)) {//异常
                            handlers.obtainMessage(LoginActivity.WX_UNIONID, getWxInfoBean.unionid).sendToTarget();
                            handlers.obtainMessage(LoginActivity.WX_LOGIN_GET_INFO_OK, getWxInfoBean).sendToTarget();
                        } else {//异常
                            sendErrorEvent();
                        }
                    } else {//异常
                        sendErrorEvent();
                    }
                },
                error -> {
                    sendErrorEvent();
                });
        NetClient.request(reponse);
    }

    private static void sendErrorEvent() {
        EventBus.getDefault().post(new EventBusEvent.LoginAndOutEvent(EventBusEvent.LoginAndOutEvent.TYPE_GET_INFO_ERROR));
    }
}
