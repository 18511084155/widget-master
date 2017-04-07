package com.woodys.widgets.indicator;

import android.graphics.RectF;

/**
 * Created by cz on 9/30/16.
 */
public class IndicatorConfig {
        public int indicatorColor;// 默认颜色
        public int indicatorAnimColor;// 执行动画颜色
        public float indicatorRadius;// 半径
        public float indicatorPadding;// 绘制间隔
        public float indicatorScale;// 设置缩放大小
        public int position;
        public float positionOffset;
        public boolean scrollToNext;
        public RectF indicatorRect;// 当前绘制区域

        public IndicatorConfig() {
            super();
            this.scrollToNext=true;
            this.indicatorRect = new RectF();
        }
}
