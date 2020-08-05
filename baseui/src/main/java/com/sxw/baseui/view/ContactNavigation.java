package com.sxw.baseui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sxw.baseui.utils.ViewUtils;


/**
 * 导航字母控件  类似通讯录右侧
 */
public class ContactNavigation extends View {

    /*绘制的列表导航字母*/
    private String[] words = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    /*字母画笔*/
    private Paint wordsPaint;
    /*字母背景画笔*/
    private Paint bgPaint;
    /*每一个字母的宽度*/
    private int itemWidth;
    /*每一个字母的高度*/
    private int itemHeight;
    /*手指按下的字母索引*/
    private int touchIndex = 0;

    private OnWordsChangeListener listener;

    public ContactNavigation(Context context) {
        super(context);
        init();
    }

    public ContactNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        wordsPaint = new Paint();
        wordsPaint.setColor(Color.parseColor("#000000"));
        wordsPaint.setAntiAlias(true);
        wordsPaint.setTextSize(ViewUtils.dp2px( 11));
        wordsPaint.setTypeface(Typeface.DEFAULT_BOLD);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        //使得边距好看一些
        int height = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
        itemHeight = height / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wordWidth;
        float wordX, wordY, baseLine;
        Paint.FontMetrics fontMetrics;
        Rect rect = new Rect();

        for (int i = 0; i < words.length; i++) {
            /*获取文字的rect范围*/
            wordsPaint.getTextBounds(words[i], 0, 1, rect);
            wordWidth = rect.width();
            fontMetrics = wordsPaint.getFontMetrics();
            wordX = (itemWidth - wordWidth) >> 1;
            wordY = (i + 0.5f) * itemHeight + getPaddingTop();

            /*计算baseLine*/
            baseLine = wordY - (fontMetrics.bottom - fontMetrics.top) / 2;
            /*绘制字母*/
            canvas.drawText(words[i], wordX, baseLine, wordsPaint);
        }
    }

    /**
     * 当手指触摸按下的时候改变字母背景颜色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                //获得我们按下的是那个索引(字母)
                int index = (int) (y / itemHeight);
                if (index != touchIndex) {
                    touchIndex = index;
                }
                //防止数组越界
                if (listener != null && 0 <= touchIndex && touchIndex <= words.length - 1) {
                    //回调按下的字母
                    listener.wordsChange(words[touchIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //手指抬起,不做任何操作
                break;
        }
        return true;
    }

    /**
     * 设置当前按下的是那个字母
     * @param word
     */
    public void setTouchIndex(String word) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase(word)) {
                touchIndex = i;
                invalidate();
                return;
            }
        }
    }

    public interface OnWordsChangeListener {
        void wordsChange(String words);
    }

    /**
     * 设置手指按下字母改变监听
     * @param listener
     */
    public void setOnWordsChangeListener(OnWordsChangeListener listener) {
        this.listener = listener;
    }
}
