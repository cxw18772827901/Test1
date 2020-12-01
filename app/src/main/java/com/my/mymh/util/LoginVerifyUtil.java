package com.my.mymh.util;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.CareIdsRecord;
import com.my.mymh.model.record.GoodIdsRecord;
import com.my.mymh.model.record.LoginRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.util
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/11.
 */
public class LoginVerifyUtil {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(SPUtil.getString(Constants.USER_ACCOUNT));
    }

    public static void clearUserInfo() {
        SPUtil.clear();
        EventBus.getDefault().post(new EventBusEvent.LoginAndOutEvent(EventBusEvent.LoginAndOutEvent.TYPE_OUT));
    }

    public static void login() {
        SPUtil.put(Constants.SESSION_ID, "");
        String account;
        String token;
        String unionid;
        String tposType;
        String pf;
        String type = SPUtil.getString(Constants.USER_ACCOUNT_TYPE);
        if (!TextUtils.isEmpty(type)) {
            account = SPUtil.getString(Constants.USER_ACCOUNT);
            token = SPUtil.getString(Constants.ACCESS_TOKEN);
            if (Constants.USER_ACCOUNT_WX.equals(type)) {
                unionid = SPUtil.getString(Constants.UNIONID);
                tposType = "1";
                pf = "";
            } else if (Constants.USER_ACCOUNT_QQ.equals(type)) {
                unionid = "";
                tposType = "0";
                pf = SPUtil.getString(Constants.USER_PF);
            } else {
                return;
            }
        } else {
            return;
        }
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(token)) {
            return;
        }
        NetClient.request(new ObjectRequest<>(false, LoginRecord.Input.buildInput(account, token, unionid, tposType, pf),
                loginRecord -> {
                    if (null != loginRecord && 1000 == loginRecord.code && loginRecord.result != null) {
                        MyDebugUtil.toastTest("登录成功");
                        if (!TextUtils.isEmpty(loginRecord.result.sessionId)) {
                            SPUtil.put(Constants.SESSION_ID, loginRecord.result.sessionId);
                        }
                        //new data
                        if (loginRecord.result.updateFavoriteCartoonCount > 0) {
                            EventBus.getDefault().post(new EventBusEvent.NewDataEvent());
                        }
                        //ids
                        getCareIds();
                        getGoodsIds();
                    } else {
                        MyDebugUtil.toastTest("登录失败,请重新登陆");
                    }
                },
                volleyError -> MyDebugUtil.toastTest("登录失败,请重新登陆")));
    }

    public static String getMyId() {
        return SPUtil.getString(Constants.USER_ID);
    }

    public static boolean isMe(String userId) {
        String myId = getMyId();
        return isLogin() && !TextUtils.isEmpty(userId) &&
                !TextUtils.isEmpty(myId) && myId.equals(userId);
    }

    public static void getCareIds() {
        ObjectRequest<CareIdsRecord> response = new ObjectRequest<>(false, CareIdsRecord.Input.buildInput(),
                careIdsRecord -> {
                    if (careIdsRecord != null && careIdsRecord.result != null &&
                            1000 == careIdsRecord.code) {
                        SPUtil.put(Constants.USER_CARE_IDS, GsonUtil.getGson().toJson(careIdsRecord.result));
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("访问失败");
                });
        NetClient.request(response);
    }

    public static void getGoodsIds() {
        ObjectRequest<GoodIdsRecord> response = new ObjectRequest<>(false, GoodIdsRecord.Input.buildInput(),
                careIdsRecord -> {
                    if (careIdsRecord != null && careIdsRecord.result != null &&
                            1000 == careIdsRecord.code) {
                        SPUtil.put(Constants.USER_GOOD_IDS, GsonUtil.getGson().toJson(careIdsRecord.result));
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("访问失败");
                });
        NetClient.request(response);
    }

    public static boolean isCared(String userId) {
        String ids = SPUtil.getString(Constants.USER_CARE_IDS);
        if (isLogin() && !TextUtils.isEmpty(ids)) {
            List<String> isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
            }.getType());
            return isList != null && isList.contains(userId);
        } else {
            return false;
        }
    }

    public static void isAddCareId(boolean isAdd, String userId) {
        String ids = SPUtil.getString(Constants.USER_CARE_IDS);
        if (isAdd) {
            List<String> isList;
            if (!TextUtils.isEmpty(ids)) {
                isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
                }.getType());
                if (isList == null) {
                    isList = new ArrayList<>();
                }
                if (!isList.contains(userId)) {
                    isList.add(userId);
                }
            } else {
                isList = Collections.singletonList(userId);
            }
            SPUtil.put(Constants.USER_CARE_IDS, GsonUtil.getGson().toJson(isList));
        } else {
            if (isLogin() && !TextUtils.isEmpty(ids)) {
                List<String> isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
                }.getType());
                if (isList != null && isList.size() > 0) {
                    isList.remove(userId);
                }
                SPUtil.put(Constants.USER_CARE_IDS, GsonUtil.getGson().toJson(isList));
            }
        }
    }

    public static boolean isGood(String userId) {
        String ids = SPUtil.getString(Constants.USER_GOOD_IDS);
        if (isLogin() && !TextUtils.isEmpty(ids)) {
            List<String> isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
            }.getType());
            return isList != null && isList.contains(userId);
        } else {
            return false;
        }
    }

    public static void isAddGoodId(boolean isAdd, String userId) {
        String ids = SPUtil.getString(Constants.USER_GOOD_IDS);
        if (isAdd) {
            List<String> isList;
            if (!TextUtils.isEmpty(ids)) {
                isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
                }.getType());
                if (isList == null) {
                    isList = new ArrayList<>();
                }
                if (!isList.contains(userId)) {
                    isList.add(userId);
                }
            } else {
                isList = Collections.singletonList(userId);
            }
            SPUtil.put(Constants.USER_GOOD_IDS, GsonUtil.getGson().toJson(isList));
        } else {
            if (isLogin() && !TextUtils.isEmpty(ids)) {
                List<String> isList = GsonUtil.getGson().fromJson(ids, new TypeToken<List<String>>() {
                }.getType());
                if (isList != null && isList.size() > 0) {
                    isList.remove(userId);
                }
                SPUtil.put(Constants.USER_GOOD_IDS, GsonUtil.getGson().toJson(isList));
            }
        }
    }
}
