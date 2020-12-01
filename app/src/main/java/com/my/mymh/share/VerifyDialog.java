package com.my.mymh.share;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.my.mymh.R;


/**
 * 没有认证或者留下提现信息对话框
 * Created by Dave on 2016/3/26.
 */
public class VerifyDialog {
    private static Dialog permisionDialog;

    public static Dialog showVerifyDialog(Context context, String descMessage, String okMessage, View.OnClickListener okListener) {
        permisionDialog = new Dialog(context, R.style.dialog);
        permisionDialog.setContentView(R.layout.verify_dialog);
        Window window = permisionDialog.getWindow();//设置窗体位置以及动画
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
        }
        //点击外面对话框是否消失
        permisionDialog.setCanceledOnTouchOutside(true);
        permisionDialog.setOnKeyListener(keylistener);
        TextView tv_description = (TextView) permisionDialog.findViewById(R.id.tv_description);
        TextView bt_ok = (TextView) permisionDialog.findViewById(R.id.bt_permision_ok);
        bt_ok.setText(okMessage);
        tv_description.setText(descMessage);
        bt_ok.setOnClickListener(okListener);
        if (!permisionDialog.isShowing()) {
            permisionDialog.show();
        }

        return permisionDialog;
    }

    public static void close() {
        if (null != permisionDialog && permisionDialog.isShowing()) {
            permisionDialog.dismiss();
            permisionDialog = null;
        }
    }

    public static boolean isShowing() {
        return null != permisionDialog && permisionDialog.isShowing();
    }

    /**
     * 当dialog显示时,先将事件给对话框截取;当对话框消失后事件按照原来的方式进行
     */
    private static DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return keyCode == KeyEvent.KEYCODE_BACK;
        }
    };
}
