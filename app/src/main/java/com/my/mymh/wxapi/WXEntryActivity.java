package com.my.mymh.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.my.mymh.base.BaseApplication;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import static com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode.ERR_OK;

/**
 * 微信实体类
 * Desc:用于和微信通话（接收微信的请求及返回值）
 * 注意包名和类名必须用申请的包名(java下层级目录也必须是指定目录,不仅是清单文件和.gradle文件中的包名)，不然获取不到返回值
 * <p>
 * Created by sky on 2015/12/07
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BaseApplication.mIWXAPI.handleIntent(intent, this);
    }

    /**
     * 微信发送的请求将回调到onReq方法
     */
    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    /**
     * 发送到微信请求的响应结果将回调到onResp方法
     */
    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        MyDebugUtil.logTest("login", "resp.errCode---------=" + resp.errCode);
        if (errCode == ERR_OK) {//0
            if (resp instanceof SendAuth.Resp) {//登录
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                int errorCode = sendResp.errCode;//识别码,判断成功与否
                if (0 == errorCode) {
                    MyDebugUtil.logTest("login", "获取微信code成功");
//                    MyDebugUtil.log("ttttttt","获取微信code成功");
                    EventBus.getDefault().post(new EventBusEvent.LoginAndOutEvent(EventBusEvent.LoginAndOutEvent.TYPE_GET_WX_CODE, sendResp.code));
                } else {
                    if (-4 == errorCode) {
                        MyDebugUtil.toast("已拒绝");
                    } else {
                        MyDebugUtil.toast("已取消");
                    }
                }
            } else if (resp instanceof SendMessageToWX.Resp) {//分享
                MyDebugUtil.toast("分享成功");
                EventBus.getDefault().post(new EventBusEvent.ShareEvent(EventBusEvent.ShareEvent.TYPE_SHARE));
            }
        } else {
            if (errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {//-2
                MyDebugUtil.toast("已取消");
            } else if (errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {//-4
                MyDebugUtil.toast("已拒绝");
            } else {//其他
                MyDebugUtil.toast("请重试");
            }
        }
        finish();
    }
}