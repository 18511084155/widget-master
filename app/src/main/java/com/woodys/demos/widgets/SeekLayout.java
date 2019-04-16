package com.woodys.demos.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.woodys.demos.R;

/**
 * Created by cz on 16/3/7.
 */
public class SeekLayout extends LinearLayout {
    private OnSeekProgressChangeListener listener;

    public SeekLayout(Context context) {
        this(context, null);
    }

    public SeekLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.seek_layout, this);
        final TextView infoView = (TextView) findViewById(R.id.info);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekLayout);
        seekBar.setMax(a.getInteger(R.styleable.SeekLayout_sl_max, 100));
        final String info = a.getString(R.styleable.SeekLayout_sl_info);
        infoView.setText(info);
        a.recycle();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                infoView.setText(info + ":" + progress);
                if (null != listener) {
                    listener.onProgressChanged(seekBar, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    public void setOnSeekProgressChangeListener(OnSeekProgressChangeListener listner) {
        this.listener = listner;
    }

    public interface OnSeekProgressChangeListener {
        void onProgressChanged(SeekBar seekBar, int progress);
    }
}
