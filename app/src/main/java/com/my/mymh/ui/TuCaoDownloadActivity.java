package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/8/31.
 */
@SuppressLint("Registered")
public class TuCaoDownloadActivity extends AppCompatActivity {

//    enum DOWN_LOAD_STATE {
//        DOWNLOAD_ING, DOWNLOAD_IDLE
//    }
//
//    private Dialog dialog_down;
//    private ProgressBar load_progressBar;
//    private TextView tv_pro;
//    private TextView tv_size;
//    private DOWN_LOAD_STATE DOWN_LOAD_STATE = DOWNLOAD_IDLE;
//    private boolean mustUpdate = true;
////    private String downUrl = "http://pic.laiyue.mobi/shud.apk";
//    private String downUrl = "http://mxtjxz.gzwlzyw.com/XXX/SOULAPP";
//    private String APP_DOWN_NAME;
//    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
//    private NumberFormat nt = NumberFormat.getPercentInstance();
//
//    protected void checkDirAndStart() {
//        APP_DOWN_NAME = "/吐槽_" + AppUtil.getVersionCode(this) + ".apk";
//        //check
//        if (DOWN_LOAD_STATE == DOWNLOAD_ING) {
//            return;
//        }
//        // 判断是否存在sd卡,如果存在,使用,如果不存在,存于data目录
//        File dir;
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
//        http.download(downUrl, dir.getPath() + APP_DOWN_NAME, true, new RequestCallBack<File>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                DOWN_LOAD_STATE = DOWNLOAD_ING;
//                MyDebugUtil.toast("开始下载");
//                MyDebugUtil.toast("下载中...");
//                MyDebugUtil.logTest("down", "app下载中...");
//            }
//
//            @SuppressLint("SetTextI18n")
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
//                SPUtil.put(Constants.APK_ABSOLUTE_PATH_1, apkAbsolutePath);
//                installAPK(file);
//                if (mustUpdate) {
////                    finish();
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
//                    SPUtil.put(Constants.APK_ABSOLUTE_PATH_1, apkAbsolutePath);
//                    installAPK(file);
//                    if (mustUpdate) {
////                        finish();
//                    }
//                } else {
//                    MyDebugUtil.toast("下载失败");
//                    MyDebugUtil.logTest("down", "app下载中...");
//                }
//            }
//        });
//    }
//
//    private void installAPK(File file) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
////            File file = (new File(apkPath));
//            // 由于没有在Activity环境下启动Activity,设置下面的标签
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

}
