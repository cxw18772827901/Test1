package com.my.mymh.wedjet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.ui.RankFragment;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.wedjet
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/17.
 */
public class RankView extends FrameLayout implements View.OnClickListener {

    private TextView tv_tab1;
    private TextView tv_tab2;
    private TextView tv_tab3;
    private FrameLayout fl_indicator;
    private ViewPager view_pager;
    private List<BaseFragment> fragmentList;

    public RankView(@NonNull Context context) {
        this(context, null);
    }

    public RankView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.rank_view, this);
        tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
        tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
        tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
        fl_indicator = (FrameLayout) findViewById(R.id.fl_indicator);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void initView(FragmentManager fragmentManager) {
        //周榜
        RankFragment rankFragment1 = new RankFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("tag", 5);
        rankFragment1.setArguments(bundle1);
        //月榜
        RankFragment rankFragment2 = new RankFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("tag", 6);
        rankFragment2.setArguments(bundle2);
        //总榜
        RankFragment rankFragment3 = new RankFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("tag", 2);
        rankFragment3.setArguments(bundle3);
        //add
        fragmentList = new ArrayList<>();
        fragmentList.add(rankFragment1);
        fragmentList.add(rankFragment2);
        fragmentList.add(rankFragment3);
        //set data
        view_pager.setOffscreenPageLimit(2);
        view_pager.setAdapter(new MyAdapter(fragmentManager));
        //listener
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((FrameLayout.LayoutParams) fl_indicator.getLayoutParams()).leftMargin = (int) (UIUtil.dip2px(90) * positionOffset) + position * UIUtil.dip2px(90);
                fl_indicator.requestLayout();
                tv_tab1.setTextSize(UIUtil.dip2px(position == 0 ? 6 : 5));
                tv_tab2.setTextSize(UIUtil.dip2px(position == 1 ? 6 : 5));
                tv_tab3.setTextSize(UIUtil.dip2px(position == 2 ? 6 : 5));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tv_tab1.setOnClickListener(this);
        tv_tab2.setOnClickListener(this);
        tv_tab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int pos = -1;
        switch (v.getId()) {
            case R.id.tv_tab1:
                break;
            case R.id.tv_tab2:
                pos = 1;
                break;
            case R.id.tv_tab3:
                pos = 2;
                break;
        }
        if (view_pager.getCurrentItem() != pos) {
            view_pager.setCurrentItem(pos, false);
        }
    }

    private class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
}
