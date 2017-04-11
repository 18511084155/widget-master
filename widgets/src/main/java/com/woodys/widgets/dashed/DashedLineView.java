package com.woodys.widgets.dashed;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.woodys.widgets.R;

/**
 * Created by woodys on 17/4/10.
 *
 * 自定义虚线的实现
 */

public class DashedLineView extends BaseDashedLine {

    /**虚线的方向*/
    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;
    /**默认为水平方向*/
    public static final int DEFAULT_DASH_ORIENTATION = ORIENTATION_HORIZONTAL;

    private Path mPath;
    private int mOrientation;

    public DashedLineView(Context context) {
        super(context);
        init();
    }

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void initAttr(TypedArray typedArray) {
        mOrientation = typedArray.getInt(R.styleable.DashedLineView_dlv_dashOrientation, DEFAULT_DASH_ORIENTATION);
    }

    private void init() {
        mPath = new Path();
    }

    public void setOrientation(Orientation orientation) {
        switch (orientation) {
            case HORIZONTAL:
                mOrientation = ORIENTATION_HORIZONTAL;
                break;
            case VERTICAL:
                mOrientation = ORIENTATION_VERTICAL;
                break;
        }
        invalidate();
    }

    @Override
    protected void onDrawDash(Canvas canvas, Paint paint, float strokeWidth) {
        int width = getWidth();
        int height = getHeight();

        mPaint.setStrokeWidth(mStrokeWidth != 0 ? mStrokeWidth : (mOrientation == ORIENTATION_VERTICAL ? width : height));

        mPath.rewind();
        if (mOrientation == ORIENTATION_VERTICAL) { /// 垂直
            mPath.moveTo(width / 2, 0);
            mPath.lineTo(width / 2, height);
        } else { /// 水平
            mPath.moveTo(0, height / 2);
            mPath.lineTo(width, height / 2);
        }
        canvas.drawPath(mPath, paint);
    }

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }
}
