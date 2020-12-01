package com.my.mymh.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.my.mymh.R;


/**
 * 底部弹出带动画的对话框
 * Created by Dave on 2016/10/24.
 */
public class MoreDialogUtil {
    private static Dialog dialog;

    //选照片的对话框
    public static void showDeleteDialog(final Context context, String userId, DeleteListener deleteListener) {
        if (isShowing()) {
            return;
        }
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(View.inflate(context, R.layout.delete_layout, null));
        //设置位置和效果
        Window window = dialog.getWindow();//设置窗体位置以及动画
        if (null != window) {
            window.setWindowAnimations(R.style.main_menu_animstyle);
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.setCanceledOnTouchOutside(true);
        //view
        TextView tv_delete = (TextView) dialog.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(view -> {
            close();
            deleteListener.doClick();
        });
        if (LoginVerifyUtil.isLogin()) {
            if (LoginVerifyUtil.isCared(userId)) {
                tv_delete.setText("取消关注");
            } else {
                tv_delete.setText("关注");
            }
        } else {
            tv_delete.setText("关注");
        }
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> close());
        dialog.show();
    }

    private static boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }

    public static void close() {
        if (isShowing()) {
            dialog.dismiss();
        }
    }

    public interface DeleteListener {
        void doClick();
    }
}
