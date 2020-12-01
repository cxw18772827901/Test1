package com.my.mymh.util;

import android.app.Activity;
import android.app.Dialog;
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
public class CollectDialogUtil {
    private static Dialog dialog;

    public static void showDialog(Activity context, CollectListener signListener) {
        if (isShow()) {
            return;
        }
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.collect_dialog);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            return;
        }
        TextView tv_collect = (TextView) dialog.findViewById(R.id.tv_collect);
        TextView tv_collect_not = (TextView) dialog.findViewById(R.id.tv_collect_not);
        tv_collect.setOnClickListener(v -> {
            closeDialog();
            signListener.collect();
        });
        tv_collect_not.setOnClickListener(v -> {
            closeDialog();
            signListener.collectNot();
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public static boolean isShow() {
        return dialog != null && dialog.isShowing();
    }

    public static void closeDialog() {
        if (isShow()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface CollectListener {
        void collect();

        void collectNot();
    }
}
