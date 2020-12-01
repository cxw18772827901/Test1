package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.request.DirectoryManager;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.util.CommonDialog;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.SPUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 * <p>
 * 设置
 */

public class SettingActivity extends BaseDownLoadActivity implements View.OnClickListener {
    public static final String TAG = "CartoonRackFragment";
    //文件大小换选
    private static final int SCALE = 1024;
    private static final long B = (long) Math.pow(SCALE, 0);
    private static final long KB = (long) Math.pow(SCALE, 1);
    private static final long MB = (long) Math.pow(SCALE, 2);
    private static final long GB = (long) Math.pow(SCALE, 3);
    private static final long TB = (long) Math.pow(SCALE, 4);
    //private static final long PB = (long) Math.pow(1024, 5);
    @ViewInject(id = R.id.rl_clear, needClick = true)
    private RelativeLayout rl_clear;
    @ViewInject(id = R.id.tv_clear_cache)
    private TextView tv_clear_cache;
    @ViewInject(id = R.id.switch_button)
    private SwitchButton switch_button;
    @ViewInject(id = R.id.tv_update, needClick = true)
    private TextView tv_update;
    @ViewInject(id = R.id.tv_about, needClick = true)
    private TextView tv_about;
    @ViewInject(id = R.id.tv_login, needClick = true)
    private TextView tv_login;
    @ViewInject(id = R.id.tv_test, needClick = true)
    private TextView tv_test;
    private String volleyCacheDir;
    //    private Handler handler = new Handler();
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.setting_fragment);
        init();
        initView();
        initData();
    }

    private void init() {
        setTitleName("设置");
        decimalFormat = new DecimalFormat("0.0");
        EventBus.getDefault().register(this);
    }

    private void initView() {
        switch_button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SPUtil.put(Constants.WIFI_SWITCH, isChecked);
            if (!isChecked) {
                MyDebugUtil.toast("关闭后使用手机移动数据浏览时将不再提醒");
            }
        });
        tv_login.setText(LoginVerifyUtil.isLogin() ? "退出" : "登录");
    }

    private void initData() {
        getCacheSize();
    }

    private void getCacheSize() {
        long volleyCacheSize = getVolleyCacheSize();
        long glideCacheSize = getGlideCacheSize();
        MyDebugUtil.log(TAG, "volleyCacheSize=" + volleyCacheSize);
        MyDebugUtil.log(TAG, "glideCacheSize=" + glideCacheSize);
        long totalSize = volleyCacheSize + glideCacheSize;
        forMatCache(totalSize);
    }

    //按照B,KB,MB和GB来格式化缓存数字的大小
    private void forMatCache(float totalSize) {
        if (0 == totalSize) {
            tv_clear_cache.setText("当前无缓存");
            rl_clear.setEnabled(false);
        } else {
            String sizeFormated = "0";
            if (totalSize >= B && totalSize < KB) {
                sizeFormated = decimalFormat.format(totalSize / B) + "B";
            } else if (totalSize >= KB && totalSize < MB) {
                sizeFormated = decimalFormat.format(totalSize / KB) + "KB";
            } else if (totalSize >= MB && totalSize < GB) {
                sizeFormated = decimalFormat.format(totalSize / MB) + "MB";
            } else if (totalSize >= GB && totalSize < TB) {
                sizeFormated = decimalFormat.format(totalSize / GB) + "GB";
            }
            tv_clear_cache.setText(/*"清除缓存(" + */sizeFormated /*+ ")"*/);
            rl_clear.setEnabled(true);
        }
    }

    private long getVolleyCacheSize() {
        // Volley目录
        File volleyFile = DirectoryManager.getDirectory(DirectoryManager.DIR_VOLLEY_CACHE);
        String volleyAbsolutePath = volleyFile.getAbsolutePath();
        volleyCacheDir = volleyAbsolutePath + "/" + NetClient.DEFAULT_CACHE_DIR;
        MyDebugUtil.logTest("cache", "volley的缓存目录是=" + volleyCacheDir);
        return getFileSize(new File(volleyCacheDir));
    }

    public long getGlideCacheSize() {
        try {
            return getFileSize(Glide.getPhotoCacheDir(this));// getFileSize(new File(BaseApplication.getContext().getCacheDir() + "/" + Glide.GLIDE_CARCH_DIR))
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //递归获取缓存数据的大小
    public long getFileSize(File f) {
        long size = 0;
        File files[] = f.listFiles();
        if (null == files || 0 == files.length) {
            return 0;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                size += getFileSize(file);
            } else {
                size += file.length();
            }
        }
        return size;
    }

    private void clearCache() {
//      handler.postDelayed(() -> {
        rl_clear.postDelayed(() -> {
            //直接将volley和picassio两个缓存目录下的所有缓存内容全部删掉即可
            DirectoryManager.clear(new File(volleyCacheDir));
            DirectoryManager.clear(Glide.getPhotoCacheDir(this));
            tv_clear_cache.setText("当前无缓存");
            rl_clear.setEnabled(false);
            MyDebugUtil.toast("清除完毕");
        }, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_clear:
                clearCache();
                break;
            case R.id.tv_about:
                IntentUtil.startActivity(this, AboutUsActivity.class);
                break;
            case R.id.tv_update:
                checkUpDate(1);
                break;
            case R.id.tv_login:
                if (LoginVerifyUtil.isLogin()) {
                    CommonDialog.showDialog(this, "是否退出登录?", "确认", "取消", view -> {
                        CommonDialog.close();
                        switch (view.getId()) {
                            case R.id.bt_ok:
                                tv_login.setText("登录");
                                LoginVerifyUtil.clearUserInfo();
                                IntentUtil.startActivity(this, LoginActivity.class);
                                EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_OUT));
                                break;
                            default:
                                break;
                        }
                    });
                } else {
                    IntentUtil.startActivity(this, LoginActivity.class);
                }
                break;
            case R.id.tv_test:
                test();
                break;
        }
    }

    private void test() {
        String url = "http://api.test.tzlc51.com:8080/user/anchor/page";
        Map<String, String> params = new HashMap<>();
        params.put("size", "10");
        params.put("page", "0");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.e("stringRequest", response);
        }, error -> {
            Log.e("stringRequest", "error");
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("X-Wap-Proxy-Cookie", "none");
                header.put("Content-Type", "application/json;charset=utf-8");
                header.put("device", "Android");
                header.put("version", "1.4.1");
                header.put("appFlag", "hem");
                header.put("locale", "en_US");
                header.put("auth", "kcC/8dsUO9BX1BfsLV8W5YJe4V23KmIq+wj5DnQ4vzBSMmdXpmzRNhtHumKTRy1Ga91U96q6VcVs+whRXSmK0g==");
                return header;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String str = null;
                try {
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str,
                        HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public String getBodyContentType() {
                return "application/json;charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return new Gson().toJson(params).getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetClient.request(stringRequest);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoginOrOut(EventBusEvent.LoginAndOutEvent loginAndOutEvent) {
        if (EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_QQ == loginAndOutEvent.loginEventType ||
                EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_WX == loginAndOutEvent.loginEventType) {
            tv_login.setText("退出");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
