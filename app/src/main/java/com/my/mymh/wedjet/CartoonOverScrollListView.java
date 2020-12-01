package com.my.mymh.wedjet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;


/**
 * 处理漫画加载上一章和下一章节的情况
 * Created by Dave on 2015/11/12.
 */
@Deprecated
public class CartoonOverScrollListView extends ListView {
    public static final String TAG = "OverScrollListView";
    public static final int OVER_SCROLL_HEIGHT = UIUtil.dip2px(2);
    private boolean triggerFresh = false;
    private boolean triggerLoad = false;
    private OnTriggerListener onTriggerListener;

    public CartoonOverScrollListView(Context context) {
        this(context, null);
    }

    public CartoonOverScrollListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartoonOverScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (isTouchEvent && deltaY < 0) { // 表示下拉了
            MyDebugUtil.log(TAG, "下拉了,maxOverScrollX=" + maxOverScrollX + ",scrollY=" + scrollY);
            if (scrollY == -OVER_SCROLL_HEIGHT) {//到顶了
                if (!triggerFresh) {
                    triggerFresh = true;
                }
            }
        } else if (isTouchEvent && deltaY > 0) {// 表示上拉了
            MyDebugUtil.log(TAG, "上拉了,maxOverScrollX=" + maxOverScrollX + ",scrollY=" + scrollY);
            if (scrollY == OVER_SCROLL_HEIGHT) {//到底了
                if (!triggerLoad) {
                    triggerLoad = true;
                }
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, OVER_SCROLL_HEIGHT, isTouchEvent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (null != onTriggerListener) {
                    if (triggerFresh) {
                        triggerFresh = false;
                        onTriggerListener.triggerFresh();
                    } else if (triggerLoad) {
                        MyDebugUtil.log("timeeee", "start="+System.currentTimeMillis()/1000);
                        triggerLoad = false;
                        onTriggerListener.triggerLoad();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnTriggerListener(OnTriggerListener onTriggerListener) {
        this.onTriggerListener = onTriggerListener;
    }

    public interface OnTriggerListener {
        void triggerFresh();

        void triggerLoad();
    }
}
