package com.sxw.baseui.view.shadow.easyShadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sxw.baseui.R;


public class EasyShadowLayoutParamsData {
    int radius;
    int shadowColor;
    int shadowDx;
    int shadowDy;
    int shadowEvaluation;
    RectF widgetRect;
    Path widgetPath;
    Path clipPath;
    boolean needClip;
    boolean hasShadow;

    public EasyShadowLayoutParamsData(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyShadowLayout);
        radius = a.getDimensionPixelOffset(R.styleable.EasyShadowLayout_layout_radius, 0);
        shadowDx = a.getDimensionPixelOffset(R.styleable.EasyShadowLayout_layout_shadowDx, 0);
        shadowDy = a.getDimensionPixelOffset(R.styleable.EasyShadowLayout_layout_shadowDy, 0);
        shadowColor = a.getColor(R.styleable.EasyShadowLayout_layout_shadowColor, 0x99999999);
        shadowEvaluation = a.getDimensionPixelOffset(R.styleable.EasyShadowLayout_layout_shadowEvaluation, 0);
        a.recycle();
        needClip = radius > 0;
        hasShadow = shadowEvaluation > 0;
    }

    public void initPath(View v) {
        widgetRect = new RectF(v.getLeft(),
                v.getTop(),
                v.getRight(),
                v.getBottom());

        widgetPath = new Path();
        clipPath = new Path();
        clipPath.addRect(widgetRect, Path.Direction.CCW);
        clipPath.addRoundRect(
                widgetRect,
                radius,
                radius,
                Path.Direction.CW
        );
        widgetPath.addRoundRect(
                widgetRect,
                radius,
                radius,
                Path.Direction.CW
        );

    }
}
