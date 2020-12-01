package com.my.mymh.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * PackageName  com.hgd.hgdnovel.ui
 * ProjectName  NovelProject
 * Author       chenxiaowu
 * Date         2018/11/9.
 */
public class QRActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "QRActivity";
    @ViewInject(id = R.id.iv_qr)
    private ImageView iv_qr;
    @ViewInject(id = R.id.tv_save_qr, needClick = true)
    private TextView tv_save_qr;
    private boolean hasSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.qractivity);
        init();
    }

    private void init() {
        setTitleName("小程序二维码");
    }

//    @Override
//    public void setThemeAndLayoutId() {
//        activity_theme = APP_THEME.BLUE_THEME;
//        activity_layoutId = R.layout.qractivity;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save_qr:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_qr_novel);
                saveImg(bitmap);
                break;
        }
    }

    public void saveImg(Bitmap bitmap) {
        if (hasSaved) {
            MyDebugUtil.toast("已保存！");
            return;
        }
        String extraPath = Environment.getExternalStorageDirectory().getPath();
        String path = extraPath + "/DCIM/Camera/";
        String path1 = extraPath + "/DCIM/";
        String appDir;
        File file = new File(path);
        if (file.exists()) {
            appDir = path;
        } else {
            appDir = path1;
        }
        String name = System.currentTimeMillis() + ".jpg";
        file = new File(appDir, name);
        if (file.exists()) {
            MyDebugUtil.toast("已保存！");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(getContentResolver(),/*bitmap*/ file.getAbsolutePath(), name, null);
            // 最后通知图库更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            MyDebugUtil.toast("保存成功！");
            hasSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hasSaved = false;
    }
}
