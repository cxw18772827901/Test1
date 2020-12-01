package com.my.mymh.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * PackageName  com.hgd.hgdcomic.util
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/8/30.
 */
public class DateUtil {
    public static String getTodayTime() {
        Calendar c = Calendar.getInstance();//
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        return mYear + "-" + mMonth + "-" + mDay;
    }

    @SuppressLint("SimpleDateFormat")
    public static long getStringToLong(String time) {
        SimpleDateFormat  sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
