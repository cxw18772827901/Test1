package com.my.mymh.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.my.mymh.R;


/**
 * 当用户勾选不再提醒时,下次进入时系统不再提醒,但是如果不再提醒体验不好,我们需要自己弹窗来提醒用户
 * Created by Dave on 2016/3/26.
 */
public class NewDataDialog {
    private static Dialog permisionDialog;

    public static void showDialog(Context context, String titleMessage, String okMessage, String cancleMessage, View.OnClickListener okListener) {
        if (null != permisionDialog && permisionDialog.isShowing()) {
            return;
        }
        permisionDialog = new Dialog(context, R.style.dialog);
        permisionDialog.setContentView(R.layout.permision_dialog);
        Window window = permisionDialog.getWindow();//设置窗体位置以及动画
        if (window != null) {
            window.setWindowAnimations(R.style.per_fade);
        }
        //点击外面对话框是否消失
//        permisionDialog.setCanceledOnTouchOutside(false);
//        permisionDialog.setOnKeyListener(keylistener);
        TextView tv_title = (TextView) permisionDialog.findViewById(R.id.tv_permision_title);
//        TextView tv_check = (TextView) permisionDialog.findViewById(R.id.tv_check);
        TextView bt_ok = (TextView) permisionDialog.findViewById(R.id.bt_ok);
        TextView bt_cancel = (TextView) permisionDialog.findViewById(R.id.bt_cancel);
        tv_title.setText(titleMessage);
        bt_ok.setText(TextUtils.isEmpty(okMessage) ? "允许授权" : okMessage);
        bt_cancel.setText(TextUtils.isEmpty(cancleMessage) ? "拒绝授权" : cancleMessage);
        bt_ok.setOnClickListener(okListener);
        bt_cancel.setOnClickListener(okListener);
        if (!permisionDialog.isShowing()) {
            permisionDialog.show();
        }
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
