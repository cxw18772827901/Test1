package com.my.mymh.wedjet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.my.mymh.R;
import com.my.mymh.ui.retroAction.AppSuggestionsFragment;
import com.my.mymh.ui.retroAction.CartoonsComplementedFragment;

/**
 * 类QQ顶部按钮导航栏
 * Created by Dave on 2017/1/18.
 */
public class RetroActionHost extends LinearLayout {
    //tag
    public static final String TAG = "tag";
    public static final String TAG_21 = "tag_21";
    public static final String TAG_22 = "tag_22";
    public static final String TAB = "tab";
    //data
    private Activity activity;
    private FragmentManager fragmentManager;
    private int containerViewId;
    //current
    private Fragment currentFragment;
    private Fragment fragment1;
    private Fragment fragment2;
    private int currentTab = 0;
    //View
    private RadioGroup rg;

    public RetroActionHost(Context context) {
        this(context, null);
    }

    public RetroActionHost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RetroActionHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.msg_tread_container, this);
        rg = (RadioGroup) findViewById(R.id.rg);
    }

    public void init(Activity activity, FragmentManager fragmentManager, int containerViewId, Bundle bundle) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.containerViewId = containerViewId;
        setData();
        firstShow(bundle);
        changeListener();
    }

    private void setData() {
        fragment1 = fragmentManager.findFragmentByTag(TAG_21);
        if (fragment1 == null) {
            fragment1 = new AppSuggestionsFragment();
            //标记
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG_21);
            fragment1.setArguments(bundle);
        }
        fragment2 = fragmentManager.findFragmentByTag(TAG_22);
        if (fragment2 == null) {
            fragment2 = new CartoonsComplementedFragment();
            //标记
            Bundle bundle = new Bundle();
            bundle.putString(TAG, TAG_22);
            fragment2.setArguments(bundle);
        }
    }

    private void firstShow(Bundle bundle) {
        if (bundle != null) {
            int lastTab = bundle.getInt(TAB);
            if (lastTab >= 0 && lastTab < 2) {
                //currentData
                currentTab = lastTab;
                currentFragment = lastTab == 0 ? fragment1 : fragment2;
                //check
                ((RadioButton) rg.getChildAt(lastTab)).setChecked(true);
            }
        } else {
            //currentData
            currentTab = 0;
            currentFragment = fragment1;
            if (!currentFragment.isAdded()) {
                fragmentManager.beginTransaction().add(containerViewId, currentFragment, TAG_21).commit();
            } else {
                fragmentManager.beginTransaction().show(currentFragment).commit();
            }
            //check
            ((RadioButton) rg.getChildAt(0)).setChecked(true);
        }
    }

    private void changeListener() {
        rg.setOnCheckedChangeListener((radioGroup, id) -> {
            switch (id) {
                case R.id.rb_received:
                    if (fragment1.isAdded()) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(fragment1).commit();
                    } else {
                        fragmentManager.beginTransaction().hide(currentFragment).add(containerViewId, fragment1, TAG_21).commit();
                    }
                    currentTab = 0;
                    currentFragment = fragment1;
                    break;
                default:
                    notifyData();
                    if (fragment2.isAdded()) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(fragment2).commit();
                    } else {
                        fragmentManager.beginTransaction().hide(currentFragment).add(containerViewId, fragment2, TAG_22).commit();
                    }
                    currentTab = 1;
                    currentFragment = fragment2;
                    break;
            }
        });
    }

    //清楚角标
    private void notifyData() {
//        BaseApplication.getMsgDao().updateCount(2, 0);
//        EventBus.getDefault().post(new EventBusEvent.MultiEvent(EventBusEvent.MultiEvent.TYPE_NOTIFY_MSG));
    }

    //获取当前tab栏
    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int position) {
        ((RadioButton) rg.getChildAt(position)).setChecked(true);
    }

    //tab切换后在MainActivity里面的回调,参数为下标
    private OnTabChangedListener onTabChangedListener;

    public void setOnTabChangedListener(OnTabChangedListener onTabChangedListener) {
        this.onTabChangedListener = onTabChangedListener;
    }

    public interface OnTabChangedListener {
        void tabChange(int tabIndex);
    }
}
