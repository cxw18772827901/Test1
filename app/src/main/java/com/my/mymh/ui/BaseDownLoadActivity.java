package com.my.mymh.ui;

import android.annotation.SuppressLint;

import com.my.mymh.ui.base.BaseActivity;

/**
 * 主页基类,处理下载app相关操作
 * Created by chenxiaowu on 2017/7/4.
 */
@SuppressLint("Registered")
public class BaseDownLoadActivity extends BaseActivity {
//    private String downUrl;
//    private boolean mustUpdate;
//    private boolean isShowing;
//    private File dir;
//    private int tag;
//
//    enum DOWN_LOAD_STATE {
//        DOWNLOAD_ING, DOWNLOAD_IDLE
//    }
//
//    private DOWN_LOAD_STATE DOWN_LOAD_STATE = DOWNLOAD_IDLE;
//    private ProgressBar load_progressBar;
//    private Dialog dialog_down;
//    private HttpHandler<File> fileHttpHandler;
//    private TextView tv_pro;
//    private NumberFormat nt;
//    private String APP_DOWN_NAME;
//    private TextView tv_size;
//    private DecimalFormat decimalFormat;

    public void checkUpDate(int tag) {
//        this.tag = tag;
//        decimalFormat = new DecimalFormat("0.00");
//        APP_DOWN_NAME = "/咔米漫画_" + AppUtil.getVersionCode(this) + ".apk";
//        nt = NumberFormat.getPercentInstance();
//        //设置百分数精确度2即保留两位小数
//        nt.setMinimumFractionDigits(0);
//        //删除之前apk包
//        String apkPath = SPUtil.getString(Constants.APK_ABSOLUTE_PATH);
//        if (!TextUtils.isEmpty(apkPath)) {
//            File file = new File(apkPath);
//            file.deleteOnExit();
//            SPUtil.put(Constants.APK_ABSOLUTE_PATH, "");
//        }
//        checkClient();
    }

//    private void checkClient() {
//        String appid = CommonUtils.getAppId(this);
//        String version = AppUtil.getVersionName(this) + "";
//        ObjectRequest<CheckVersionRecord> reponse = new ObjectRequest<>(false, CheckVersionRecord.Input.buildInput(appid, version),
//                checkVersionRecord -> {
//                    if (null != checkVersionRecord && 0 == checkVersionRecord.code) {
//                        //没有弹框,空闲
//                        if (DOWN_LOAD_STATE == DOWNLOAD_IDLE && !isShowing) {
//                            int needUpdate = checkVersionRecord.appInfo.needUpdate;//0不需要更新,1强制更新,2正常更新
//                            if (0 != needUpdate) {
//                                isShowing = true;
//                                downUrl = checkVersionRecord.appInfo.apkUrl;
//                                mustUpdate = (1 == needUpdate);
//                                if (!TextUtils.isEmpty(downUrl)) {
//                                    if (downUrl.contains("tongpei")) {
//                                        downUrl = downUrl.replace("tongpei", CommonUtils.getChannelId(BaseDownLoadActivity.this));
//                                    }
//                                    MyDebugUtil.logTest("down", "下载地址downUrl=" + downUrl);
//                                    showDialog();
//                                } else {
//                                    if (1 == tag) {
//                                        MyDebugUtil.toast("访问失败");
//                                    }
//                                }
//                            } else {
//                                if (1 == tag) {
//                                    MyDebugUtil.toast("当前是最新版本");
//                                }
//                            }
//                        }
//                    } else {
//                        if (1 == tag) {
//                            MyDebugUtil.toast("当前是最新版本");
//                        }
//                    }
//                },
//                error -> {
//                    if (1 == tag) {
//                        MyDebugUtil.toast("访问失败");
//                    }
//                });
//        NetClient.request(reponse);
//    }
//
//    private void showDialog() {
//        String title = mustUpdate ? "发现新版本，需要立即更新" : "发现新版本,是否立即更新";
//        UpdateDialog.showVerifyDialog(this, "", title, "开始下载", mustUpdate ? "退出应用" : "下次再说", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.bt_permision_ok:
//                        checkDirAndStart();
//                        UpdateDialog.close();
//                        break;
//                    case R.id.bt_permision_cancel:
//                        if (mustUpdate) {
//                            System.exit(0);
//                        } else {
//                            UpdateDialog.close();
//                        }
//                        break;
//                }
//                isShowing = false;
//            }
//        });
//    }
//
//    private void checkDirAndStart() {
//        // 判断是否存在sd卡,如果存在,使用,如果不存在,存于data目录
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            // /mnt/sdcard
//            dir = Environment.getExternalStorageDirectory();
//        } else {
//            // /data/data/自己的包名/files/
//            dir = getFilesDir();
//        }
//        if (mustUpdate) {
//            showPro();
//        }
//        //开始下载
//        HttpUtils http = new HttpUtils();
////        downUrl=downUrl.replace(" ","");
//        fileHttpHandler = http.download(downUrl, dir.getPath() + APP_DOWN_NAME, true, new RequestCallBack<File>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                DOWN_LOAD_STATE = DOWNLOAD_ING;
//                MyDebugUtil.toast("开始下载");
//                MyDebugUtil.toast("下载中...");
//                MyDebugUtil.logTest("down", "app下载中...");
//            }
//
//            @Override
//            public void onLoading(long total, long current, boolean isUploading) {
//                super.onLoading(total, current, isUploading);
//                DOWN_LOAD_STATE = DOWNLOAD_ING;
//                MyDebugUtil.logTest("down", "app下载中...");
//                if (mustUpdate) {
//                    load_progressBar.setMax((int) total);
//                    load_progressBar.setProgress((int) current);
//                    //百分比
//                    float baifen = (float) current / total;
//                    tv_pro.setText(nt.format(baifen));
//                    String formatTotal = decimalFormat.format((double) total / 1024 / 1024) + "MB";
//                    String formatCurrent;
//                    if (current / 1024 < 1024) {
//                        formatCurrent = decimalFormat.format((double) current / 1024) + "KB";
//                    } else {
//                        formatCurrent = decimalFormat.format((double) current / 1024 / 1024) + "MB";
//                    }
//                    tv_size.setText(formatTotal + "/" + formatCurrent);
//                }
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<File> responseInfo) {
//                DOWN_LOAD_STATE = DOWNLOAD_IDLE;
//                if (mustUpdate) {
//                    dialog_down.dismiss();
//                }
//                MyDebugUtil.toast("app下载成功,即将开始安装");
//                MyDebugUtil.logTest("down", "app下载中...");
//                File file = new File(dir, APP_DOWN_NAME);
//                String apkAbsolutePath = file.getAbsolutePath();
//                SPUtil.put(Constants.APK_ABSOLUTE_PATH, apkAbsolutePath);
//                installAPK(file);
//                if (mustUpdate) {
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                DOWN_LOAD_STATE = DOWNLOAD_IDLE;
//                if (mustUpdate) {
//                    dialog_down.dismiss();
//                }
//                File file = new File(dir, APP_DOWN_NAME);
//                if (file.exists()) {
//                    String apkAbsolutePath = file.getAbsolutePath();
//                    SPUtil.put(Constants.APK_ABSOLUTE_PATH, apkAbsolutePath);
//                    installAPK(file);
//                    if (mustUpdate) {
//                        finish();
//                    }
//                } else {
//                    MyDebugUtil.toast("下载失败");
//                    MyDebugUtil.logTest("down", "app下载中...");
//                }
//            }
//        });
//    }
//
//    private void showPro() {
//        dialog_down = new Dialog(this, R.style.dialog);
//        dialog_down.setContentView(R.layout.dialog_down_progress);
//        //点击外面对话框是否消失
//        dialog_down.setCanceledOnTouchOutside(false);
//        dialog_down.setOnKeyListener(keylistener);
//        load_progressBar = (ProgressBar) dialog_down.findViewById(R.id.load_progressBar);
//        tv_pro = (TextView) dialog_down.findViewById(R.id.tv_pro);
//        tv_size = (TextView) dialog_down.findViewById(R.id.tv_size);
//        dialog_down.show();
//    }
//
//    private Dialog.OnKeyListener keylistener = (dialog, keyCode, event) -> KeyEvent.KEYCODE_BACK == keyCode;
//
//    private void installAPK(File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//            Uri apkUri = DownloadProvider.getUriForFile(this, "com.hgd.hgdcomic.installapkly", file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            // 定义uri
//            Uri data = Uri.fromFile(file);
//            intent.setDataAndType(data, "application/vnd.android.package-archive");
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (null != fileHttpHandler) {
//            fileHttpHandler.cancel();
//        }
//    }
}
