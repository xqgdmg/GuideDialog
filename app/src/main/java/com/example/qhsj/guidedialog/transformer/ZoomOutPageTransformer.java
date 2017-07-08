package com.example.qhsj.guidedialog.transformer;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/*
 * 变形金刚，有木有
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @SuppressLint("NewApi")
    public void transformPage(View view, float position) {

         // position 每次都会同时打印两次，两个绝对值相加的结果为 1，a页 & b页
         // 初始值 a 页 0 ；b 页 1
        Log.e("chris","position===" + position);

        if (position < -1) { // 不在合理范围内
            view.setAlpha(0);
        } else if (position <= 1){  //a页滑动至b页 --> a页从 0.0 -1 ；b页从1 - 0.0

             // 当 position 的绝对值，小于 0.15 的时候，缩放倍率会大于 0.85
             // 效果就是 左右两个临界值会一点一点的放大或者缩小
            float scaleValue = Math.max(MIN_SCALE, 1 - Math.abs(position));

            // 缩放 x轴 和 y轴，scaleValue==1 的时候不会缩放
            view.setScaleX(scaleValue);
            view.setScaleY(scaleValue);

            // 效果就是 左右两个临界值会一点一点的变清晰
            view.setAlpha(MIN_ALPHA + (scaleValue - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // 不在合理范围内
            view.setAlpha(0);
        }
    }
}