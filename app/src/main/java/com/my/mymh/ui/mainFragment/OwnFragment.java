package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.QQUserBean;
import com.my.mymh.model.WxInfoBean;
import com.my.mymh.ui.CaresActivity;
import com.my.mymh.ui.LoginActivity;
import com.my.mymh.ui.MainActivity;
import com.my.mymh.ui.QRActivity;
import com.my.mymh.ui.RetroActionActivity;
import com.my.mymh.ui.SettingActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.ClipboardUtil;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.SPUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;

/**
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 * <p>
 * 设置
 */

public class OwnFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "FoundFragment";
    @ViewInject(id = R.id.iv_left)
    private ImageView iv_left;
    @ViewInject(id = R.id.tv_title_title)
    private TextView tv_title_title;
    @ViewInject(id = R.id.iv_right)
    private ImageView iv_right;
    @ViewInject(id = R.id.ll_head, needClick = true)
    private LinearLayout ll_head;
    @ViewInject(id = R.id.tv_name)
    private TextView tv_name;
    @ViewInject(id = R.id.iv_head)
    private RoundImageView iv_head;
    @ViewInject(id = R.id.tv_care, needClick = true)
    private TextView tv_care;
    @ViewInject(id = R.id.tv_setting, needClick = true)
    private TextView tv_setting;
    @ViewInject(id = R.id.tv_report, needClick = true)
    private TextView tv_report;
    @ViewInject(id = R.id.tv_update, needClick = true)
    private TextView tv_update;
    @ViewInject(id = R.id.tv_share, needClick = true)
    private TextView tv_share;
    @ViewInject(id = R.id.tv_qr_code, needClick = true)
    private TextView tv_qr_code;
    @ViewInject(id = R.id.tv_listen, needClick = true)
    private TextView tv_listen;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.own_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }

    public void init() {
        EventBus.getDefault().register(this);
    }

    public void initView() {
        tv_listen.setSelected(true);
        iv_left.setVisibility(View.GONE);
        iv_right.setVisibility(View.GONE);
        tv_title_title.setText("我的");
        //user_info
        if (LoginVerifyUtil.isLogin()) {
            showUserInfo();
        } else {
            //占位图
            showHolder();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showHolder() {
        tv_name.setText("游客");
        iv_head.setImageResource(R.drawable.ic_defult_header);
    }

    private void showUserInfo() {
        switch (SPUtil.getString(Constants.USER_ACCOUNT_TYPE)) {
            case Constants.USER_ACCOUNT_QQ: {
                String info = SPUtil.getString(Constants.USER_ACCOUNT_INFO);
                QQUserBean qqUserBean = GsonUtil.getGson().fromJson(info, QQUserBean.class);
                if (null != qqUserBean) {
                    showQQInfo(qqUserBean);
                }
                break;
            }
            case Constants.USER_ACCOUNT_WX: {
                String info = SPUtil.getString(Constants.USER_ACCOUNT_INFO);
                WxInfoBean wxInfoBean = GsonUtil.getGson().fromJson(info, WxInfoBean.class);
                if (null != wxInfoBean) {
                    showWXInfo(wxInfoBean);
                }
                break;
            }
            default:
                showHolder();
                break;
        }
    }

    private void showQQInfo(QQUserBean qqUserBean) {
        tv_name.setText(qqUserBean.nickname);
        tv_name.setSelected("女".equals(qqUserBean.gender));
        GlideUtil.load(getActivity(), qqUserBean.figureurl_qq_2, R.drawable.ic_defult_header, iv_head);
    }

    private void showWXInfo(WxInfoBean userInfo) {
        try {
            String nameChange = new String(userInfo.nickname.getBytes("ISO-8859-1"), "UTF-8");
            tv_name.setText(nameChange);
            tv_name.setSelected(2 == userInfo.sex);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        GlideUtil.load(getActivity(), userInfo.headimgurl, R.drawable.ic_defult_header, iv_head);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_head:
                if (!LoginVerifyUtil.isLogin()) {
                    IntentUtil.startActivity(getActivity(), LoginActivity.class);
                }
                break;
            case R.id.tv_care:
                if (!LoginVerifyUtil.isLogin()) {
                    IntentUtil.startActivity(getActivity(), LoginActivity.class);
                } else {
                    IntentUtil.startActivity(getActivity(), CaresActivity.class);
                }
                break;
            case R.id.tv_setting:
                IntentUtil.startActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.tv_update:
                ((MainActivity) getActivity()).checkUpDate(1);
                break;
            case R.id.tv_share:
                ((MainActivity) getActivity()).share();
                break;
            case R.id.tv_qr_code:
                IntentUtil.startActivity(getActivity(), QRActivity.class);
                break;
            case R.id.tv_report:
                if (!LoginVerifyUtil.isLogin()) {
                    IntentUtil.startActivity(getActivity(), LoginActivity.class);
                } else {
                    IntentUtil.startActivity(getActivity(), RetroActionActivity.class);
                }
                break;
            case R.id.tv_listen:
                ClipboardUtil.copy("723879293");
                MyDebugUtil.toast("复制成功~");
                break;
        }
    }

    /*注意:eventbus只能有一个接收的方法*/
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventLoginOrOut(EventBusEvent.LoginAndOutEvent loginAndOutEvent) {
        if (EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_QQ == loginAndOutEvent.loginEventType) {
            showQQInfo((QQUserBean) loginAndOutEvent.userInfo);
        } else if (EventBusEvent.LoginAndOutEvent.TYPE_LOGIN_WX == loginAndOutEvent.loginEventType) {
            showWXInfo((WxInfoBean) loginAndOutEvent.userInfo);
        } else if (EventBusEvent.LoginAndOutEvent.TYPE_OUT == loginAndOutEvent.loginEventType) {
            showHolder();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
