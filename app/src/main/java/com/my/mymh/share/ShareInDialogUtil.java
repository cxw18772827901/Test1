package com.my.mymh.share;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.my.mymh.R;

/**
 * 分享弹出的底部对话框
 * Created by chenxiaowu on 2017/6/2.
 */

public class ShareInDialogUtil {

    private static Dialog dialog;
    private static ShareToListener shareToListeners;
    private static boolean isCenters;

    public static void showDialog(Context context, boolean isCenter, ShareToListener shareToListener) {
        if (isShowing()) {
            return;
        }
        shareToListeners = shareToListener;
        isCenters = isCenter;
        View convertView = View.inflate(context, isCenter ? R.layout.share_choose_dialog_center : R.layout.share_choose_dialog_in_bottom, null);
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(convertView);
        Window window = dialog.getWindow();//窗体
        if (window != null) {
            if (!isCenter) {
                window.setWindowAnimations(R.style.main_menu_anim_bottom);//动画
                window.setGravity(Gravity.BOTTOM);//位置
            } else {
//                window.setWindowAnimations(R.style.main_menu_anim_center);//动画
                window.setWindowAnimations(R.style.per_fade);//动画
                window.setGravity(Gravity.CENTER);//位置
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//样式
        }
        dialog.setCanceledOnTouchOutside(true);
        initView();
        dialog.show();
    }

    private static void initView() {
        Click click = new Click();
        dialog.findViewById(R.id.ll_share_to_qq_f).setOnClickListener(click);
        dialog.findViewById(R.id.ll_share_to_qq_c).setOnClickListener(click);
        dialog.findViewById(R.id.ll_share_to_wx_f).setOnClickListener(click);
        dialog.findViewById(R.id.ll_share_to_wx_c).setOnClickListener(click);
        if (!isCenters) {
            dialog.findViewById(R.id.ll_collect).setOnClickListener(click);
            dialog.findViewById(R.id.ll_error).setOnClickListener(click);
        }
    }

    private static class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!isCenters) {
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            switch (v.getId()) {
                case R.id.ll_share_to_qq_f:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(0);
                    }
                    break;
                case R.id.ll_share_to_qq_c:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(1);
                    }
                    break;
                case R.id.ll_share_to_wx_f:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(2);
                    }
                    break;
                case R.id.ll_share_to_wx_c:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(3);
                    }
                    break;
                case R.id.ll_collect:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(4);
                    }
                    break;
                case R.id.ll_error:
                    if (null != shareToListeners) {
                        shareToListeners.shareTo(5);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static boolean isShowing() {
        return null != dialog && dialog.isShowing();
    }

    public static void close() {
        if (isShowing()) {
            dialog.dismiss();
            dialog = null;
            shareToListeners = null;
        }
    }

    public interface ShareToListener {
        void shareTo(int position);
    }
}
