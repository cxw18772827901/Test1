package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.my.mymh.R;
import com.my.mymh.base.BaseApplication;
import com.my.mymh.base.Constants;
import com.my.mymh.model.QQUserBean;
import com.my.mymh.model.WxInfoBean;
import com.my.mymh.model.record.LoginRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.SPUtil;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.WxUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import static com.tencent.connect.common.Constants.REQUEST_APPBAR;
import static com.tencent.connect.common.Constants.REQUEST_LOGIN;

/**
 * PackageName  com.test.novel.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/15.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "LoginActivity";
    @ViewInject(id = R.id.fl_close, needClick = true)
    private View fl_close;
    @ViewInject(id = R.id.ll_qq, needClick = true)
    private View ll_qq;
    @ViewInject(id = R.id.ll_wx, needClick = true)
    private View ll_wx;
    private Tencent mTencent;
    private QQListener qqListener;
    private static final int QQ_LOGIN_TOKEN = 0;
    private static final int QQ_LOGIN_GET_INFO_OK = 3;
    public static final int WX_LOGIN_TOKEN = 1;
    public static final int WX_LOGIN_OPEN_ID = 2;
    public static final int WX_LOGIN_GET_INFO_OK = 4;
    public static final int WX_UNIONID = 5;
    private String qqOpenId;
    private String qqAccessToken;
    private String wxOpenId;
    private String wxAccessToken;
    private String unionid;
    private QQUserBean qqUserBean;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QQ_LOGIN_TOKEN:
                    break;
                case WX_LOGIN_TOKEN:
                    wxAccessToken = (String) msg.obj;
                    break;
                case WX_LOGIN_OPEN_ID:
                    wxOpenId = (String) msg.obj;
                    break;
                case WX_UNIONID:
                    unionid = (String) msg.obj;
                    break;
                case QQ_LOGIN_GET_INFO_OK:
                    qqUserBean = (QQUserBean) msg.obj;
                    login(0, null);
                    break;
                case WX_LOGIN_GET_INFO_OK:
                    login(1, (WxInfoBean) msg.obj);
                    break;
            }
        }
    };
    private String qq_pf;

    private void login(int tag, WxInfoBean wxInfoBean) {
//      MyDebugUtil.log("ttttttt", "start");
        String userAvatar;
        String accessToken;
        String tposType = String.valueOf(tag);
        String pf;
        if (0 == tag) {
            userAvatar = qqOpenId;
            accessToken = qqAccessToken;
            unionid = "";
            pf = qq_pf;
        } else {
            userAvatar = wxOpenId;
            accessToken = wxAccessToken;
            pf = "";
        }
        NetClient.request(new ObjectRequest<>(false, LoginRecord.Input.buildInput(userAvatar, accessToken, unionid, tposType, pf),
                loginRecord -> {
                    if (null != loginRecord && 1000 == loginRecord.code && loginRecord.result != null) {
                        MyDebugUtil.toast("登录成功");
                        SPUtil.put(Constants.LOGIN_TAG, System.currentTimeMillis());
                        if (0 == tag) {
                            qqLoginOk();
                        } else {
                            wxLoginOk(wxInfoBean);
                        }
                        if (!TextUtils.isEmpty(loginRecord.result.userId)) {
                            SPUtil.put(Constants.USER_ID, loginRecord.result.userId);
                        }
                        if (!TextUtils.isEmpty(loginRecord.result.sessionId)) {
                            SPUtil.put(Constants.SESSION_ID, loginRecord.result.sessionId);
                        }
                        SPUtil.put(Constants.ACCESS_TOKEN, accessToken);
                        //new data
                        if (loginRecord.result.updateFavoriteCartoonCount > 0) {
                            EventBus.getDefault().post(new EventBusEvent.NewDataEvent());
                        }
                        EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_FRESH));
                        //ids
                        LoginVerifyUtil.getCareIds();
                        LoginVerifyUtil.getGoodsIds();
                    } else {
                        MyDebugUtil.toastTest("登录失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("登录失败");
                }));
    }

    private void qqLoginOk() {
        SPUtil.put(Constants.USER_ACCOUNT_TYPE, Constants.USER_ACCOUNT_QQ);
        SPUtil.put(Constants.USER_ACCOUNT, qqOpenId);
        SPUtil.put(Constants.USER_PF, qq_pf);
        SPUtil.put(Constants.USER_ACCOUNT_INFO, GsonUtil.getGson().toJson(qqUserBean));
        EventBus.getDefault().post(new EventBusEvent.LoginAndOutEvent(EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_QQ, Constants.USER_ACCOUNT_QQ, qqOpenId, qqUserBean));
        finish();
    }

    private void wxLoginOk(WxInfoBean wxInfoBean) {
        SPUtil.put(Constants.USER_ACCOUNT_TYPE, Constants.USER_ACCOUNT_WX);
        SPUtil.put(Constants.USER_ACCOUNT, wxInfoBean.openid);
        SPUtil.put(Constants.UNIONID, unionid);
        SPUtil.put(Constants.USER_ACCOUNT_INFO, GsonUtil.getGson().toJson(wxInfoBean));
        EventBus.getDefault().post(new EventBusEvent.LoginAndOutEvent(EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_WX, Constants.USER_ACCOUNT_QQ, qqOpenId, wxInfoBean));
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.login_activity);
        init();
    }

    public void init() {
        TitleBarUtil.initImmersiveBar(this, R.color.cl_white);
        //初始化
        qqListener = new QQListener();
        mTencent = BaseApplication.mTencent;
        if (null != mTencent && mTencent.isSessionValid()) {
            mTencent.logout(this);
        }
        //eventbus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_close:
                finish();
                break;
            case R.id.ll_qq:
                if (null != mTencent) {
                    startLoginQQ();
                }
//                MyDebugUtil.toast("暂不支持QQ登陆");
                break;
            case R.id.ll_wx:
                startLoginWX();
                break;
        }
    }

    private void startLoginQQ() {
        mTencent.login(this, "all", qqListener);
    }

    private void startLoginWX() {
        WxUtil.sendReq();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGetWXInfo(EventBusEvent.LoginAndOutEvent loginAndOutEvent) {
        if (EventBusEvent.LoginAndOutEvent.TYPE_GET_WX_CODE == loginAndOutEvent.loginEventType) {
//            MyDebugUtil.log("ttttttt", "onEventGetWXInfo");
            WxUtil.getWxToken(handler, BaseApplication.WX_APP_ID, BaseApplication.WX_SECRET, loginAndOutEvent.wxCode, "authorization_code");
        } else {
//            MyDebugUtil.log("ttttttt", "onEventGetWXInfo error");
        }
    }

    private class QQListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                MyDebugUtil.toast("QQ登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                MyDebugUtil.toastTest("QQ登录失败");
                return;
            }
            //获取权限
            getQQToken((JSONObject) response);
            //获取用户信息
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {
            MyDebugUtil.toast(uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            MyDebugUtil.toast("取消");
        }
    }

    private void getQQToken(JSONObject jsonObject) {
        try {
            qqAccessToken = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            qq_pf = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_PLATFORM_ID);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            qqOpenId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(qqAccessToken) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(qqOpenId)) {
                mTencent.setAccessToken(qqAccessToken, expires);
                mTencent.setOpenId(qqOpenId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        UserInfo mInfo = new UserInfo(this, mTencent.getQQToken());
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object response) {
                JSONObject json = (JSONObject) response;
                QQUserBean qqUserBean = GsonUtil.getGson().fromJson(json.toString(), QQUserBean.class);
                if (null != qqUserBean) {
                    handler.obtainMessage(QQ_LOGIN_GET_INFO_OK, qqUserBean).sendToTarget();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });//获取用户信息,通过上面的回调进行设置
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN || requestCode == REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
