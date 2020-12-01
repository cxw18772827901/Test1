package com.my.mymh.wedjet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ListView;

import com.my.mymh.R;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;


/**
 * 缩放listview
 * Created by Tongming on 2016/8/18.
 */
@Deprecated
public class ZoomNormalListView extends ListView {
    //<editor-fold desc="缩放">
    /**
     * 非法的手指ID
     */
    private static final int INVALID_POINTER_ID = -1;

    private Context mContext;

    /**
     * 第一根按下的手指的ID,进行拖动事件处理
     */
    private int mMainPointerId = INVALID_POINTER_ID;

    /**
     * 缩放因子
     */
    private float mScaleFactor;
    private float mLastScaleFactor;
    /**
     * 上次触摸点坐标
     */
    private float mLastTouchX;
    private float mLastTouchY;
    /**
     * canvas的偏移量
     */
    private float mDeltaX;
    private float mDeltaY;
    /**
     * 缩放中心
     */
    private float centerX;
    private float centerY;
    /**
     * 缩放因子
     */
    private float mInitScaleFactor = 1.0f;
    private float mMidScaleFactor = (float) (mInitScaleFactor * 1.5);
    private float mMaxScaleFactor = mInitScaleFactor * 2;
    /**
     * 双击自动缩放
     */
    private boolean isAutoScale;
    private int mAutoTime = 5;
    private float mAutoBigger = 1.07f;
    private float mAutoSmall = 0.93f;


    /**
     * 单击、双击手势
     */
    private GestureDetector mGestureDetector;
    /**
     * 缩放手势
     */
    private ScaleGestureDetector mScaleGestureDetector;
    /**
     * 开放监听接口
     */
    private OnGestureListener mOnGestureListener;
    private TimeCount timeCount;

    public interface OnGestureListener {
        boolean onScale(ScaleGestureDetector detector);

        boolean onSingleTapConfirmed(MotionEvent e);

        boolean onDoubleTap(MotionEvent e);
    }

    /**
     * 自动缩放的核心类
     */
    private class AutoScaleRunnable implements Runnable {
        /**
         * 目标Scale
         */
        private float mTargetScale;
        /**
         * Scale变化梯度
         */
        private float mGrad;
        /**
         * 缩放中心
         */
        private float x, y;

        private AutoScaleRunnable(float TargetScale, float x, float y, float grad) {
            mTargetScale = TargetScale;
            mGrad = grad;
        }

        @Override
        public void run() {
            if ((mGrad > 1.0f && mScaleFactor < mTargetScale)
                    || (mGrad < 1.0f && mScaleFactor > mTargetScale)) {
                mScaleFactor *= mGrad;
                postDelayed(this, mAutoTime);
            } else {
                mScaleFactor = mTargetScale;
            }
            /** 检查边界 */
            checkBorder();
            invalidate();
        }
    }

    /**
     * 数据变化监听
     */
//    private AdapterDataObserver observer;
    /**
     * 数据为空时显示
     */
    private View emptyView;

    public ZoomNormalListView(Context context) {
        this(context, null);
    }

    public ZoomNormalListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomNormalListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        timeCount = new TimeCount(3000, 1000);
        mContext = context;
        obtainStyledAttributes(attrs);
        initView();
        initDetector();
    }

    /**
     * 从XML文件获取属性
     */
    private void obtainStyledAttributes(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ZoomRecyclerView);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.ZoomRecyclerView_minScaleFactor) {
                mInitScaleFactor = ta.getFloat(attr, 1.0f);
            } else if (attr == R.styleable.ZoomRecyclerView_maxScaleFactor) {
                mMaxScaleFactor = ta.getFloat(attr, mInitScaleFactor * 2);
            } else if (attr == R.styleable.ZoomRecyclerView_autoScaleTime) {
                mAutoTime = ta.getInt(attr, 5);
            }
        }
        ta.recycle();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mMidScaleFactor = (mInitScaleFactor + mMaxScaleFactor) / 2;
        mScaleFactor = mInitScaleFactor;
        isAutoScale = false;

