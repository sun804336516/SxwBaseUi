package com.sxw.baseui.view.shadow.easyShadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class EasyShadowConstraintLayout extends ConstraintLayout {
    private Paint shadowPaint;
    private Paint clipPaint;

    public EasyShadowConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setDither(true);
        shadowPaint.setFilterBitmap(true);
        shadowPaint.setStyle(Paint.Style.FILL);

        clipPaint = new Paint();
        clipPaint.setAntiAlias(true);
        clipPaint.setDither(true);
        clipPaint.setFilterBitmap(true);
        clipPaint.setStyle(Paint.Style.FILL);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ShadowLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ShadowLayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null) {
                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                if (layoutParams instanceof EasyShadowLayoutParams) {
                    EasyShadowLayoutParams elp = (EasyShadowLayoutParams) layoutParams;
                    EasyShadowLayoutParamsData data = elp.getData();
                    data.initPath(child);
                }
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = false;

        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp instanceof EasyShadowLayoutParams) {
            EasyShadowLayoutParams elp = (EasyShadowLayoutParams) lp;
            EasyShadowLayoutParamsData data = elp.getData();
            if (isInEditMode()) {//预览模式采用裁剪
                canvas.save();
                canvas.clipPath(data.widgetPath);
                ret = super.drawChild(canvas, child, drawingTime);
                canvas.restore();
                return ret;
            }
            if (!data.hasShadow && !data.needClip) {
                return super.drawChild(canvas, child, drawingTime);
            }
            //为解决锯齿问题，正式环境采用xfermode
            if (data.hasShadow) {
                int count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
                shadowPaint.setShadowLayer(data.shadowEvaluation, data.shadowDx, data.shadowDy, data.shadowColor);
                shadowPaint.setColor(data.shadowColor);
                canvas.drawPath(data.widgetPath, shadowPaint);
                shadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                shadowPaint.setColor(Color.WHITE);
                canvas.drawPath(data.widgetPath, shadowPaint);
                shadowPaint.setXfermode(null);
                canvas.restoreToCount(count);

            }
            if (data.needClip) {
                int count = canvas.saveLayer(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), null, Canvas.ALL_SAVE_FLAG);
                ret = super.drawChild(canvas, child, drawingTime);
                clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                clipPaint.setColor(Color.WHITE);
                canvas.drawPath(data.clipPath, clipPaint);
                clipPaint.setXfermode(null);
                canvas.restoreToCount(count);
            } else {
                ret = super.drawChild(canvas, child, drawingTime);
            }
        }
        return ret;
    }

    static class ShadowLayoutParams extends LayoutParams implements EasyShadowLayoutParams {

        private EasyShadowLayoutParamsData data;

        public ShadowLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            data = new EasyShadowLayoutParamsData(c, attrs);
        }

        @Override
        public EasyShadowLayoutParamsData getData() {
            return data;
        }
    }
}
