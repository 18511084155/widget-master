package com.woodys.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 定制优惠券view
 */

public class CouponView extends LinearLayout {

    private static final int DEFAULT_SEMICIRCLE_GAP = 4;
    private static final int DEFAULT_DASH_LINE_LENGTH = 10;
    private static final int DEFAULT_SEMICIRCLE_RADIUS = 4;
    private static final int DEFAULT_SEMICIRCLE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DASH_LINE_HEIGHT = 1;
    private static final int DEFAULT_DASH_LINE_GAP = 5;
    private static final int DEFAULT_DASH_LINE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DASH_LINE_MARGIN = 10;

    private Context context;

    //半圆画笔
    private Paint semicirclePaint;

    //虚线画笔
    private Paint dashLinePaint;

    //view宽度
    private int viewWidth;

    //view的高度
    private int viewHeight;

    //半圆之间间距
    private float semicircleGap = DEFAULT_SEMICIRCLE_GAP;

    //半圆半径
    private float semicircleRadius = DEFAULT_SEMICIRCLE_RADIUS;

    //半圆颜色
    private int semicircleColor = DEFAULT_SEMICIRCLE_COLOR;

    //半圆数量X
    private int semicircleNumX;

    //半圆数量Y
    private int semicircleNumY;

    //绘制半圆曲线后X轴剩余距离
    private int remindSemicircleX;

    //绘制半圆曲线后Y轴剩余距离
    private int remindSemicircleY;

    //虚线的长度
    private float dashLineLength = DEFAULT_DASH_LINE_LENGTH;

    //虚线的高度
    private float dashLineHeight = DEFAULT_DASH_LINE_HEIGHT;

    //虚线的间距
    private float dashLineGap = DEFAULT_DASH_LINE_GAP;

    //虚线的颜色
    private int dashLineColor = DEFAULT_DASH_LINE_COLOR;

    //顶部虚线距离View顶部的距离
    private float dashLineMarginTop = DEFAULT_DASH_LINE_MARGIN;

    //底部虚线距离View底部的距离
    private float dashLineMarginBottom = DEFAULT_DASH_LINE_MARGIN;

    //左侧虚线距离View左侧的距离
    private float dashLineMarginLeft = DEFAULT_DASH_LINE_MARGIN;

    //右侧虚线距离View右侧的距离
    private float dashLineMarginRight = DEFAULT_DASH_LINE_MARGIN;

    //绘制虚线后X轴剩余距离
    private int remindDashLineX;

    //绘制虚线后Y轴剩余距离
    private int remindDashLineY;

    //虚线数量X
    private int dashLineNumX;

    //半圆数量Y
    private int dashLineNumY;

    public CouponView(Context context) {
        this(context, null);
    }

    public CouponView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setWillNotDraw(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CouponView, defStyle, 0);
        semicircleRadius = a.getDimensionPixelSize(R.styleable.CouponView_cv_semicircle_radius, dp2Px(DEFAULT_SEMICIRCLE_RADIUS));
        semicircleGap = a.getDimensionPixelSize(R.styleable.CouponView_cv_semicircle_gap, dp2Px(DEFAULT_SEMICIRCLE_GAP));
        semicircleColor = a.getColor(R.styleable.CouponView_cv_semicircle_color, DEFAULT_SEMICIRCLE_COLOR);