//        observer = new AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//                adapterIsEmpty();
//            }
//        };
    }

    /**
     * 初始化手势监听
     */
    private void initDetector() {
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                /** 获取缩放中心 */
                centerX = detector.getFocusX();
                centerY = detector.getFocusY();

                /** 缩放 */
                mLastScaleFactor = mScaleFactor;
                mScaleFactor *= detector.getScaleFactor();
                mScaleFactor = Math.max(mInitScaleFactor, Math.min(mScaleFactor, mMaxScaleFactor));

                /** 缩放导致偏移 */
//                mDeltaX += centerX * (mScaleFactor - mLastScaleFactor);
//                mDeltaY += centerY * (mScaleFactor - mLastScaleFactor);
//                checkBorder();//检查边界
                ZoomNormalListView.this.invalidate();

                if (mOnGestureListener != null) {
                    mOnGestureListener.onScale(detector);
                }
                return true;
            }
        });

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return mOnGestureListener != null && mOnGestureListener.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale) {
                    return true;
                }
                centerX = e.getX();
                centerY = e.getY();
//                centerX = 0;
//                centerY = 0;
                if (mScaleFactor < mMidScaleFactor) {
                    postDelayed(new AutoScaleRunnable(mMidScaleFactor, centerX, centerY, mAutoBigger), mAutoTime);
                } /*else if (mScaleFactor < mMaxScaleFactor) {
                    postDelayed(new AutoScaleRunnable(mMaxScaleFactor, centerX, centerY, mAutoBigger), mAutoTime);
                } */ else {
                    postDelayed(new AutoScaleRunnable(mInitScaleFactor, centerX, centerY, mAutoSmall), mAutoTime);
                }

                if (mOnGestureListener != null) {
                    mOnGestureListener.onDoubleTap(e);
                }
                return true;
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        if (mScaleFactor == 1.0f) {
            mDeltaX = 0.0f;
            mDeltaY = 0.0f;
        }
        canvas.translate(mDeltaX, mDeltaY);
        canvas.scale(mScaleFactor, mScaleFactor, centerX, centerY);
