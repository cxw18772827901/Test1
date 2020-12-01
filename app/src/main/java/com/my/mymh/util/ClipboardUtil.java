package com.my.mymh.util;

import android.content.ClipboardManager;
import android.content.Context;

import com.my.mymh.base.BaseApplication;

/**
 * 复制粘贴功能
 * Created by chenxiaowu on 2017/5/3.
 */
public class ClipboardUtil {

    public static ClipboardManager getClipboardManager() {
        return (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 复制文字到剪贴板
     */
    public static void copy(String str) {
        getClipboardManager().setText(str);
    }

    /**
     * 粘贴文字到剪贴板
     */
    public static String paste() {
        return getClipboardManager().getText().toString().trim();
    }
}
