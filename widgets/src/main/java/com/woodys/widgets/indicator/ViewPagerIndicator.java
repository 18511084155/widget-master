package com.woodys.widgets.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.woodys.widgets.R;

import java.util.ArrayList;

/**
 * ViewPagerIndicator,viewpager滑动指示器
 * Created by cz on 15/2/9.
 * need review code
 */
public final class ViewPagerIndicator extends View implements ViewPager.OnPageChangeListener {
    // 方向
    private static final int CENTER = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    private final ArrayList<CircleIndicatorDrawable> indicatorDrawables;
    private ViewPager.OnPageChangeListener pagerChangeListener;
    private final IndicatorConfig indicatorConfig;
    private int indicatorGravity;// 方向

    private int currentPosition;
    private int nextPosition;
    private int lastPositionOffsetPixels;
    private boolean moveRight;
    private boolean moveLeft;

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        indicatorDrawables = new ArrayList<>();
        this.indicatorConfig = new IndicatorConfig();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        setIndicatorRadius(a.getDimension(R.styleable.ViewPagerIndicator_vi_indicatorRadius, applyDimension(8)));
        setIndicatorGravity(a.getInt(R.styleable.ViewPagerIndicator_vi_circleGravity, CENTER));
        setIndicatorPadding(a.getDimension(R.styleable.ViewPagerIndicator_vi_indicatorPadding, applyDimension(2)));
        setIndicatorScale(a.getDimension(R.styleable.ViewPagerIndicator_vi_indicatorScale, 0));
        setIndicatorColor(a.getColor(R.styleable.ViewPagerIndicator_vi_indicatorColor, Color.DKGRAY));
        setIndicatorAnimColor(a.getColor(R.styleable.ViewPagerIndicator_vi_indicatorAnimColor, Color.LTGRAY));
        a.recycle();
    }

    public float applyDimension(int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,getResources().getDisplayMetrics());
    }


    /**
     * 设置缩放比例
     *
     * @param size
     */
    public void setIndicatorScale(float size) {
        this.indicatorConfig.indicatorScale = size;
        invalidate();
    }

    /**
     * 设置动画绘制颜色
     *
     * @param color
     */
    public void setIndicatorAnimColor(int color) {
        this.indicatorConfig.indicatorAnimColor = color;
    }

    /**
     * 设置绘制颜色
     *
     * @param color
     */
    public void setIndicatorColor(int color) {
        this.indicatorConfig.indicatorColor = color;
    }

    /**
     * 设置绘制间隔
     *
     * @param padding
     */
    public void setIndicatorPadding(float padding) {
        this.indicatorConfig.indicatorPadding = padding;
        invalidate();
    }

    /**
     * 设置绘制方向
     *
     * @param gravity
     */
    public void setIndicatorGravity(int gravity) {
        this.indicatorGravity = gravity;
        invalidate();
    }

    /**
     * 设置圆心半径
     *
     * @param radius
     */
    public void setIndicatorRadius(float radius) {
        this.indicatorConfig.indicatorRadius = radius;
        this.indicatorConfig.indicatorRadius = radius;
        this.indicatorConfig.indicatorRect.right = radius;
        this.indicatorConfig.indicatorRect.bottom = radius;
        invalidate();
        // TODO 执行属性动画,控件drawable变幻尺寸
    }


    public float getIndicatorRadius() {
        return this.indicatorConfig.indicatorRadius;
    }


    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context) {
        this(context, null, 0);
    }

    public void setupViewPager(ViewPager viewPager) {
        if (null == viewPager || null == viewPager.getAdapter()) {
            throw new NullPointerException("viewpager or adapter is null!");
        }
        final PagerAdapter adapter = viewPager.getAdapter();
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setIndicatorDrawableCount(adapter.getCount());
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                setIndicatorDrawableCount(adapter.getCount());
            }
        });
        viewPager.setOnPageChangeListener(this);
        setIndicatorDrawableCount(viewPager.getChildCount());
    }

    public void setIndicatorDrawableCount(int count) {
        indicatorDrawables.clear();
        for (int i = 0; i < count; i++) {
            indicatorDrawables.add(new CircleIndicatorDrawable(i, count, indicatorConfig));
        }
        this.indicatorConfig.position = 0;
        this.indicatorConfig.positionOffset = 0;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int status) {
        if (null != pagerChangeListener) {
            pagerChangeListener.onPageSelected(status);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (lastPositionOffsetPixels > positionOffsetPixels) {
            moveRight = true;
            moveLeft = false;
        } else if (lastPositionOffsetPixels < positionOffsetPixels) {
            moveRight = false;
            moveLeft = true;
        }
        lastPositionOffsetPixels = positionOffsetPixels;

        if (null != pagerChangeListener) {
            pagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        int size = indicatorDrawables.size();
        for (int i = 0; i < size; i++) {
            CircleIndicatorDrawable drawable = indicatorDrawables.get(i);
            drawable.onPageScrolled(position, positionOffset,isMovedFromNext());
        }
        this.indicatorConfig.position = position;
        this.indicatorConfig.positionOffset = positionOffset;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (null != pagerChangeListener) {
            pagerChangeListener.onPageSelected(position);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.pagerChangeListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int indicatorCount = this.indicatorDrawables.size();
        int indicatorSize = (int) (this.indicatorConfig.indicatorRadius + this.indicatorConfig.indicatorPadding);
        // 计算出所用矩阵大小
        int drawWidth = indicatorSize * indicatorCount;
        float scaleSize = this.indicatorConfig.indicatorScale / 2;
        for (int i = 0; i < indicatorCount; ++i) {
            float dx, dy = (height - this.indicatorConfig.indicatorRadius) / 2;
            switch (indicatorGravity) {
                case LEFT:
                    dx = i * indicatorSize + this.indicatorConfig.indicatorPadding;
                    break;
                case RIGHT:
                    dx = width - drawWidth + i * indicatorSize;
                    break;
                case CENTER:
                default:
                    dx = (width - drawWidth) / 2 + i * indicatorSize;
                    break;
            }
            canvas.save();
            // 根据当前选中条目的scaleSize设置translate取值
            if (indicatorConfig.scrollToNext&&i == this.indicatorConfig.position) {
                canvas.translate(dx - (scaleSize * (1f - this.indicatorConfig.positionOffset)), dy - (scaleSize * (1f - this.indicatorConfig.positionOffset)));
            } else if (!indicatorConfig.scrollToNext&&i == this.indicatorConfig.position) {
                canvas.translate(dx - (scaleSize * (this.indicatorConfig.positionOffset)), dy - (scaleSize * (this.indicatorConfig.positionOffset)));
            } else if (indicatorConfig.scrollToNext&&(i == (indicatorCount - 1 == this.indicatorConfig.position ? 0 : this.indicatorConfig.position + 1))) {
                canvas.translate(dx - (scaleSize * this.indicatorConfig.positionOffset), dy - (scaleSize * this.indicatorConfig.positionOffset));
            } else if (!indicatorConfig.scrollToNext&&(i == (0> this.indicatorConfig.position-1 ? indicatorCount -1 : this.indicatorConfig.position-1))) {
                canvas.translate(dx - (scaleSize * ( 1f-this.indicatorConfig.positionOffset)), dy - (scaleSize * (1f-this.indicatorConfig.positionOffset)));
            } else {
                canvas.translate(dx, dy);
            }
            indicatorDrawables.get(i).draw(canvas);
            canvas.restore();
        }
    }

    private boolean isMovedFromPrevious() {
        return moveLeft;
    }

    private boolean isMovedFromNext() {
        return moveRight;
    }

}
