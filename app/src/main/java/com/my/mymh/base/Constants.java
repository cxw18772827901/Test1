package com.my.mymh.base;

import android.widget.LinearLayout;

import com.my.mymh.util.debugLog.MyDebugUtil;

/**
 * Package  com.dave.project.base
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/10/30.
 */

public interface Constants {
    String URL_BASE_HEADER_TEST = "http://192.168.100.252:8090/dm/client/";
    String URL_BASE_HEADER_RELEASE = "http://dm0115.zishupai.cn/client/";
    //    String URL_BASE_HEADER_RELEASE = "http://upload.youlaiyici.com/client/";
    String URL_BASE_HEADER = MyDebugUtil.isDebug ? URL_BASE_HEADER_TEST : URL_BASE_HEADER_RELEASE;
    String SESSION_ID = "";
    long AUTO_DELAY = 5 * 1000;
    int FRESH_HEADER_HEIGHT = 70;
    String WIFI_SWITCH = "wifi_switch";
    String WIFI_SWITCH_NO_MORE = "wifi_switch_no_more";
    int ORIENTATION_V = LinearLayout.VERTICAL;
    String APK_ABSOLUTE_PATH = "apk_absolute_path";
    String APK_ABSOLUTE_PATH_1 = "apk_absolute_path_1";
    String URL_BLANK_HOLDER = "";
    String SIGN_TUCAO_TIME = "sign_tucao_time";
    String SIGN_TUCAO_CODE = "sign_tucao_code";
    String TAG_IS_FIRST_TIME = "tag_is_first_time";
    String USER_ACCOUNT = "user_account";
    String USER_ID = "user_id";
    String USER_ACCOUNT_INFO = "user_account_info";
    String USER_ACCOUNT_TYPE = "user_account_type";
    String USER_ACCOUNT_QQ = "qq";
    String USER_ACCOUNT_WX = "wx";
    String ACCESS_TOKEN = "access_token";
    String UNIONID = "unionid";
    String USER_PF = "user_pf";
    String USER_CARE_IDS = "user_care_ids";
    String USER_GOOD_IDS = "user_good_ids";
    String LOGIN_TAG = "login_tag";
    String APP_VERSION = "app_version";
    String IS_ADULT = "is_adult";
}
