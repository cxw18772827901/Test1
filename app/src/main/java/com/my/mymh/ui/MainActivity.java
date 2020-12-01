package com.my.mymh.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.my.mymh.R;
import com.my.mymh.base.BaseApplication;
import com.my.mymh.base.Constants;
import com.my.mymh.share.ShareOutDialogUtil;
import com.my.mymh.util.CheckPermissonUtil;
import com.my.mymh.util.CommonDialog;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.JudgeNetUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.NewDataDialog;
import com.my.mymh.util.SPUtil;
import com.my.mymh.util.ShareListener;
import com.my.mymh.util.ShareUtil;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.BottomTabHost;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tencent.connect.common.Constants.REQUEST_QQ_SHARE;
import static com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 */

public class MainActivity extends BaseDownLoadActivity implements BottomTabHost.OnTabChangedListener {
    public static final String TAG = "MainActivity";
    private static final List<String> tvList = Arrays.asList("书架", "漫库", "分类", "设置");
    private static final List<Integer> imgList = Arrays.asList(R.drawable.tab1, R.drawable.tab2, R.drawable.tab3, R.drawable.tab4);
    @ViewInject(id = R.id.tabhost)
    private BottomTabHost myTabHost;
    private ShareListener shareListener;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setTab(intent);
    }

    private void setTab(Intent intents) {
        Intent intent = intents;
        if (null == intent) {
            intent = getIntent();
            if (null != intent) {
                String index = intent.getStringExtra("index");
                if (!TextUtils.isEmpty(index)) {
                    myTabHost.setCurrentTab(Integer.parseInt(index));
                }
            }
        } else {
            String index = intent.getStringExtra("index");
            if (!TextUtils.isEmpty(index)) {
                myTabHost.setCurrentTab(Integer.parseInt(index));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BottomTabHost.TAB, myTabHost.getCurrentTab());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.main_activity);
        init();
        initView(bundle);
        initData();
    }

    private void init() {
        EventBus.getDefault().register(this);
        TitleBarUtil.initImmersiveBar(this, R.color.cl_main_red_head);
        setTab(null);
        checkPermissions();
//        setLeftIvClick(R.drawable.ic_rank, view -> IntentUtil.startActivity(MainActivity.this, RankActivity.class));
    }

    private final int PERMISSION_REQUEST_CODE = 1;//检查权限的code

    private void checkPermissions() {
        List<String> permissionsList = Arrays.asList(Manifest.permission.WRITE_EXTERNAL_STORAGE/*, Manifest.permission.READ_PHONE_STATE*/);
        if (!CheckPermissonUtil.hasPermission(this, permissionsList)) {
            CheckPermissonUtil.requestPermissions(this, permissionsList, PERMISSION_REQUEST_CODE, "是否允许权限 ?", () -> CommonDialog.showDialog(MainActivity.this, "未获取权限则无法继续使用，是否开启权限？", "确认开启", "依然拒绝", v -> {
                CommonDialog.close();
                switch (v.getId()) {
                    case R.id.bt_ok:
                        ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), PERMISSION_REQUEST_CODE);
                        break;
                    case R.id.bt_cancel:
                        IntentUtil.finishActivityWithAnim(MainActivity.this);
                        break;
                }
            }));
        } else {
            checkUpDate(0);
//            ActiveLogUtil.sendActiveLog(0, this, CommonUtils.getAppId(this), CommonUtils.getChannelId(this));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Map<String, Integer> pers = new HashMap<>();
//                pers.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                pers.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++) {
                    pers.put(permissions[i], grantResults[i]);
                }
                if (/*pers.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&*/
                        pers.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    checkUpDate(0);
//                    ActiveLogUtil.sendActiveLog(0, this, CommonUtils.getAppId(this), CommonUtils.getChannelId(this));
                } else {
                    MyDebugUtil.toast("如果您已经勾选不再提醒,需要手动打开权限");
                    IntentUtil.finishActivityWithAnim(this);
                }
                break;
        }
    }

    private void initView(Bundle bundle) {
        myTabHost.init(bundle, getSupportFragmentManager(), R.id.flContainer, tvList, imgList/*, fragmentList*/, -1, -1);//不需要消息pop和影藏某一个tab栏就传负数就可以了
        myTabHost.setOnTabChangedListener(this);
        if (null != bundle) {
            tabChange(bundle.getInt(BottomTabHost.TAB));
        }
        shareListener = new ShareListener();
    }

    public void initData() {
        //删除之前apk包
        String apkPath = SPUtil.getString(Constants.APK_ABSOLUTE_PATH_1);
        if (!TextUtils.isEmpty(apkPath)) {
            File file = new File(apkPath);
            file.deleteOnExit();
            SPUtil.put(Constants.APK_ABSOLUTE_PATH_1, "");
        }
        //login
        LoginVerifyUtil.login();
        SPUtil.put(Constants.LOGIN_TAG, System.currentTimeMillis());
    }

    @Override
    public void tabChange(int tabIndex) {
//        if (1 == tabIndex) {
//            EventBus.getDefault().post(new EventBusEvent.FreshOrDeleteEvent(EventBusEvent.FreshOrDeleteEvent.TYPE_FRESH));
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            wantExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void wantExit() {
        CommonDialog.showDialog(this, "是否退出?", "确认", "取消", v -> {
            CommonDialog.close();
            switch (v.getId()) {
                case R.id.bt_ok:
                    moveTaskToBack(true);
                    break;
                case R.id.bt_cancel:
                    break;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);//友盟统计
        long lastTime = SPUtil.getLong(Constants.LOGIN_TAG);
        long timeNow = System.currentTimeMillis();
        if (lastTime != -1 && timeNow > lastTime && timeNow - lastTime > 30 * 60 * 1000) {
            LoginVerifyUtil.login();
            SPUtil.put(Constants.LOGIN_TAG, timeNow);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);//友盟统计
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewDataEvent(EventBusEvent.NewDataEvent newDataEvent) {
        NewDataDialog.showDialog(this, "您收藏的作品更新了！", "立即查看", "我知道了", v -> {
            NewDataDialog.close();
            if (v.getId() == R.id.bt_ok) {
                int currentTab = myTabHost.getCurrentTab();
                if (currentTab != 0) {
                    myTabHost.setCurrentTab(0);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        MobclickAgent.onKillProcess(this);
    }

    public void share() {
        ShareOutDialogUtil.showDialog(this, false, position -> {
//            if (null == result) {
//                MyDebugUtil.toast("请重试~");
//                return;
//            }
            if (!JudgeNetUtil.hasNet(this)) {
                MyDebugUtil.toast("请连接网络后重试~");
                return;
            }
            String logoUrl = "https://mmbiz.qlogo.cn/mmbiz_png/RTMaDfovwlq1whwR5Tm1O5qbJWogsmnaNmbStotJvGVsSzqcJEynicITDLmUS52AEDAEeRofic7etdgn9icwB1vmA/0?wx_fmt=png";// Constants.QINIU_GET_ICON_HEADER_BASE + "share_logo.png";//分享用logo
//            String shareUrl = "http://manga.zishupai.cn/share/share.html"; //分享的链接
            String shareUrl = "http://post.zishupai.cn/c/share.html"; //分享的链接
            String title = "快和我一起看漫画！";//分享的标题
            String content = "看漫画就到咔米漫画，更新快、漫画全，小伙伴们一起来看吧！";//描述
            switch (position) {
                case 0://qq好友
                    ShareUtil.shareToQQ(2, BaseApplication.mTencent, shareListener, this, logoUrl, shareUrl, title, content);
                    break;
                case 1://qq朋友圈
                    ShareUtil.shareToQZone(2, BaseApplication.mTencent, shareListener, this, logoUrl, shareUrl, title, content);
                    break;
                case 2://微信好友
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 0, this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
//                    tiMini();
                    break;
                case 3://微信朋友圈
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 1, this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QQ_SHARE || requestCode == REQUEST_QZONE_SHARE)
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
    }
}
