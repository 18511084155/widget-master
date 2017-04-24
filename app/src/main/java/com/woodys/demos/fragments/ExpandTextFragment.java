package com.woodys.demos.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.woodys.demos.R;
import com.woodys.widgets.ExpandTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandTextFragment extends Fragment {

    @Bind(R.id.bt_test)
    Button btTest;
    @Bind(R.id.etv_ExpandTextView)
    ExpandTextView etvExpandTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expand_text, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etvExpandTextView.startDropDownAnimator();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
