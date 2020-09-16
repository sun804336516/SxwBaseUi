package com.sxw.baseui.view.shadow;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * 作者：sxw on 2019/7/29 15:07
 */
public class ShadowTextView extends AppCompatTextView implements Checkable {

    private ShadowHelper mShadowHelper;

    public ShadowTextView(Context context) {
        this(context, null);
    }

    public ShadowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mShadowHelper = new ShadowHelper(this, context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mShadowHelper.createShadowBitmap(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mShadowHelper.createShadowBitmap(right - left, bottom - top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mShadowHelper.onDraw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public void setChecked(boolean checked) {
        mShadowHelper.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mShadowHelper.isChecked();
    }

    @Override
    public void toggle() {
        mShadowHelper.toggle();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mShadowHelper.destroy();
    }
}
