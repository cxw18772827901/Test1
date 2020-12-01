package com.my.mymh.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.my.mymh.R;

/**
 * PackageName  com.hgd.hgdhole.util
 * ProjectName  CrossTalkProject
 * Author       chenxiaowu
 * Date         2018/7/20.
 */
public class PhotoDialogUtil {

    private static Dialog photoDialog;

    //选照片的对话框
    public static void showChoosePicDialog(final Context context, SelPhotoListener cameraListener) {
        if (isShowing()) {
            return;
        }
        photoDialog = new Dialog(context, R.style.dialog);
        photoDialog.setContentView(View.inflate(context, R.layout.choose_photo_layout, null));
        //设置位置和效果
        Window window = photoDialog.getWindow();//设置窗体位置以及动画
        if (null != window) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        photoDialog.setCanceledOnTouchOutside(true);
        //view
        photoDialog.findViewById(R.id.tv_camera).setOnClickListener(view -> cameraListener.camera());
        photoDialog.findViewById(R.id.tv_photos).setOnClickListener(view -> cameraListener.photo());
        photoDialog.findViewById(R.id.tv_cancle).setOnClickListener(v -> cameraListener.cancel());
        photoDialog.show();
    }

    private static boolean isShowing() {
        return photoDialog != null && photoDialog.isShowing();
    }

    public static void close() {
        if (isShowing()) {
            photoDialog.dismiss();
            photoDialog = null;
        }
    }

    public interface SelPhotoListener {
        void camera();

        void photo();

        void cancel();
    }
}
