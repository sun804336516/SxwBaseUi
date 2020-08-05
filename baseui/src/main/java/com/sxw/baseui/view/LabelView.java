package com.sxw.baseui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.sxw.baseui.R;


/**
 * 热门标签控件
 */
public class LabelView extends View {
    private String mTextContent;
    private int mTextColor;
    private float mTextSize;
    private boolean mTextBold;
    private boolean mFillTriangle;
    private boolean mTextAllCaps;
    protected float mFullTriangleDegree = 0;
    private int mBackgroundColor;
    private float mMinSize;
    private float mPadding;
    private int mGravity;
    private static final int DEFAULT_DEGREES = 45;

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mPath = new Path();
    private int mFqSize;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        obtainAttributes(context, attrs);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelView);
        mFqSize = ta.getInt(R.styleable.LabelView_lv_fill_size, 0);
        mTextContent = ta.getString(R.styleable.LabelView_lv_text);
        mTextColor = ta.getColor(R.styleable.LabelView_lv_text_color, Color.parseColor("#ffffff"));
        mTextSize = ta.getDimension(R.styleable.LabelView_lv_text_size, sp2px(11));
        mFullTriangleDegree = ta.getDimension(R.styleable.LabelView_lv_fill_triangle_degree, sp2px(0));
        mTextBold = ta.getBoolean(R.styleable.LabelView_lv_text_bold, true);
        mTextAllCaps = ta.getBoolean(R.styleable.LabelView_lv_text_all_caps, true);
        mFillTriangle = ta.getBoolean(R.styleable.LabelView_lv_fill_triangle, false);
        mBackgroundColor = ta.getColor(R.styleable.LabelView_lv_background_color, Color.parseColor("#FF4081"));
        mMinSize = ta.getDimension(R.styleable.LabelView_lv_min_size, mFillTriangle ? dp2px(35) : dp2px(50));
        mPadding = ta.getDimension(R.styleable.LabelView_lv_padding, dp2px(3.5f));
        mGravity = ta.getInt(R.styleable.LabelView_lv_gravity, Gravity.TOP | Gravity.LEFT);
        ta.recycle();
    }

    public LabelView setTextColor(int textColor) {
        mTextColor = textColor;
        return this;
    }

    public LabelView setText(String text) {
        mTextContent = text;
        return this;
    }

    public LabelView setTextSize(float textSize) {
        mTextSize = sp2px(textSize);
        return this;
    }

    public LabelView setTextBold(boolean textBold) {
        mTextBold = textBold;
        return this;
    }

    public LabelView setFillTriangle(boolean fillTriangle) {
        mFillTriangle = fillTriangle;
        return this;
    }

    public LabelView setTextAllCaps(boolean textAllCaps) {
        mTextAllCaps = textAllCaps;
        return this;
    }

    public LabelView setBgColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
        return this;
    }

    public LabelView setMinSize(float minSize) {
        mMinSize = dp2px(minSize);
        return this;
    }

    public LabelView setPadding(float padding) {
        mPadding = dp2px(padding);
        return this;
    }

    /**
     * Gravity.TOP | Gravity.LEFT
     * Gravity.TOP | Gravity.RIGHT
     * Gravity.BOTTOM | Gravity.LEFT
     * Gravity.BOTTOM | Gravity.RIGHT
     */
    public LabelView setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public String getText() {
        return mTextContent;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public boolean isTextBold() {
        return mTextBold;
    }

    public boolean isFillTriangle() {
        return mFillTriangle;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public int getBgColor() {
        return mBackgroundColor;
    }

    public float getMinSize() {
        return mMinSize;
    }

    public float getPadding() {
        return mPadding;
    }

    public int getGravity() {
        return mGravity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = getHeight();

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setFakeBoldText(mTextBold);
        mBackgroundPaint.setColor(mBackgroundColor);

        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        int i = dp2px(mFqSize);
        if (mFillTriangle) {
            if (mGravity == (Gravity.TOP | Gravity.LEFT)) {
                mPath.reset();
                mPath.moveTo(0, 0);
                mPath.lineTo(0, size);
                mPath.lineTo(size, 0);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawTextWhenFill(size, -DEFAULT_DEGREES, canvas, true);
            } else if (mGravity == (Gravity.TOP | Gravity.RIGHT)) {
                mPath.reset();
                mPath.moveTo(0, 0);
                //暂时只处理右上角的情况
                if (mFullTriangleDegree > 0) {
                    mPath.lineTo(size - mFullTriangleDegree * 2, 0);
                    RectF rectF = new RectF(size - mFullTriangleDegree * 2, 0, size, mFullTriangleDegree * 2);
                    mPath.addArc(rectF, -90, 90);
                    mPath.lineTo(size, mFullTriangleDegree * 2);
                    mPath.lineTo(size, size);
                    mPath.lineTo(0, 0);
                } else {
                    mPath.lineTo(size, 0);
                    mPath.lineTo(size, size);
                }

                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawTextWhenFill(size, DEFAULT_DEGREES, canvas, true);
            } else if (mGravity == (Gravity.BOTTOM | Gravity.LEFT)) {
                mPath.reset();
                mPath.moveTo(0, size);
                mPath.lineTo(0, 0);
                mPath.lineTo(size, size);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawTextWhenFill(size, DEFAULT_DEGREES, canvas, false);
            } else if (mGravity == (Gravity.BOTTOM | Gravity.RIGHT)) {
                mPath.reset();
                mPath.moveTo(size, size);
                mPath.lineTo(0, size);
                mPath.lineTo(size, 0);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawTextWhenFill(size, -DEFAULT_DEGREES, canvas, false);
            }
        } else {
            double delta = (textHeight + mPadding * 2) * Math.sqrt(2);
            if (mGravity == (Gravity.TOP | Gravity.LEFT)) {
                mPath.reset();
                mPath.moveTo(0, (float) (size - delta) - i);
                mPath.lineTo(0, size - i);
                mPath.lineTo(i, size);
                mPath.lineTo(i, size - (i * 2));
                mPath.lineTo(size - (i * 2), i);
                mPath.lineTo(size, i);
                mPath.lineTo(size - i, 0);
                mPath.lineTo((float) (size - delta) - i, 0);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawText(size, -DEFAULT_DEGREES, canvas, textHeight + i, true);
            } else if (mGravity == (Gravity.TOP | Gravity.RIGHT)) {
                mPath.reset();
                mPath.moveTo(0 + i, 0);
                mPath.lineTo((float) delta + i, 0);
                mPath.lineTo(size, (float) (size - delta - i));
                mPath.lineTo(size, size - i);
                mPath.lineTo(size - i, size);
                mPath.lineTo(size - i, size - (i * 2));
                mPath.lineTo(i * 2, i);
                mPath.lineTo(0, i);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawText(size, DEFAULT_DEGREES, canvas, textHeight + i, true);
            } else if (mGravity == (Gravity.BOTTOM | Gravity.LEFT)) {
                mPath.reset();
                mPath.moveTo(i, 0);
                mPath.lineTo(0, i);
                mPath.lineTo(0, (float) delta + i);
                mPath.lineTo((float) (size - delta) - i, size);
                mPath.lineTo(size - i, size);
                mPath.lineTo(size, size - i);
                mPath.lineTo(size - (i * 2), size - i);
                mPath.lineTo(i, i * 2);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawText(size, DEFAULT_DEGREES, canvas, textHeight + i, false);
            } else if (mGravity == (Gravity.BOTTOM | Gravity.RIGHT)) {
                mPath.reset();
                mPath.moveTo(0, size - i);
                mPath.lineTo(i, size);
                mPath.lineTo((float) delta + i, size);
                mPath.lineTo(size, (float) delta + i);
                mPath.lineTo(size, i);
                mPath.lineTo(size - i, 0);
                mPath.lineTo(size - i, i * 2);
                mPath.lineTo(i * 2, size - i);
                mPath.close();
                canvas.drawPath(mPath, mBackgroundPaint);

                drawText(size, -DEFAULT_DEGREES, canvas, textHeight + i, false);
            }
        }
    }

    private void drawText(int size, float degrees, Canvas canvas, float textHeight, boolean isTop) {
        canvas.save();
        canvas.rotate(degrees, size / 2f, size / 2f);
        float delta = isTop ? -(textHeight + mPadding * 2) / 2 : (textHeight + mPadding * 2) / 2;
        float textBaseY = size / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2 + delta;
        canvas.drawText(getContent(),
                getPaddingLeft() + (size - getPaddingLeft() - getPaddingRight()) / 2, textBaseY, mTextPaint);
        canvas.restore();
    }

    private void drawTextWhenFill(int size, float degrees, Canvas canvas, boolean isTop) {
        canvas.save();
        canvas.rotate(degrees, size / 2f, size / 2f);
        float delta = isTop ? -size / 4 : size / 4;
        float textBaseY = size / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2 + delta;
        canvas.drawText(getContent(),
                getPaddingLeft() + (size - getPaddingLeft() - getPaddingRight()) / 2, textBaseY, mTextPaint);
        canvas.restore();
    }

    private String getContent() {
        if (TextUtils.isEmpty(mTextContent)) {
            return "";
        }
        return mTextAllCaps ? mTextContent.toUpperCase() : mTextContent;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredWidth);
    }

    /**
     * 确定View宽度大小
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {//大小确定直接使用
            result = specSize;
        } else {
            int padding = getPaddingLeft() + getPaddingRight();
            mTextPaint.setColor(mTextColor);
            mTextPaint.setTextSize(mTextSize);
            float textWidth = mTextPaint.measureText(mTextContent + "");
            result = (int) ((padding + (int) textWidth) * Math.sqrt(2));
            //如果父视图的测量要求为AT_MOST,即限定了一个最大值,则再从系统建议值和自己计算值中去一个较小值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }

            result = Math.max((int) mMinSize, result);
        }

        return result;
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}