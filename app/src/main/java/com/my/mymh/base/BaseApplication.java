package com.my.mymh.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.my.mymh.request.DirectoryManager;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.util.CommonUtils;
import com.my.mymh.util.choosePic.ISNav;
import com.my.mymh.util.choosePic.common.ImageLoader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

/**
 * Package  com.dave.project.base
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/10/27.
 */

public class BaseApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static String WX_APP_ID = "wxbe6ea2b3bad43b97";
    public static final String WX_SECRET = "c5b0583c76666aeb2b1d74cefd6dff6a";
    private static String QQ_APP_ID = "1106775500";
    public static Tencent mTencent;
    public static IWXAPI mIWXAPI;

    //md5 7A:F7:81:F2:0C:20:C5:80:12:57:6C:BB:E9:2E:E9:6E
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    private void init() {
        //路径管理类初始化
        DirectoryManager.init(this);
        //Volley允许SSL访问（所有的Https请求统一初始化）
        //HttpsTrustManager.allowAllSSL();
        //Volley初始化网络请求队列以及缓存目录
        NetClient.init(this);
        //glide
        ISNav.getInstance().init((ImageLoader) (context, path, imageView) -> Glide.with(context).load(path).into(imageView));
        //友盟
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "5a97cd9df43e4829ee000028", CommonUtils.getChannelId(this));
        MobclickAgent.startWithConfigure(config);
        //qq
        mTencent = initQQ(this);
        //微信
        mIWXAPI = initWx(this);
        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "1d736a6f5e", false);
//        //广告
//        FalconAdEntrance.getInstance().init(this, GGConstants.GG_APP_ID);
//        AdConfig adConfig = new AdConfig(this).setConfigMode(AdConfig.CONFIG_EVERYTIME);
//        SpreadManager.getInstance(this).init(adConfig);
//        InsertManager.getInstance(this).init(adConfig);
//        BannerManager.getInstance(this).init(adConfig);
//        NativeManager.getInstance(this).init(adConfig);
//        VideoManager.getInstance(this).init(adConfig);
//        FalconAdEntrance.getInstance().init(this, GGConstants.GG_APP_ID);
//       高磊广告
//        AdGlobalMgr.init(this, "ea20e3e0369b91bbc234215eef456113");
    }

    //获取context
    public static Context getContext() {
        return context;
    }

    //微信初始化
    public static IWXAPI initWx(Context context) {
        IWXAPI mIWXAPI = WXAPIFactory.createWXAPI(context, BaseApplication.WX_APP_ID, true);
        mIWXAPI.registerApp(BaseApplication.WX_APP_ID);
        return mIWXAPI;
    }

    //QQ初始化
    public static Tencent initQQ(Context context) {
        return Tencent.createInstance(BaseApplication.QQ_APP_ID, context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
