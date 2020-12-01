package com.my.mymh.util;

import android.text.TextUtils;

import com.my.mymh.model.record.GetQiNiuKeyRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

/**
 * 七牛上传图片工具类:操作是每次都会重新获取七牛的token,
 * Created by Dave on 2017/2/20.
 */
public class QiNiuUploadUtil {
    public static final String TAG = "QiNiuUploadUtil";
    public static final String KEY = "CkjRb1E4A5Ke2zJ1TpDT51EdNUmoF8O4TKXcp3rWxejiZGiG";
    private static UploadManager uploadManager;

    private static UploadManager getUploadManager() {
        if (null != uploadManager) {
            return uploadManager;
        } else {
            Configuration config = new Configuration.Builder().build();
            uploadManager = new UploadManager(config);
            return uploadManager;
        }
    }

    /**
     * 七牛上传图片的操作
     *
     * @param path                数据，可以是 byte 数组、文件路径、文件
     * @param qianZhui            前缀
     * @param upCompletionHandler 上传回调函数，必填
     * @param uploadOptions       如果需要进度通知、crc 校验、中途取消、指定 mimeType，则需要填写相应字段，详见下面的 UploadOptions 参数说明
     */
    public static void upLoadImg(final String path, final String qianZhui, final UpCompletionHandler upCompletionHandler, final UploadOptions uploadOptions) {
        ObjectRequest<GetQiNiuKeyRecord> reponse = new ObjectRequest<>(false, GetQiNiuKeyRecord.Input.buildInput(),
                getQiNiuKeyRecord -> {
                    if (null != getQiNiuKeyRecord && null != getQiNiuKeyRecord.result &&
                            getQiNiuKeyRecord.code == 1000 && !TextUtils.isEmpty(getQiNiuKeyRecord.result)) {
                        MyDebugUtil.logTest(TAG, "获取token成功,token=" + getQiNiuKeyRecord.result);
                        upLoad(path, qianZhui, upCompletionHandler, uploadOptions, getQiNiuKeyRecord.result);
                    } else {
                        MyDebugUtil.logTest(TAG, "获取七牛token失败");
                    }
                },
                error -> MyDebugUtil.logTest(TAG, "获取七牛token失败"));
        NetClient.request(reponse);
    }

    /**
     * 七牛上传图片的操作
     *
     * @param qianZhui            前缀
     * @param path                数据，可以是 byte 数组、文件路径、文件
     * @param upCompletionHandler 上传回调函数，必填
     * @param uploadOptions       如果需要进度通知、crc 校验、中途取消、指定 mimeType，则需要填写相应字段，详见下面的 UploadOptions 参数说明
     * @param token               上传到七牛的token
     */
    public static void upLoad(String path, String qianZhui, UpCompletionHandler upCompletionHandler, UploadOptions uploadOptions, String token) {
        UploadManager uploadManager = getUploadManager();
        String key = getUpLoadImgName(path, qianZhui);
        uploadManager.put(path, key, token, upCompletionHandler, uploadOptions);
    }

    /**
     * 通过路径名设置上传图片名字
     * 统一上传文件名为:前缀+用户id+"_"+当前时间+.文件名以及后缀
     *
     * @param filePath 文件路径
     * @param qianZhui 前缀
     */
    public static String getUpLoadImgName(String filePath, String qianZhui) {
        int index = filePath.lastIndexOf(".");
        MyDebugUtil.logTest(TAG, "index=" + index);
        return qianZhui + "_" + System.currentTimeMillis() + filePath.substring(index, filePath.length());
    }

}
