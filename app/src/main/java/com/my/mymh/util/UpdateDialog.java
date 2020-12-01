package com.my.mymh.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.my.mymh.R;


/**
 * 更新版本对话框
 * Created by Dave on 2016/3/26.
 */
public class UpdateDialog {
    private static Dialog permisionDialog;

    public static Dialog showVerifyDialog(Context context, String titleMessage, String descMessage, String okMessage, String cancleMessage, View.OnClickListener okListener) {
        permisionDialog = new Dialog(context, R.style.dialog);
        permisionDialog.setContentView(R.layout.update_dialog);
        Window window = permisionDialog.getWindow();//设置窗体位置以及动画
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
        }
        //点击外面对话框是否消失
        permisionDialog.setCanceledOnTouchOutside(false);
        permisionDialog.setOnKeyListener(keylistener);
        TextView tv_title = (TextView) permisionDialog.findViewById(R.id.tv_permision_title);
        TextView tv_description = (TextView) permisionDialog.findViewById(R.id.tv_description);
        TextView bt_ok = (TextView) permisionDialog.findViewById(R.id.bt_permision_ok);
        TextView bt_cancel = (TextView) permisionDialog.findViewById(R.id.bt_permision_cancel);
        tv_title.setText(titleMessage);
        bt_ok.setText(okMessage);
        bt_cancel.setText(cancleMessage);
        tv_description.setText(descMessage);
        bt_ok.setOnClickListener(okListener);
        bt_cancel.setOnClickListener(okListener);
        permisionDialog.show();
        return permisionDialog;
    }

    public static void close() {
        if (null != permisionDialog && permisionDialog.isShowing()) {
            permisionDialog.dismiss();
            permisionDialog = null;
        }
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
