package com.my.mymh.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInjectUtil;


/**
 * 主界面fragment基类
 * Created by Dave on 2017/1/11.
 */
public class BaseFragment extends Fragment {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    //处理长时间挂后台因为内存回收导致展示错乱的问题
    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (null != bundle) {
            boolean isSupportHidden = bundle.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewInjectUtil.initNotInActivity(this, view);//注解
    }

    //tab切换后fragment里面的回调
    public void tabClick() {
        MyDebugUtil.logTest("tabClick", "tab4");
    }

}
