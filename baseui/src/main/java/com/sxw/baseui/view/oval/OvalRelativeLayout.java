package com.sxw.baseui.view.oval;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.RoundUtil;
import com.sxw.baseui.utils.ViewUtils;


/**
 * 自定义圆角LinearLayout
 */
public class OvalRelativeLayout extends RelativeLayout {

    public OvalRelativeLayout(Context context) {
        this(context, null);
    }
    private RoundUtil mRoundUtil;

    public OvalRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public OvalRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OvalLayout);
        float radius = typedArray.getDimension(R.styleable.OvalLayout_oval_radius, ViewUtils.dp2px(5));
        typedArray.recycle();
        mRoundUtil = new RoundUtil(context,radius);
        setWillNotDraw(false);
    }

    public void setCorner(float adius) {
        mRoundUtil.setRect_rdius(adius);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRoundUtil.setRoundRect(new RectF(0, 0, getWidth(), getHeight()));
    }

    @Override
    public void draw(Canvas canvas) {
        mRoundUtil.draw(canvas);
        super.draw(canvas);
        canvas.restore();
    }

}