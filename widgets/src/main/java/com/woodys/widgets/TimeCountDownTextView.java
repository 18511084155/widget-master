package com.woodys.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by woodys on 2017/6/08.
 */
public class TimeCountDownTextView extends TextView {
    public final int MINUTES = 60 * 1000;// 分毫秒值
    public final int HOUR = 60 * MINUTES;// 小时毫秒值
    public final int DAY = 24 * HOUR;// 天毫秒值
    private CountDownTimer mTimer = null;
    private String mCss;
    private long mCountDownTime;
    private long mHour;
    private long mSecond;
    private long mMinute;
    private onCountDownFinishListener mOnCountDownFinishListener;

    public TimeCountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCountDownTime(context, attrs);
    }

    public TimeCountDownTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCountDownTime(context, attrs);
    }

    public TimeCountDownTextView(Context context) {
        super(context);
    }

    private void initCountDownTime(Context context, AttributeSet attrs) {
        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.TimeCountDownView);
        mCountDownTime = (long) attribute.getFloat(R.styleable.TimeCountDownView_tcd_countDownTime, 0);
        mCss = attribute.getString(R.styleable.TimeCountDownView_tcd_count_down_format);
        if (TextUtils.isEmpty(mCss)) {
            mCss = getContext().getString(R.string.count_down_default_format);
        }
    }

    public void setCountDownTimes(long countDownTime, String cssResId) {
        if (!TextUtils.isEmpty(cssResId)) {
            this.mCss = cssResId;
        }
        mCountDownTime = countDownTime;
    }

    public void setCountDownTimes(long countDownTime) {
        mCountDownTime = countDownTime;
    }

    public void start() {
        if (mCountDownTime < 0) {
            mCountDownTime = 0;
        }
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            int countDownInterval = 1000;
            mTimer = new CountDownTimer(mCountDownTime, countDownInterval) {
                @Override
                public void onTick(long millisUntilFinished) {

                    //当前时,分,秒
                    mHour = millisUntilFinished / HOUR;
                    mMinute = (millisUntilFinished - mHour * HOUR) / MINUTES;
                    mSecond = millisUntilFinished / 1000 % 60;

                    TimeCountDownTextView.this.setText(Html.fromHtml(String.format(mCss, mHour,mMinute, mSecond)));
                }

                @Override
                public void onFinish() {
                    if(mOnCountDownFinishListener != null){
                        mOnCountDownFinishListener.onFinish();
                    }
                }
            };
        }
        mTimer.start();
    }

    public void setOnCountDownFinishListener(onCountDownFinishListener onCountDownFinishListener) {
        this.mOnCountDownFinishListener = onCountDownFinishListener;
    }

    public interface onCountDownFinishListener {
        void onFinish();
    }
}
