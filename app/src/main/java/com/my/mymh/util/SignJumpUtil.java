package com.my.mymh.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.my.mymh.base.BaseApplication;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.GetSignCodeRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

import java.util.Map;

/**
 * PackageName  com.hgd.hgdnovel.util
 * ProjectName  NovelProject
 * Author       chenxiaowu
 * Date         2018/8/30.
 */
public class SignJumpUtil {
    private static final String CROSSTALK_PACKAGE_NAME = "com.hgd.hgdhole";

//    public static void getSignCode(Activity activity) {
//        if (!MyDebugUtil.isLocal) {
//            ObjectRequest<GetSignCodeRecord> response = new ObjectRequest<>(false, GetSignCodeRecord.Input.buildInput(),
//                    getSignCodeRecord -> {
//                        if (null != getSignCodeRecord && /*1000 == getSignCodeRecord.code &&*/ !TextUtils.isEmpty(getSignCodeRecord.signCode)) {
//                            MyDebugUtil.toastTest("提交成功");
//                            startSignActivity(activity, getSignCodeRecord.signCode);
//                        } else {
//                            MyDebugUtil.toast("网络异常");
//                        }
//                    },
//                    volleyError -> MyDebugUtil.toast("网络异常"));
//            NetClient.request(response);
//        } else {
//            startSignActivity(activity, "");
//        }
//    }

    public static void signTCAPP(Activity activity/*, String code*/) {
        if (isApkNotInstalled(activity)) {
            MyDebugUtil.toast("没有安装~");
            return;
        }
        Intent intent = new Intent();
        //第一种方式
        ComponentName cn = new ComponentName(CROSSTALK_PACKAGE_NAME, "com.hgd.hgdhole.ui.WelcomeActivity");
        intent.setComponent(cn);
        intent.putExtra("type", 1);
//        intent.putExtra("code", code);
        //第二种方式
        //intent.setClassName(CROSSTALK_PACKAGE_NAME, "com.hgd.hgdhole.ui.WelcomeActivity");
        //intent.putExtra("test", "intent1");
        activity.startActivity(intent);
    }

    public static boolean isApkNotInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(CROSSTALK_PACKAGE_NAME, PackageManager.GET_UNINSTALLED_PACKAGES);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void signMiniTC() {
        String signTime = SPUtil.getString(Constants.SIGN_TUCAO_TIME);
        String signCode = SPUtil.getString(Constants.SIGN_TUCAO_CODE);
        if (TextUtils.isEmpty(signTime) || TextUtils.isEmpty(signCode) || !DateUtil.getTodayTime().equals(signTime)) {
            getSignCode();
        } else {
            toMini(signCode);
        }
    }

    private static void getSignCode() {
        ObjectRequest<GetSignCodeRecord> response = new ObjectRequest<>(false, GetSignCodeRecord.Input.buildInput(),
                getSignCodeRecord -> {
                    if (null != getSignCodeRecord && !TextUtils.isEmpty(getSignCodeRecord.signCode)) {
                        MyDebugUtil.toastTest("提交成功");
                        SPUtil.put(Constants.SIGN_TUCAO_TIME, DateUtil.getTodayTime());
                        SPUtil.put(Constants.SIGN_TUCAO_CODE, getSignCodeRecord.signCode);
                        toMini(getSignCodeRecord.signCode);
                    } else {
                        MyDebugUtil.toast("网络异常");
                    }
                },
                volleyError -> MyDebugUtil.toast("网络异常"));
        NetClient.request(response);
    }

    private static void toMini(String code) {
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_a7ce41da036b"; // 填小程序原始id,非小程序的appid
        req.path = "/pages/index/index?signcode=" + code;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        BaseApplication.mIWXAPI.sendReq(req);
    }

    public static void openMiniCartoon(Map<String, String> map) {
        if (map == null) {
            return;
        }
        StringBuilder data = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (TextUtils.isEmpty(data)) {
                    data.append(entry.getKey()).
                            append("=").
                            append(entry.getValue());
                } else {
                    data.append("&").
                            append(entry.getKey()).
                            append("=").
                            append(entry.getValue());
                }
            }
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = "gh_c5c72bfcdabf"; // 填小程序原始id,非小程序的appid
            req.path = "/pages/details/details?" + data.toString();                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
            BaseApplication.mIWXAPI.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
