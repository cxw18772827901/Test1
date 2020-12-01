package com.my.mymh.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.Window;

import com.my.mymh.R;

/**
 * PackageName  com.hgd.hgdhole.sign
 * ProjectName  CrossTalkProject
 * Author       chenxiaowu
 * Date         2018/8/30.
 */
public class SignOkDialogUtil {
    private static Dialog permisionDialog;

    public static void showDialog(Activity context) {
        if (isShow()) {
            return;
        }
        permisionDialog = new Dialog(context, R.style.dialog);
        permisionDialog.setContentView(R.layout.sign_ok_dialog);
        Window window = permisionDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        permisionDialog.findViewById(R.id.iv_sign_ok).setOnClickListener(view -> permisionDialog.dismiss());
        permisionDialog.setCanceledOnTouchOutside(true);
        permisionDialog.show();
    }

    public static boolean isShow() {
        return permisionDialog != null && permisionDialog.isShowing();
    }

}
