package com.woodys.widgets.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;


/**
 * 圆形指示器drawable
 * <p/>
 * Created by cz on 15/2/9.
 */
public class CircleIndicatorDrawable extends ShapeDrawable {
    private static final String TAG = "CircleIndicatorDrawable";
    private IndicatorConfig indicatorConfig;
    private int indicatorIndex;// 当前位置
    private int indicatorCount;// 当前绘制总数
    private Paint paint;

    /**
     * 初始化角度
     */
    public CircleIndicatorDrawable(int index, int count, IndicatorConfig config) {
        super();
        this.indicatorConfig = config;
        this.indicatorIndex = index;
        this.indicatorCount = count;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(this.indicatorConfig.indicatorAnimColor);
        getPaint().setColor(this.indicatorConfig.indicatorColor);
    }


    @Override
    public void draw(Canvas canvas) {
        drawSizeCircle(canvas);
    }

    /**
     * 绘制颜色,大小改变的动画
     *
     * @param canvas
     */
    private void drawSizeCircle(Canvas canvas) {
        RectF newRect = initScaleRect(this.indicatorConfig.indicatorRect, indicatorConfig.scrollToNext);
        if (indicatorIndex == this.indicatorConfig.position) {
            // setColor无效,当使用shader绘制时,就必须使用shader改变取值
            int color = evaluate(this.indicatorConfig.scrollToNext?this.indicatorConfig.positionOffset:1f- indicatorConfig.positionOffset, this.indicatorConfig.indicatorAnimColor, this.indicatorConfig.indicatorColor);
            paint.setColor(color);
            canvas.drawOval(newRect, paint);
        } else if (indicatorConfig.scrollToNext&&(indicatorIndex == (indicatorCount - 1 == this.indicatorConfig.position ? 0 : this.indicatorConfig.position + 1))) {
            //往右到头
            int color = evaluate(this.indicatorConfig.positionOffset, this.indicatorConfig.indicatorColor, this.indicatorConfig.indicatorAnimColor);
            paint.setColor(color);
            canvas.drawOval(newRect, paint);
        } else if (!indicatorConfig.scrollToNext&& indicatorIndex == (0> this.indicatorConfig.position-1 ? indicatorCount -1 : this.indicatorConfig.position-1)) {
            //往左,到头
            int color = evaluate(1f-this.indicatorConfig.positionOffset, this.indicatorConfig.indicatorColor, this.indicatorConfig.indicatorAnimColor);
            paint.setColor(color);
            canvas.drawOval(newRect, paint);
        } else {
            getPaint().setColor(indicatorConfig.indicatorColor);
            canvas.drawOval(newRect, getPaint());
        }
    }


    private RectF initScaleRect(RectF rect,boolean scrollToNext) {
        RectF newRect = new RectF(rect);
        if (scrollToNext&& indicatorIndex == this.indicatorConfig.position) {
            newRect.right += (this.indicatorConfig.indicatorScale * (1f - this.indicatorConfig.positionOffset));
            newRect.bottom += (this.indicatorConfig.indicatorScale * (1f - this.indicatorConfig.positionOffset));
        } else if (!scrollToNext&& indicatorIndex == this.indicatorConfig.position) {
            newRect.right += (this.indicatorConfig.indicatorScale * (this.indicatorConfig.positionOffset));
            newRect.bottom += (this.indicatorConfig.indicatorScale * (this.indicatorConfig.positionOffset));
        } else if (scrollToNext&& indicatorIndex == (indicatorCount - 1 == this.indicatorConfig.position ? 0 : this.indicatorConfig.position + 1)) {
            newRect.right += (this.indicatorConfig.indicatorScale * this.indicatorConfig.positionOffset);
            newRect.bottom += (this.indicatorConfig.indicatorScale * this.indicatorConfig.positionOffset);
        } else if(!scrollToNext&& indicatorIndex == (0> this.indicatorConfig.position-1 ? indicatorCount -1 : this.indicatorConfig.position-1)){
            newRect.right += (this.indicatorConfig.indicatorScale * (1f - this.indicatorConfig.positionOffset));
            newRect.bottom += (this.indicatorConfig.indicatorScale * (1f - this.indicatorConfig.positionOffset));
        }
        return newRect;
    }


    public void onPageScrolled(int position, float offset,boolean toNext) {
        this.indicatorConfig.position = position % indicatorCount;
        this.indicatorConfig.positionOffset = offset;
        this.indicatorConfig.scrollToNext=toNext;
    }
    /**
     * 计算颜色渐变取值
     *
     * @param fraction   偏移量
     * @param startValue
     * @param endValue
     * @return
     */
    public int evaluate(float fraction, int startValue, int endValue) {
        int startInt =  startValue;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt =  endValue;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return  ((startA + (int) (fraction * (endA - startA))) << 24) | ((startR + (int) (fraction * (endR - startR))) << 16) |  ((startG + (int) (fraction * (endG - startG))) << 8)
                |  ((startB + (int) (fraction * (endB - startB))));
    }

}
