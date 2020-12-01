package com.my.mymh.util.debugLog;


import android.util.Log;
import android.widget.Toast;

import com.my.mymh.base.BaseApplication;


/**
 * 调试工具
 * Created by Dave on 2017/1/6.
 */
public class MyDebugUtil {
    public static boolean isDebug = false;
    //public static boolean isLocal = true;
    public static boolean isHideShareOrSign = false;
    public static boolean isFree = true;

    public static String getCartoonType() {
        return "0";//SPUtil.getBoolean(Constants.IS_ADULT)?"2":"0";
    }

    /**
     * 测试的log
     *
     * @param tag
     * @param str
     */
    public static void logTest(String tag, String str) {
        BaseLog.d("main", str);
        if (isDebug) {
            Log.e(tag, str);
        }
    }

    /**
     * 测试的蒙板
     *
     * @param str
     */
    public static void toastTest(String str) {
        BaseLog.d("main", str);
        if (isDebug) {
//            Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 正常显示的log
     *
     * @param tag
     * @param str
     */
    public static void log(String tag, String str) {
        BaseLog.d("main", str);
        Log.e(tag, str);
    }

    /**
     * 正常显示的蒙板
     *
     * @param str
     */
    public static void toast(String str) {
        BaseLog.d("main", str);
        Toast.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
