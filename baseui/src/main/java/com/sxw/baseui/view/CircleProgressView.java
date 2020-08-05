package com.sxw.baseui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;

public class CircleProgressView extends View {
    private static final String TAG = "CircleProgressBar";

    private int mMaxProgress = 100;

    private int mProgress = 1;

    private int mCircleLineStrokeWidth = 8;

    private final int mTxtStrokeWidth = 2;

    // 画圆所在的距形区域
    private RectF mRectF;

    private Paint mPaint;


    private String mTxtHint1;

    private String mTxtHint2;
    private int mWidth;
    private int mHeight;
    private int bgColor = 0xffededed;
    private int ProgressColor = 0xff00afec;

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRectF = new RectF();
        mPaint = new Paint();
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mCircleLineStrokeWidth = ViewUtils.dp2px( 4);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        bgColor = ContextCompat.getColor(context, R.color.progressbgcolor);
        ProgressColor = ContextCompat.getColor(context, R.color.progresscolor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = this.getMeasuredWidth();
        mHeight = this.getMeasuredHeight();
        mWidth = Math.min(mWidth, mHeight);
        mRectF.left = mCircleLineStrokeWidth;
        mRectF.top = mCircleLineStrokeWidth;
        mRectF.right = mWidth - mCircleLineStrokeWidth;
        mRectF.bottom = mHeight - mCircleLineStrokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        // 绘制圆圈，进度条背景
        mPaint.setColor(bgColor);
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(ProgressColor);
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

//        // 绘制进度文案显示
//        mPaint.setStrokeWidth(mTxtStrokeWidth);
//        String text = mProgress + "%";
//        int textHeight = height / 4;
//        mPaint.setTextSize(textHeight);
//        int textWidth = (int) mPaint.measureText(text, 0, text.length());
//        mPaint.setStyle(Paint.Style.FILL);
//        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);
////
//        if (!TextUtils.isEmpty(mTxtHint1)) {
//            mPaint.setStrokeWidth(mTxtStrokeWidth);
//            text = mTxtHint1;
//            textHeight = height / 8;
//            mPaint.setTextSize(textHeight);
//            mPaint.setColor(Color.rgb(0x99, 0x99, 0x99));
//            textWidth = (int) mPaint.measureText(text, 0, text.length());
//            mPaint.setStyle(Paint.Style.FILL);
//            canvas.drawText(text, width / 2 - textWidth / 2, height / 4 + textHeight / 2, mPaint);
//        }
//
//        if (!TextUtils.isEmpty(mTxtHint2)) {
//            mPaint.setStrokeWidth(mTxtStrokeWidth);
//            text = mTxtHint2;
//            textHeight = height / 8;
//            mPaint.setTextSize(textHeight);
//            textWidth = (int) mPaint.measureText(text, 0, text.length());
//            mPaint.setStyle(Paint.Style.FILL);
//            canvas.drawText(text, width / 2 - textWidth / 2, 3 * height / 4 + textHeight / 2, mPaint);
//        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }

    public String getmTxtHint1() {
        return mTxtHint1;
    }

    public void setmTxtHint1(String mTxtHint1) {
        this.mTxtHint1 = mTxtHint1;
    }

    public String getmTxtHint2() {
        return mTxtHint2;
    }

    public void setmTxtHint2(String mTxtHint2) {
        this.mTxtHint2 = mTxtHint2;
    }
}