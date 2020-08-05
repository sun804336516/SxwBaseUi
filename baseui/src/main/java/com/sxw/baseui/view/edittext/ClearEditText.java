package com.sxw.baseui.view.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.sxw.baseui.R;


/**
 * 右侧显示清除按钮的EditText
 */
public class ClearEditText extends EditText implements TextWatcher {

    private Drawable mClearDrawable;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.mipmap.icon_edittext_clear);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);
        addTextChangedListener(this);
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClearDrawable != null) {
            if (event.getAction() == MotionEvent.ACTION_UP && hasFocus()) {
                if (isTouchInClearRect(event)) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 触摸位置是否在右侧清除框内
     *
     * @param event
     * @return
     */
    private boolean isTouchInClearRect(MotionEvent event) {
        return event.getX() >= (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                && event.getX() <= getWidth() - getPaddingRight();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (hasFocus()) {
            setClearIconVisible(text.length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * 是否显示右侧按钮
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1]
                , visible ? mClearDrawable : null
                , getCompoundDrawables()[3]);
    }

    /**
     * 设置抖动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }


    /**
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
