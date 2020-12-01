package com.my.mymh.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.my.mymh.R;

/**
 * PackageName  com.hgd.hgdhole.sign
 * ProjectName  CrossTalkProject
 * Author       chenxiaowu
 * Date         2018/8/30.
 */
public class SignDialogUtil {
    private static Dialog permisionDialog;
    private static boolean cancelClose = false;
    @SuppressLint("StaticFieldLeak")
    private static TextView tv_sign;
    @SuppressLint("StaticFieldLeak")
    private static Activity contexts;
    private static SignListener signListeners;

    public static void showDialog(Activity context, SignListener signListener) {
        if (isShow()) {
            return;
        }
        contexts = context;
        signListeners = signListener;
        permisionDialog = new Dialog(context, R.style.dialog);
        permisionDialog.setContentView(R.layout.sign_dialog);
        Window window = permisionDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            return;
        }
        TextView tv_close = (TextView) permisionDialog.findViewById(R.id.tv_close);
        tv_sign = (TextView) permisionDialog.findViewById(R.id.tv_sign);
        TextView tv_mini = (TextView) permisionDialog.findViewById(R.id.tv_mini);
        //关闭
        tv_close.setOnClickListener(view -> {
            cancelClose = true;
            closeDialog();
        });
        //打开小程序
        tv_mini.setOnClickListener(view -> SignJumpUtil.signMiniTC());
        //签到或者下载
        boolean notInstalled = SignJumpUtil.isApkNotInstalled(context);
        tv_sign.setText(notInstalled ? "下载app" : "打开app");
        tv_sign.setOnClickListener(view -> {
            if (notInstalled) {
                signListener.download();
            } else {
                SignJumpUtil.signTCAPP(context);
            }
        });
        permisionDialog.setCanceledOnTouchOutside(false);
        permisionDialog.setOnKeyListener(onKeyListener);
        permisionDialog.setOnDismissListener(dialogInterface -> {
            if (cancelClose) {
                signListener.close();
            }
        });
        permisionDialog.show();
    }

    public static boolean isShow() {
        return permisionDialog != null && permisionDialog.isShowing();
    }

    public static void closeDialog() {
        if (isShow()) {
            permisionDialog.dismiss();
            permisionDialog = null;
        }
    }

    private static Dialog.OnKeyListener onKeyListener = (dialogInterface, keyCode, keyEvent) -> {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            cancelClose = true;
//            closeDialog();
            return true;
        }
        return false;
    };

    public static void updateView() {
        boolean notInstalled = SignJumpUtil.isApkNotInstalled(contexts);
        tv_sign.setText(notInstalled ? "下载app" : "打开app");
        tv_sign.setOnClickListener(view -> {
            if (notInstalled) {
                signListeners.download();
            } else {
                SignJumpUtil.signTCAPP(contexts);
            }
        });
    }

    public interface SignListener {
        void download();

        void close();
    }
}
