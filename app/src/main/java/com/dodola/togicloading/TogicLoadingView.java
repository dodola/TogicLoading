package com.dodola.togicloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 * Created by dodola on 15/6/30.
 */
public class TogicLoadingView extends View {

    private float mInterpolatedTime;
    private TogicLoadingAnimation wa;
    private Paint paint, paint2;
    private final int CIRCLE_RADIUS = 20;

    public TogicLoadingView(Context context) {
        super(context);
        initPaint();
    }

    public TogicLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public TogicLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private class TogicLoadingAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
            if (mInterpolatedTime == 1) {
                sign = !sign;
            }
        }
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(Color.WHITE); // 设置画笔颜色
        paint.setStyle(Paint.Style.FILL);


        paint2 = new Paint();
        paint2.setAntiAlias(true); // 设置画笔为抗锯齿
        paint2.setColor(Color.RED); // 设置画笔颜色
        paint2.setStyle(Paint.Style.STROKE);
    }

    private void startAnimation() {
        wa = new TogicLoadingAnimation();
        wa.setDuration(500);
        wa.setInterpolator(new DecelerateInterpolator());
        wa.setRepeatCount(Animation.INFINITE);
        wa.setRepeatMode(Animation.RESTART);
        startAnimation(wa);
    }

    private void stopAnimation() {
        this.clearAnimation();
        postInvalidate();
    }

    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);

            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    private boolean sign = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sign) {
            canvas.save();
            canvas.rotate(180 * mInterpolatedTime, 60, getHeight() / 2);
            canvas.drawCircle(20, getHeight() / 2, CIRCLE_RADIUS, paint);
            canvas.drawCircle(CIRCLE_RADIUS + 1 * 80, getHeight() / 2, CIRCLE_RADIUS, paint);
            canvas.restore();
            canvas.drawCircle(CIRCLE_RADIUS + 160, getHeight() / 2, CIRCLE_RADIUS, paint);

        } else {
            canvas.save();
            canvas.drawCircle(20, getHeight() / 2, CIRCLE_RADIUS, paint);
            canvas.rotate(180 * mInterpolatedTime, 140, getHeight() / 2);
            canvas.drawCircle(CIRCLE_RADIUS + 80, getHeight() / 2, CIRCLE_RADIUS, paint);
            canvas.drawCircle(CIRCLE_RADIUS + 160, getHeight() / 2, CIRCLE_RADIUS, paint);
            canvas.restore();
        }
//        canvas.drawCircle(60, getHeight() / 2, CIRCLE_RADIUS * 2, paint2);
//        canvas.drawCircle(140, getHeight() / 2, CIRCLE_RADIUS * 2, paint2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(CIRCLE_RADIUS * 10, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(CIRCLE_RADIUS * 6, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }
}
