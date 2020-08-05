package com.sxw.baseui.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sxw.baseui.R;


/**
 * 作者：孙贤武 on 2018/1/25 0025 14:19
 * 邮箱：sun91985415@163.com
 * LIKE:The best ChaoMei
 */

public class ProgressTextView extends View {
    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int originalColor;
    private int gradientColor;
    private int textSize;
    private int round_radius;
    private Paint mPaint;
    private int default_originalColor;
    private int default_gradientColor;
    private int wid, hei;
    private int progress = 0, indexX = 0;
    private Rect mRect = new Rect(), textBound = new Rect();
    private String text = "";
    private Path mPath = new Path();
    private Paint.FontMetrics fontMetrics;

    public ProgressTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView);
        default_originalColor = ContextCompat.getColor(getContext(), R.color.blue_light);
        default_gradientColor = ContextCompat.getColor(getContext(), R.color.white);
        originalColor = typedArray.getColor(R.styleable.ProgressTextView_originalcolor, default_originalColor);
        gradientColor = typedArray.getColor(R.styleable.ProgressTextView_gradientcolor, default_gradientColor);
        textSize = typedArray.getDimensionPixelSize(R.styleable.ProgressTextView_textsize, getResources().getDimensionPixelOffset(R.dimen.textsize_14));
        round_radius = typedArray.getDimensionPixelSize(R.styleable.ProgressTextView_progress_round_radius, 0);
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
        fontMetrics = mPaint.getFontMetrics();
//        mPaint.setStrokeWidth(4);
    }

    public void setText(String text) {
        this.text = text;
        mPaint.getTextBounds(text, 0, text.length(), textBound);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wid = getMeasuredWidth();
        hei = getMeasuredHeight();
        mRect = new Rect(0, 0, wid, hei);
        RectF rectF = new RectF(0, 0, wid, hei);

        int radius = round_radius == 0 ? (hei >> 1) : round_radius;
        mPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        indexX = (int) (wid / 100f * progress);
        if (indexX > wid) {
            indexX = wid;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipPath(mPath);
        mPaint.setStyle(Paint.Style.FILL);
        float x = (wid - textBound.width()) >> 1;
        float y = mRect.centerY() - (fontMetrics.bottom + fontMetrics.top) / 2;

        mPaint.setColor(originalColor);
        canvas.save();
        canvas.drawText(text, x, y, mPaint);
        canvas.restore();

        canvas.save();
        mRect.set(0, 0, indexX, hei);
        canvas.clipRect(mRect);
        canvas.drawColor(originalColor);
        mPaint.setColor(gradientColor);
        canvas.drawText(text, x, y, mPaint);
        canvas.restore();
    }
}
