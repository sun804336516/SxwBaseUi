package com.sxw.baseui.view.imageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 作者：sxw on 2019/9/12/012 17:26
 */
public class MaskImageView extends ImageView {
    private int maskColor = Color.parseColor("#86222222");
    private boolean drawMask = false;
    private final Paint mMaskPaint = new Paint();

    public void setDrawMask(boolean drawMask) {
        this.drawMask = drawMask;
        invalidate();
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        mMaskPaint.setColor(maskColor);
        invalidate();
    }


    public MaskImageView(Context context) {
        this(context, null);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMaskPaint.setColor(maskColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawMask) {
            canvas.drawRect(new RectF(0, 0, getMeasuredWidth(), getMaxHeight()), mMaskPaint);
        }
    }
}
