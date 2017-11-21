package com.woodys.demos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woodys.demos.R;
import com.woodys.widgets.textview.aligntextview.AlignTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlignTextFragment extends Fragment {
    @Bind(R.id.text_view)
    TextView mTextViewTv;
    @Bind(R.id.cb_align_text_view)
    AlignTextView mAlignTv;

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

        final String text = "订单号： （点击复制）xys12345678901234569999sdhaldadj999999这是一段中英文混合的文本,I am very happy today,aaaaaaaaaa," +
                "测试TextView文本对齐AlignTextView可以通过setAlign()方法设置每一段尾行的对齐方式, 默认尾行居左对齐. " +
                "CBAlignTextView可以像原生TextView一样操作,但是需要使用getRealText()获取文本,欢迎访问open.codeboy.me";
        mTextViewTv.setText(text);
//        mAlignTv.setPunctuationConvert(true);
        mAlignTv.setText(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
