package com.woodys.widgets.dashed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.woodys.widgets.R;

/**
 * Created by woodys on 17/4/10.
 */

public abstract class BaseDashedLine extends View {

    protected Paint mPaint;
    protected int mDashColor;
    protected float mStrokeWidth;
    protected DashEffect mDashEffect;

    public BaseDashedLine(Context context) {
        super(context);
        initParent();
    }

    public BaseDashedLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        initParent();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DashedLineView);
        mDashColor = typedArray.getColor(R.styleable.DashedLineView_dlv_dashColor, Color.TRANSPARENT);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.DashedLineView_dlv_strokeWidth, 0);
        int mDashGap = typedArray.getDimensionPixelSize(R.styleable.DashedLineView_dlv_dashGap, 0);
        int mDashWidth = typedArray.getDimensionPixelSize(R.styleable.DashedLineView_dlv_dashWidth, 1);
        int mOffset = typedArray.getDimensionPixelSize(R.styleable.DashedLineView_dlv_offset, 0);
        mDashEffect = new DashEffect(mDashWidth, mDashGap,mOffset);

        initAttr(typedArray);
        typedArray.recycle();
    }

    protected abstract void initAttr(TypedArray typedArray);

    protected void initParent() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void setDashColor(int color) {
        mDashColor = color;
    }

    public void setDashEffect(int dashWidth, int dashGap) {
        mDashEffect.setDashEffect(dashWidth, dashGap);
    }

    public void setDashEffect(int dashWidth, int dashGap, int offset) {
        mDashEffect.setDashEffect(dashWidth, dashGap, offset);
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mDashColor);
        mPaint.setPathEffect(mDashEffect.getDashEffect());

        onDrawDash(canvas, mPaint, mStrokeWidth);
    }

    protected abstract void onDrawDash(Canvas canvas, Paint paint, float strokeWidth);

    protected class DashEffect {
        protected int dashWidth;
        protected int dashGap;
        protected int offset;
        protected DashPathEffect dashEffect;

        public DashEffect(int dashWidth, int dashGap) {
            setDashEffect(dashWidth, dashGap);
        }

        public DashEffect(int dashWidth, int dashGap, int offset) {
            setDashEffect(dashWidth, dashGap, offset);
        }

        public void setDashEffect(int dashWidth, int dashGap) {
            this.dashWidth = dashWidth;
            this.dashGap = dashGap;
            dashEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, 0);
        }

        public void setDashEffect(int dashWidth, int dashGap, int offset) {
            this.dashWidth = dashWidth;
            this.dashGap = dashGap;
            this.offset = offset;
            dashEffect = new DashPathEffect(new float[]{dashWidth, dashGap}, offset);
        }

        public DashPathEffect getDashEffect() {
            return dashEffect;
        }
    }
}
