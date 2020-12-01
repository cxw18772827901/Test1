package com.my.mymh.wedjet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.my.mymh.R;
import com.my.mymh.util.UIUtil;

/**
 * PackageName  com.dave.project.wedjet
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2017/12/22.
 */

public class ProgressView extends View {

    private int firstColor;
    private int secondColor;
    private int borderWidth;
    private int speed;
    private int currentPosition = 0;
    private boolean shouldChange = false;
    private Paint mPaint;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, 0);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        firstColor = typedArray.getColor(R.styleable.ProgressView_firstColor, getResources().getColor(R.color.colorPrimaryDark));
        secondColor = typedArray.getColor(R.styleable.ProgressView_secondColor, getResources().getColor(R.color.colorAccent));
        borderWidth = (int) typedArray.getDimension(R.styleable.ProgressView_borderWidth, UIUtil.dip2px(100));
        speed = typedArray.getColor(R.styleable.ProgressView_speed, 10);
        typedArray.recycle();
        //paint
        mPaint = new Paint();
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //绘制
        post();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = centre - borderWidth / 2;
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        if (!shouldChange) {
            mPaint.setColor(firstColor);
            canvas.drawCircle(centre, centre, radius, mPaint);
            mPaint.setColor(secondColor);
            canvas.drawArc(oval, -90, currentPosition, false, mPaint);
        } else {
            mPaint.setColor(secondColor);
            canvas.drawCircle(centre, centre, radius, mPaint);
            mPaint.setColor(firstColor);
            canvas.drawArc(oval, -90, currentPosition, false, mPaint);
        }
        post();
    }

    private void post() {
        currentPosition += 5;
        if (currentPosition >= 360) {
            shouldChange = !shouldChange;
            currentPosition = 0;
        }
        postInvalidateDelayed(speed);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.EXACTLY != widthMode || MeasureSpec.EXACTLY != heightMode) {
            if (MeasureSpec.EXACTLY != widthMode) {
                width = UIUtil.dip2px(100);
            }
            if (MeasureSpec.EXACTLY != heightMode) {
                height = UIUtil.dip2px(100);
            }
            setMeasuredDimension(width, height);
        }
    }
}
