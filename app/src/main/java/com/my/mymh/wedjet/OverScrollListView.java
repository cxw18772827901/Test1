package com.my.mymh.wedjet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.my.mymh.util.UIUtil;

/**
 * PackageName  com.hgd.hgdcomic.wedjet
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/4/12.
 */
public class OverScrollListView extends ListView {

    private int overScrollY;

    public OverScrollListView(Context context) {
        this(context, null);
    }

    public OverScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        overScrollY = UIUtil.dip2px(50);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, overScrollY, isTouchEvent);
    }
}
