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
import com.my.mymh.ui.mainFragment.CollectFragment;
import com.my.mymh.ui.mainFragment.LocalRackFragment;

/**
 * 类QQ顶部按钮导航栏
 * Created by Dave on 2017/1/18.
 */
public class FoundTopHost extends LinearLayout {
    //tag
    public static final String TAG = "tag";
    public static final String TAG_11 = "tag_11";
    public static final String TAG_12 = "tag_12";
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

    public FoundTopHost(Context context) {
        this(context, null);
    }

    public FoundTopHost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoundTopHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.found_top_container, this);
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
        fragment1 = fragmentManager.findFragmentByTag(TAG_11);
        if (fragment1 == null) {
            fragment1 = new CollectFragment();
        }
        fragment2 = fragmentManager.findFragmentByTag(TAG_12);
        if (fragment2 == null) {
            fragment2 = new LocalRackFragment();
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
                fragmentManager.beginTransaction().add(containerViewId, currentFragment, TAG_11).commit();
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
                        fragmentManager.beginTransaction().hide(currentFragment).add(containerViewId, fragment1, TAG_11).commit();
                    }
                    currentTab = 0;
                    currentFragment = fragment1;
                    break;
                default:
                    if (fragment2.isAdded()) {
                        fragmentManager.beginTransaction().hide(currentFragment).show(fragment2).commit();
                    } else {
                        fragmentManager.beginTransaction().hide(currentFragment).add(containerViewId, fragment2, TAG_12).commit();
                    }
                    currentTab = 1;
                    currentFragment = fragment2;
                    break;
            }
            if (onTabChangedListener!=null){
                onTabChangedListener.tabChange(currentTab);
            }
        });
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
