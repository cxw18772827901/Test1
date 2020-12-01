package com.my.mymh.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.my.mymh.db.dao.SignDao;

import org.greenrobot.eventbus.EventBus;

/**
 * PackageName  com.hgd.hgdnovel.util
 * ProjectName  NovelProject
 * Author       chenxiaowu
 * Date         2018/8/30.
 */
public class SignReceiver extends BroadcastReceiver {
    private static final String SIGN_OK_ACTION_CARTOON = "SIGN_OK_ACTION_CARTOON";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && SIGN_OK_ACTION_CARTOON.equals(intent.getAction())) {
            SignDao signDao = new SignDao(context);
            if (!signDao.isSignedToday()) {
                signDao.add();
                EventBus.getDefault().post(new EventBusEvent.ShareEvent(EventBusEvent.ShareEvent.TYPE_SIGN_OK));
            }
        }
    }
}
