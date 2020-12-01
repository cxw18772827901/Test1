package com.my.mymh.util;

import com.my.mymh.util.debugLog.MyDebugUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;

/**
 * 分享
 * Created by dave on 2016/4/1.
 */
public class ShareListener implements IUiListener {

    @Override
    public void onCancel() {
        MyDebugUtil.toast("已取消");
    }

    @Override
    public void onComplete(Object arg0) {
        MyDebugUtil.toast("分享成功");
        EventBus.getDefault().post(new EventBusEvent.ShareEvent(EventBusEvent.ShareEvent.TYPE_SHARE));
    }

    @Override
    public void onError(UiError arg0) {
        MyDebugUtil.toast("请重试");
    }
}