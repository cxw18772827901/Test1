package com.my.mymh.wedjet;

import android.widget.AbsListView;

/**
 * PackageName  com.dave.project.wedjet
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/17.
 */

public class MyScrollListener implements AbsListView.OnScrollListener {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        scroll(scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    protected void scroll(int scrollState) {

    }
}
