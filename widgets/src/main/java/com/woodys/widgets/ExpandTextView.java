package com.woodys.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by woodys on 17/04/21.
 * 两种模式,三种对齐选择和一个padding值
 */
public class ExpandTextView extends TextView implements View.OnClickListener {

    private static final int MAX_COLLAPSED_LINES = 8;// The default number of lines 默认显示行数为8行
    private static final int DEFAULT_ANIM_DURATION = 300; // The default animation duration 默认动画时长为300ms
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;// The default alpha value when the animation starts

    private int mMaxCollapsedLines = 8;//最大显示行数
    private int mAnimationDuration;
    private float mAnimAlphaStart;
    private Drawable mExpandDrawable;//展开前显示图片
    private Drawable mCollapseDrawable;//展开后图片

    private int mTextMaxLines;

    private boolean mCollapsed; // Show short version as default.
    //标示现在所处的折叠状态
    private boolean IS_COLLAPSED = false;
    private boolean mAnimating = false;
    private boolean needCollapse = true; //标示是否需要折叠已显示末尾的图标

    private Lock mLock;
    private int mDrawableSize = 0;

    /**
     * 表示箭头对齐方式,靠左/上,右/下,还是居中
     */
    private static final int ALIGN_RIGHT_BOTTOM = 0;
    private static final int ALIGN_LEFT_TOP = 1;
    private static final int ALIGN_CENTER = 2;
    private int arrowAlign = ALIGN_RIGHT_BOTTOM;

    /**
     * 表示箭头显示位置,在文字右边还是在文字下边
     */
    private static final int POSITION_RIGHT = 0;
    private static final int POSITION_BELOW = 1;
    private int arrowPosition = POSITION_RIGHT;

    /**
     * 箭头图标和文字的距离
     */
    private int arrowDrawablePadding = 0;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;


    public ExpandTextView(Context context) {
        this(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLock = new ReentrantLock();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, defStyleAttr, 0);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandTextView_etv_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandTextView_etv_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandTextView_etv_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandTextView_etv_expandDrawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandTextView_etv_collapseDrawable);
        arrowAlign = typedArray.getInteger(R.styleable.ExpandTextView_etv_arrowAlign, ALIGN_RIGHT_BOTTOM);
        arrowPosition = typedArray.getInteger(R.styleable.ExpandTextView_etv_arrowPosition, POSITION_RIGHT);
        arrowDrawablePadding = (int) typedArray.getDimension(R.styleable.ExpandTextView_etv_arrowPadding, DensityUtil.dp2px(context, 2f));
        mCollapsed = typedArray.getBoolean(R.styleable.ExpandTextView_etv_is_collapsed,IS_COLLAPSED);
        typedArray.recycle();

        setClickable(true);
        setOnClickListener(this);
    }

    private boolean isDrawablePaddingResolved = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getVisibility() == GONE || mAnimating) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //测量TextView总高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTextMaxLines = getLineCount();
        if (getLineCount() <= mMaxCollapsedLines) {
            needCollapse = false;
            return;
        }
        needCollapse = true;
        setMaxLines(mCollapsed?mMaxCollapsedLines:Integer.MAX_VALUE);
        //小于等于0的时候隐藏高度
        if(mCollapsed && mMaxCollapsedLines<=0) {
            getLayoutParams().height = 0;
            setMaxHeight(0);
        }
        mDrawableSize =(null!= mExpandDrawable)?mExpandDrawable.getIntrinsicWidth():0;
        if (!isDrawablePaddingResolved) {
            if (arrowPosition == POSITION_RIGHT) {
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight() + mDrawableSize + arrowDrawablePadding, getPaddingBottom());
            } else {
                setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + ((null!= mExpandDrawable)?mExpandDrawable.getIntrinsicHeight():0) + arrowDrawablePadding);
            }
            isDrawablePaddingResolved = true;
        }
        //设置完成后重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (needCollapse) {
            int left, top;
            if (arrowPosition == POSITION_RIGHT) {
                left = getWidth() - getTotalPaddingRight() + arrowDrawablePadding;
                switch (arrowAlign) {
                    case ALIGN_LEFT_TOP:
                        top = getTotalPaddingTop();
                        break;
                    case ALIGN_CENTER:
                        top = (getHeight() - ((null!= mExpandDrawable)?mExpandDrawable.getIntrinsicHeight():0)) / 2;
                        break;
                    case ALIGN_RIGHT_BOTTOM:
                    default:
                        top = getHeight() - getTotalPaddingBottom() - ((null!= mExpandDrawable)?mExpandDrawable.getIntrinsicHeight():0);
                        break;
                }
            } else {
                top = getHeight() - getTotalPaddingBottom() + arrowDrawablePadding;
                switch (arrowAlign) {
                    case ALIGN_LEFT_TOP:
                        left = getTotalPaddingLeft();
                        break;
                    case ALIGN_CENTER:
                        left = (getWidth() - ((null!= mExpandDrawable)?mExpandDrawable.getIntrinsicWidth():0)) / 2;
                        break;
                    case ALIGN_RIGHT_BOTTOM:
                    default:
                        left = getWidth() - getTotalPaddingRight() -((null!= mExpandDrawable)?mExpandDrawable.getIntrinsicWidth():0);
                        break;
                }
            }
            canvas.translate(left, top);

            if (mCollapsed) {
                if(null!=mExpandDrawable){
                    mExpandDrawable.setBounds(0, 0, mExpandDrawable.getIntrinsicWidth(), mExpandDrawable.getIntrinsicHeight());
                    mExpandDrawable.draw(canvas);
                }
            } else {
                if(null!=mCollapseDrawable) {
                    mCollapseDrawable.setBounds(0, 0, mCollapseDrawable.getIntrinsicWidth(), mCollapseDrawable.getIntrinsicHeight());
                    mCollapseDrawable.draw(canvas);
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    @Override
    public void onClick(View v) {
        startDropDownAnimator();
    }

    public void startDropDownAnimator(){
        startDropDownAnimator(!mCollapsed);
    }

    public void startDropDownAnimator(final boolean collapsed){
        mLock.lock();
        setCollapsed(collapsed);
        if (!needCollapse) {
            return;//行数不足,不响应点击事件
        }
        // mark that the animation is in progress
        mAnimating = true;
        Animation animation = new ExpandCollapseAnimation(this, getHeight(), getRealTextViewHeight(this,collapsed?mMaxCollapsedLines:Integer.MAX_VALUE));
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mListener != null) {
                    mListener.onChangeStateStart(!collapsed);
                }
                applyAlphaAnimation(ExpandTextView.this, mAnimAlphaStart);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                // notify the listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(ExpandTextView.this, !collapsed);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(animation);
        mLock.unlock();
    }

    private class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTargetView.getLayoutParams().height = newHeight;
            setMaxHeight(newHeight);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(ExpandTextView.this, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }


    private Drawable getDrawable(Context context, int drawableResId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(drawableResId, context.getTheme());
        } else {
            return resources.getDrawable(drawableResId);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            // make it instant
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    private boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private int getRealTextViewHeight(TextView textView,int line) {
        int textLineCount = Math.min(line,mTextMaxLines);
        int textHeight = textView.getLayout().getLineTop(textLineCount);
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textLineCount>0?textHeight + padding:0;
    }


    public void setCollapsed(boolean isCollapsed) {
        mCollapsed = isCollapsed;
    }

    public boolean isCollapsed() {
        return mCollapsed;
    }


    public interface OnExpandStateChangeListener {
        void onChangeStateStart(boolean willExpand);
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }

    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }
}