//        canvas.scale(mScaleFactor, mScaleFactor);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        /** 单击、双击事件的处理 */
        if (mGestureDetector.onTouchEvent(event)) {
            mMainPointerId = event.getPointerId(0);//防止发生手势事件后,mActivePointerId=-1的情况
            return true;
        }
        /** 缩放事件的处理 */
        mScaleGestureDetector.onTouchEvent(event);

        /** 拖动事件的处理 */
        /** 只获得事件类型值，不获得point的index值 */
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                mMainPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                int mainPointIndex = event.findPointerIndex(mMainPointerId);
                float mainPointX = event.getX(mainPointIndex);
                float mainPointY = event.getY(mainPointIndex);

                /** 计算与上次坐标的偏移量并累加 */
                mDeltaX += (mainPointX - mLastTouchX);
                mDeltaY += (mainPointY - mLastTouchY);

                /** 保存坐标 */
                mLastTouchX = mainPointX;
                mLastTouchY = mainPointY;

                /** 检查边界 */
                checkBorder();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mMainPointerId = INVALID_POINTER_ID;
                listen();
                break;
            case MotionEvent.ACTION_CANCEL:
                mMainPointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                /** 获取抬起手指 */
                int pointerIndex = event.getActionIndex();
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mMainPointerId) {
                    /** 抬起手指是主手指,则寻找另一根手指*/
                    int newPointerIndex = (pointerIndex == 0 ? 1 : 0);
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mMainPointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

//    @Override
//    public void setAdapter(ListAdapter adapter) {
////        ListAdapter oldAdapter = getAdapter();
////        if (oldAdapter != null && observer != null) {
////            oldAdapter.unregisterAdapterDataObserver(observer);
////        }
////        if (adapter != null && observer != null) {
////            adapter.registerAdapterDataObserver(observer);
////        }
//        super.setAdapter(adapter);
////        adapterIsEmpty();
//    }

    /**
     * 判断数据为空,显示emptyView
     */
//    private void adapterIsEmpty() {
//        if (emptyView != null) {
//            emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
//        }
//    }

    /**
     * 检查边界
     */
    private void checkBorder() {
//        int Pos[] = {-1, -1};  //保存当前坐标的数组
//        this.getLocationOnScreen(Pos);  //获取选中的 Item 在屏幕中的位置，以左上角为原点 (0, 0)
//        int oldListX = Pos[0];
//        int oldListY = Pos[1];
//        MyDebugUtil.log("checkBorder", "mDeltaX=" + mDeltaX + ",width=" + getWidth() + ",scale=" + mScaleFactor + ",getLeft" + getRight());
//        MyDebugUtil.log("checkBorder", "oldListX=" + oldListX + ",oldListY=" + oldListY);
//        MyDebugUtil.log("checkBorder", "mDeltaY=" + mDeltaY);
        MyDebugUtil.log("checkBorder", "mDeltaY=" + mDeltaY + ",mScaleFactor=" + mScaleFactor);
        //左右
        float twoSideBorder = 500.0f * (mScaleFactor - 1.0f);
        if (mDeltaX > twoSideBorder) {
            mDeltaX = twoSideBorder;
        }
        if (mDeltaX < -twoSideBorder) {
            mDeltaX = -twoSideBorder;
        }
        //上下
        float upAndDown = 850 * (mScaleFactor - 1.0f);
        if (mDeltaY > upAndDown) {
            mDeltaY = upAndDown;
        }
        //下面
        if (mDeltaY < -upAndDown) {
            mDeltaY = -upAndDown;
        }
    }

//
//    /**
//     * setter and getter
//     */
//    public View getEmptyView() {
//        return emptyView;
//    }
//
//    public void setEmptyView(View emptyView) {
//        this.emptyView = emptyView;
//    }
//
//    public float getAutoBigger() {
//        return mAutoBigger;
//    }
//
//    public void setAutoBigger(float autoBigger) {
//        this.mAutoBigger = autoBigger;
//    }
//
//    public float getAutoSmall() {
//        return mAutoSmall;
//    }
//
//    public void setAutoSmall(float autoSmall) {
//        this.mAutoSmall = autoSmall;
//    }
//
//    public int getAutoTime() {
//        return mAutoTime;
//    }
//
//    public void setAutoTime(int autoTime) {
//        this.mAutoTime = autoTime;
//    }
//
//    public float getInitScaleFactor() {
//        return mInitScaleFactor;
//    }
//
//    public void setInitScaleFactor(float initScaleFactor) {
//        this.mInitScaleFactor = initScaleFactor;
//    }
//
//    public float getMaxScaleFactor() {
//        return mMaxScaleFactor;
//    }
//
//    public void setMaxScaleFactor(float maxScaleFactor) {
//        this.mMaxScaleFactor = maxScaleFactor;
//    }
//
//    public float getMidScaleFactor() {
//        return mMidScaleFactor;
//    }
//
//    public void setMidScaleFactor(float midScaleFactor) {
//        this.mMidScaleFactor = midScaleFactor;
//    }
//
//    public OnGestureListener getOnGestureListener() {
//        return mOnGestureListener;
//    }
//
//    public void setOnGestureListener(OnGestureListener onGestureListener) {
//        this.mOnGestureListener = onGestureListener;
//    }
//
//    public float getScaleFactor() {
//        return mScaleFactor;
//    }
//
//    public void setScaleFactor(float scaleFactor) {
//        this.mScaleFactor = scaleFactor;
//    }

    //</editor-fold>

    //<editor-fold desc="加载监听">
    public static final String TAG = "OverScrollListView";
    public static final int OVER_SCROLL_HEIGHT = UIUtil.dip2px(2);
    private boolean triggerFresh = false;
    private boolean triggerLoad = false;
    private CartoonOverScrollListView.OnTriggerListener onTriggerListener;

    public void setOnTriggerListener(CartoonOverScrollListView.OnTriggerListener onTriggerListener) {
        this.onTriggerListener = onTriggerListener;
    }

    public interface OnTriggerListener {
        void triggerFresh();

        void triggerLoad();
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

    private void listen() {
        if (null != onTriggerListener && isIdel) {
            if (triggerFresh) {
                triggerFresh = false;
                onTriggerListener.triggerFresh();
                timeCount.start();
                isIdel = false;
            } else if (triggerLoad) {
                triggerLoad = false;
                onTriggerListener.triggerLoad();
                timeCount.start();
                isIdel = false;
            }
        } else {
            MyDebugUtil.log("isIdel", "无响应");
        }
    }

    boolean isIdel = true;

    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            MyDebugUtil.log("isIdel", "isIdel=" + isIdel);
        }

        @Override
        public void onFinish() {
            MyDebugUtil.log("isIdel", "空闲");
            isIdel = true;
        }
    }

    //</editor-fold>
}
