package com.my.mymh.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 分享到微信,QQ工具类
 * Created by Dave on 2015/12/9.
 */
public class ShareUtil {
    /**
     * 分享到微信
     *
     * @param hasBitmap       是否为直接传递bitmap
     * @param type            分享类型
     * @param wxApi           微信api
     * @param flag            0,微信朋友;1,朋友圈
     * @param context         context
     * @param headImageBitmap bitmap,url传null
     * @param headUrl         bitmap传null
     * @param url             分享的链接
     * @param title           标题
     * @param content         内容
     */

    public static void shareToWX(boolean hasBitmap, int type, final IWXAPI wxApi, final int flag, Context context, Bitmap headImageBitmap, final String headUrl, String url, String title, String content) {
        final int a = 90;
        //①检测是否安装微信
        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "检测到当前并未安装信客户端,请安装后再重试", Toast.LENGTH_SHORT).show();
            return;
        }
        //②分享类型进行分享操作
        switch (type) {
            case 0://文字
                break;
            case 1://图片
                break;
            case 2://链接
                if (hasBitmap) {//传过来的参数是bitmap,压缩即可
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = url;
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = title;
                    msg.description = content;
                    //这里替换一张自己工程里的图片资源
                    Bitmap newbm = zoomImg(headImageBitmap, a, a);
                    //Bitmap.Config config=newbm.getConfig();//类型是ARGB:8888
                    msg.setThumbImage(newbm);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = String.valueOf(System.currentTimeMillis());
                    req.message = msg;
                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                    wxApi.sendReq(req);
                } else {//传过来的是url,需要生成bitmap,进行处理
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = url;
                    final WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = title;
                    msg.description = content;

                    new Thread() {//访问图片时候注意开启子线程来运行
                        public void run() {
                            HttpURLConnection conn = null;
                            try {
                                URL urlForHead = new URL(headUrl + "/thumb");

                                conn = (HttpURLConnection) urlForHead.openConnection();
                                conn.setRequestMethod("GET");
                                //设置超时
                                conn.setReadTimeout(5000);
                                conn.setConnectTimeout(5000);
                                int code = conn.getResponseCode();
                                if (200 == code) {
                                    InputStream inputStream = conn.getInputStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    Bitmap newbm = zoomImg(bitmap, a, a);
                                    msg.setThumbImage(newbm);

                                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                                    req.transaction = String.valueOf(System.currentTimeMillis());
                                    req.message = msg;
                                    req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                                    wxApi.sendReq(req);
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (conn != null) {
                                    conn.disconnect();// 记得关闭连接
                                }
                            }
                        }
                    }.start();
                }
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    /**
     * 分享到QQ
     *
     * @param type     分享类型
     * @param tencent  QQapi
     * @param listener 坚挺回调
     * @param context  context
     * @param headUrl  头像url
     * @param url      要分享的url
     * @param title    标题
     * @param content  内容
     */
    public static void shareToQQ(int type, final Tencent tencent, final IUiListener listener, final Context context, final String headUrl, String url, String title, String content) {
        switch (type) {
            case 0://文字
                break;
            case 1://图片
                break;
            case 2://链接
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, headUrl);
//                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, headUrl);
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "骨朵直播");
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, 0);
                tencent.shareToQQ((Activity) context, params, listener);
                break;
            case 3:
                break;
        }
    }

    /**
     * 分享到QQ空间
     *
     * @param type     分享类型
     * @param tencent  QQapi
     * @param listener 坚挺回调
     * @param context  context
     * @param headUrl  头像url
     * @param url      要分享的url
     * @param title    标题
     * @param content  内容
     */
    public static void shareToQZone(int type, final Tencent tencent, final IUiListener listener, final Context context, final String headUrl, String url, String title, String content) {
        switch (type) {
            case 0://文字
                break;
            case 1://图片
                break;
            case 2://链接
                final Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);//选填
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
                ArrayList<String> list = new ArrayList<>();
                list.add(headUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
                tencent.shareToQzone((Activity) context, params, listener);
                break;
            case 3:
                break;
        }
    }

    /**
     * 位图的处理
     *
     * @param headImage 需要处理的bitmap对象
     * @param newWidth  目标大小:宽度
     * @param newHeight 目标大小:高度
     * @return 返回压缩后的图
     */
    public static Bitmap zoomImg(Bitmap headImage, int newWidth, int newHeight) {
        //①获取bitmap的高宽
        int width = headImage.getWidth();
        int height = headImage.getHeight();
        //②计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //③为了防止图片的变形,取出高宽中较大的比例进行缩放
        if (scaleWidth != 0 && scaleWidth <= scaleHeight) {
            scaleHeight = scaleWidth;
        } else {
            scaleWidth = scaleHeight;
        }
        //④设置最终的缩放参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //⑤返回处理后的bitmap
        return Bitmap.createBitmap(headImage, 0, 0, width, height, matrix, true);
    }
}
