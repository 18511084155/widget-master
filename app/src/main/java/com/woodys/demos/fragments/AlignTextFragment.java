package com.woodys.demos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.woodys.demos.R;
import com.woodys.demos.widgets.AlignTextView1;
import com.woodys.widgets.textview.aligntextview.AlignTextView;
import com.woodys.widgets.textview.aligntextview.util.BCConvert;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlignTextFragment extends Fragment {
    @Bind(R.id.text_view)
    TextView mTextViewTv;
    @Bind(R.id.align_text_view)
    AlignTextView1 mAlignTv;
    @Bind(R.id.cb_align_text_view)
    AlignTextView mCBAlignTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_align_text_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String text = "订单号： （点击复制）xys12345678901234569999sdhaldadj999999这是一段中英文混合的文本,I am very happy today,aaaaaaaaaa," +
                "测试TextView文本对齐AlignTextView可以通过setAlign()方法设置每一段尾行的对齐方式, 默认尾行居左对齐. " +
                "CBAlignTextView可以像原生TextView一样操作,但是需要使用getRealText()获取文本,欢迎访问open.codeboy.me";
        mTextViewTv.setText(text);
//        mAlignTv.setPunctuationConvert(true);
        mAlignTv.setText(text);
        final SpannableStringBuilder builder = new SpannableStringBuilder(getString(R.string.order_no_copy, "xys12345678901234569999sdhaldadj999999"));
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getContext(),"复制成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ds.linkColor);
                ds.setUnderlineText(true);
                ds.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, getResources().getDisplayMetrics()));
            }
        }, 4, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getResources()
                        .getColor(R.color.colorBlueGreyPrimary)), 4,
                10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mAlignTv.setText(builder);
        mAlignTv.setMovementMethod(LinkMovementMethod.getInstance());

        mAlignTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mCBAlignTv.setText(builder.append(builder.toString()));
            }
        },2000);

        //mCBAlignTv.setText(builder);
        mCBAlignTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
