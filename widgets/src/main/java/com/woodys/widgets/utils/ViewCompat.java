package com.woodys.widgets.utils;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ViewCompat {

    public static void setDefaultViewTranslation(ViewGroup view){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            LayoutTransition layoutTransition = new LayoutTransition();

            //view出现时 view自身的动画效果
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(null, "alpha", 0f, 1F).setDuration(layoutTransition.getDuration(LayoutTransition.APPEARING));
            layoutTransition.setAnimator(LayoutTransition.APPEARING, animator1);

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 1F, 0f).setDuration(layoutTransition.getDuration(LayoutTransition.DISAPPEARING));
            layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animator2);
            //view 动画改变时，布局中的每个子view动画的时间间隔
            layoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
            layoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);

            PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
            PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
            PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
            PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
            final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(view, pvhLeft, pvhTop, pvhRight, pvhBottom).
                    setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_APPEARING));
            layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);

            final ObjectAnimator changeOut = ObjectAnimator.ofPropertyValuesHolder(view, pvhLeft, pvhTop, pvhRight, pvhBottom).
                    setDuration(layoutTransition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
            layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
            view.setLayoutTransition(layoutTransition);
        }
    }


}
