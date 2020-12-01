package com.my.mymh.util;

import android.app.Activity;
import android.content.Intent;

import com.my.mymh.R;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.ui.CartoonInfoAndCatalogActivity;


/**
 * 跳转意图工具类
 * Created by Dave on 2016/12/30.
 */

public class IntentUtil {

    public static void startActivity(Activity activity, Class aClass) {
        activity.startActivity(new Intent(activity, aClass));
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void finishActivityWithAnim(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.trans_pre_in_back, R.anim.trans_pre_out_back);
    }

    public static void startAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void finishAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.trans_pre_in_back, R.anim.trans_pre_out_back);
    }

    public static void startActivityWithString(Activity activity, Class aClass, String key, String value) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityForsultWithString(Activity activity, Class aClass, String key, String value) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key, value);
        activity.startActivityForResult(intent, 1000);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityForsultWithTwoString(Activity activity, Class aClass, String key1, String value1, String key2, String value2) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivityForResult(intent, 1000);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithTwoString(Activity activity, Class aClass, String key1, String value1, String key2, String value2) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithThreeString(Activity activity, Class aClass, String key1, String value1, String key2, String value2, String key3, String value3) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithFourString(Activity activity, Class aClass, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        intent.putExtra(key4, value4);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithFiveString(Activity activity, Class aClass, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        intent.putExtra(key4, value4);
        intent.putExtra(key5, value5);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithSixString(Activity activity, Class aClass, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5, String key6, String value6) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        intent.putExtra(key4, value4);
        intent.putExtra(key5, value5);
        intent.putExtra(key6, value6);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startActivityWithSevString(Activity activity, Class aClass, String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5, String key6, String value6, String key7, String value7) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(key1, value1);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        intent.putExtra(key4, value4);
        intent.putExtra(key5, value5);
        intent.putExtra(key6, value6);
        intent.putExtra(key7, value7);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    public static void startRead(Activity activity, LastUpdateCartoonListRecord.Result result) {
        Intent intent = new Intent(activity, CartoonInfoAndCatalogActivity.class);
        intent.putExtra("result", result);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }
}
