package com.my.mymh.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

/**
 * PackageName  com.hgd.hgdcomic.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/1.
 */

public class CommonUtils {
    public static String getChannelId(Context context) {
        String channelid = "";
        try {
            channelid = CommonUtils.getFromAssets1(context, "ZYF_ChannelID");
        } catch (Exception e) {
        }
        return channelid;
    }

    public static String getFromAssets1(Context context, String fileName) {
        String channel = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("ZYF_ChannelID");
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            channel = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }
}
