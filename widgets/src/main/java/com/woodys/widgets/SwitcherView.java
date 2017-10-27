package com.woodys.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maning on 16/7/17.
 * 垂直滚动的广告栏
 */
public class SwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    private static final int DEFAULT_TIME = 3000;
    private static final String TAG = "SwitcherView";

    private final List<String> items;
    private int currentIndex;
    private int indexCount = -1;
    private final int textSize;
    private final int textColor;
    private final int switchTime;
    private final int itemPadding;
    private boolean isRunning;
    private boolean isIntercept;
    private final Runnable switcherAction;
    private OnSwitcherItemClickListener listener;
    private OnSwitcherSelectListener onSwitcherSelectListener;

    public SwitcherView(Context context) {
        this(context, null);
    }

    public SwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.items = new ArrayList<>();
        this.switcherAction = new Runnable() {
            @Override
            public void run() {
                updateTextSwitcher(++indexCount);
                isIntercept = false;
            }
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitcherView);
        textColor = ta.getColor(R.styleable.SwitcherView_switcherTextColor, Color.BLACK);
        switchTime = ta.getInt(R.styleable.SwitcherView_switcherRollingTime, DEFAULT_TIME);
        itemPadding = (int) ta.getDimension(R.styleable.SwitcherView_switcherItemPadding, 0f);
        textSize = ta.getDimensionPixelSize(R.styleable.SwitcherView_switcherTextSize, 0);
        setInAnimation(getContext(), R.anim.m_switcher_vertical_in);
        setOutAnimation(getContext(), R.anim.m_switcher_vertical_out);
        setFactory(this);
        ta.recycle();
    }


    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColor);
        textView.setSingleLine();
        textView.setPadding(10, itemPadding, 10, itemPadding);
        textView.setBackgroundResource(R.drawable.white_item_selector);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        textView.setLayoutParams(lp);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClickListener(v, getCurrentIndex());
                }
            }
        });
        return textView;
    }

    public void setDataSource(List<String> items) {
        if (null != items) {
            destroySwitcher();
            this.items.addAll(items);
        }
    }

    public void setDataSource(String[] dataSource) {
        if (null != dataSource) {
            destroySwitcher();
            this.items.addAll(Arrays.asList(dataSource));
        }
    }

    public List<String> getDataSource() {
        return this.items;
    }


    private void updateTextSwitcher(int position) {
        int size = null != items ? items.size() : 0;
        if (!isIntercept && !items.isEmpty()) {
            if (position >= size) indexCount = 0;
            int index = (position >= size) ? 0 : position;
            if (hasWindowFocus() && getWindowToken() != null) {
                String content = items.get(index);
                if (!TextUtils.isEmpty(content)) setText(content);
                if (onSwitcherSelectListener != null) {
                    onSwitcherSelectListener.OnSelectListener(index);
                }
                currentIndex = index;
            }
        }
        if (size > 1) postDelayed(switcherAction, switchTime);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        removeCallbacks(switcherAction);
        if (isRunning && hasWindowFocus) {
            post(switcherAction);
        }
    }

    public void startRolling() {
        stopSwitcher();
        isRunning = true;
        post(switcherAction);
    }

    public String getCurrentItem() {
        return items.get(getCurrentIndex());
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void stopSwitcher() {
        isIntercept = false;
        isRunning = false;
        removeCallbacks(switcherAction);
        currentIndex = 0;
        indexCount = -1;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void destroySwitcher() {
        stopSwitcher();
        items.clear();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (MotionEvent.ACTION_DOWN == action || MotionEvent.ACTION_MOVE == action) {
            isIntercept = true;
        } else if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_CANCEL == action) {
            isIntercept = false;
        }
        return super.onTouchEvent(event);
    }

    public void setOnSwitcherItemClickListener(OnSwitcherItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnSwitcherItemClickListener {
        void onItemClickListener(View view, int index);
    }

    public void setOnSwitcherSelectListener(OnSwitcherSelectListener listener) {
        this.onSwitcherSelectListener = listener;
    }

    public interface OnSwitcherSelectListener {
        void OnSelectListener(int index);
    }

}