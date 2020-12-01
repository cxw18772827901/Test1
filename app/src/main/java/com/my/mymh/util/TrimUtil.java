package com.my.mymh.util;

/**
 * PackageName  com.hgd.hgdcomic.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/1.
 */

public class TrimUtil {
    public static String trim(String str) {
        return str.replaceAll("^\\s*|\\s*$", "");
    }
}