        dashLineGap = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_gap, dp2Px(DEFAULT_DASH_LINE_GAP));
        dashLineHeight = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_height, dp2Px(DEFAULT_DASH_LINE_HEIGHT));
        dashLineLength = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_length, dp2Px(DEFAULT_DASH_LINE_LENGTH));
        dashLineColor = a.getColor(R.styleable.CouponView_cv_dash_line_color, DEFAULT_DASH_LINE_COLOR);

        dashLineMarginTop = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_margin_top, dp2Px(DEFAULT_DASH_LINE_MARGIN));
        dashLineMarginBottom = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_margin_bottom, dp2Px(DEFAULT_DASH_LINE_MARGIN));
        dashLineMarginLeft = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_margin_left, dp2Px(DEFAULT_DASH_LINE_MARGIN));
        dashLineMarginRight = a.getDimensionPixelSize(R.styleable.CouponView_cv_dash_line_margin_right, dp2Px(DEFAULT_DASH_LINE_MARGIN));
        a.recycle();
        init();

    }

    private void init() {
        semicirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        semicirclePaint.setDither(true);
        semicirclePaint.setColor(semicircleColor);
        semicirclePaint.setStyle(Paint.Style.FILL);

        dashLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashLinePaint.setDither(true);
        dashLinePaint.setColor(dashLineColor);
        dashLinePaint.setStyle(Paint.Style.FILL);
    }

    private int dp2Px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private int px2Dp(float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        calculate();
    }

    private void calculate() {

        int orientation = getOrientation();

        remindSemicircleX = (int) ((viewHeight - semicircleGap) % (2 * semicircleRadius + semicircleGap));
        semicircleNumX = (int) ((viewWidth - semicircleGap) / (2 * semicircleRadius + semicircleGap));

        if (HORIZONTAL == orientation) {
            remindDashLineY = (int) ((viewHeight + dashLineGap - dashLineMarginTop - dashLineMarginBottom) % (dashLineLength + dashLineGap));
            dashLineNumY = (int) ((viewHeight + dashLineGap - dashLineMarginTop - dashLineMarginBottom) / (dashLineLength + dashLineGap));
        }else {
            remindDashLineX = (int) ((viewWidth + dashLineGap - dashLineMarginLeft - dashLineMarginRight) % (dashLineLength + dashLineGap));
            dashLineNumX = (int) ((viewWidth + dashLineGap - dashLineMarginLeft - dashLineMarginRight) / (dashLineLength + dashLineGap));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int orientation = getOrientation();
        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View childView = getChildAt(i);
            if (View.VISIBLE == childView.getVisibility()) {
                if (HORIZONTAL == orientation) {
                    for (int m = 0; m < dashLineNumY; m++) {
                        int right = childView.getRight();
                        canvas.drawCircle(right + dashLineMarginLeft - dashLineMarginRight - dashLineHeight, 0, semicircleRadius, semicirclePaint);
                        canvas.drawCircle(right + dashLineMarginLeft - dashLineMarginRight - dashLineHeight, getHeight(), semicircleRadius, semicirclePaint);

                        float y = dashLineMarginTop + remindDashLineY / 2 + (dashLineGap + dashLineLength) * m;
                        canvas.drawRect(right + dashLineMarginLeft - dashLineMarginRight - dashLineHeight, y, right+ dashLineMarginLeft - dashLineMarginRight, y + dashLineLength, dashLinePaint);
                    }
                } else if (VERTICAL == orientation) {
                    int bottom = childView.getBottom();

                    for (int z = 0; z < semicircleNumX; z++) {
                        float x1 = semicircleGap + semicircleRadius + remindSemicircleX / 2 + (semicircleGap + semicircleRadius * 2) * z;
                        canvas.drawCircle(x1, 0, semicircleRadius, semicirclePaint);

                        float x2 = semicircleGap + semicircleRadius + remindSemicircleX / 2 + (semicircleGap + semicircleRadius * 2) * z;
                        canvas.drawCircle(x2, getHeight(), semicircleRadius, semicirclePaint);
                    }

                    for (int n = 0; n < dashLineNumX; n++) {
                        float x = dashLineMarginLeft + remindDashLineX / 2 + (dashLineGap + dashLineLength) * n;
                        canvas.drawRect(x, bottom + dashLineMarginTop - dashLineHeight - dashLineMarginBottom, x + dashLineLength, bottom + dashLineMarginTop - dashLineMarginBottom, dashLinePaint);
                    }
                }
            }
        }
    }

    public float getSemicircleRadius() {
        return px2Dp(semicircleRadius);
    }

    public void setSemicircleRadius(float semicircleRadius) {
        if (this.semicircleRadius != semicircleRadius) {
            this.semicircleRadius = semicircleRadius;
            calculate();
            invalidate();
        }
    }

    public float getSemicircleGap() {
        return px2Dp(semicircleGap);
    }

    public void setSemicircleGap(float semicircleGap) {
        if (this.semicircleGap != semicircleGap) {
            this.semicircleGap = semicircleGap;
            calculate();
            invalidate();
        }
    }

    public float getDashLineLength() {
        return px2Dp(dashLineLength);
    }

    public void setDashLineLength(float dashLineLength) {
        if (this.dashLineLength != dashLineLength) {
            this.dashLineLength = dashLineLength;
            calculate();
            invalidate();
        }
    }

    public float getDashLineHeight() {
        return px2Dp(dashLineHeight);
    }

    public void setDashLineHeight(float dashLineHeight) {
        if (this.dashLineHeight != dashLineHeight) {
            this.dashLineHeight = dashLineHeight;
            calculate();
            invalidate();
        }
    }


    public float getDashLineGap() {
        return px2Dp(dashLineGap);
    }

    public void setDashLineGap(float dashLineGap) {
        if (this.dashLineGap != dashLineGap) {
            this.dashLineGap = dashLineGap;
            calculate();
            invalidate();
        }
    }

    public int getDashLineColor() {
        return dashLineColor;
    }

    public void setDashLineColor(int dashLineColor) {
        if (this.dashLineColor != dashLineColor) {
            this.dashLineColor = dashLineColor;
            calculate();
            invalidate();
        }
    }

    public float getDashLineMarginTop() {
        return px2Dp(dashLineMarginTop);
    }

    public void setDashLineMarginTop(float dashLineMarginTop) {
        if (this.dashLineMarginTop != dashLineMarginTop) {
            this.dashLineMarginTop = dashLineMarginTop;
            calculate();
            invalidate();
        }
    }

    public float getDashLineMarginBottom() {
        return px2Dp(dashLineMarginBottom);
    }

    public void setDashLineMarginBottom(float dashLineMarginBottom) {
        if (this.dashLineMarginBottom != dashLineMarginBottom) {
            this.dashLineMarginBottom = dashLineMarginBottom;
            calculate();
            invalidate();
        }
    }

    public float getDashLineMarginLeft() {
        return px2Dp(dashLineMarginLeft);
    }

    public void setDashLineMarginLeft(float dashLineMarginLeft) {
        if (this.dashLineMarginLeft != dashLineMarginLeft) {
            this.dashLineMarginLeft = dashLineMarginLeft;
            calculate();
            invalidate();
        }
    }

    public float getDashLineMarginRight() {
        return px2Dp(dashLineMarginRight);
    }

    public void setDashLineMarginRight(float dashLineMarginRight) {
        if (this.dashLineMarginRight != dashLineMarginRight) {
            this.dashLineMarginRight = dashLineMarginRight;
            calculate();
            invalidate();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        calculate();
        invalidate();
    }
}
