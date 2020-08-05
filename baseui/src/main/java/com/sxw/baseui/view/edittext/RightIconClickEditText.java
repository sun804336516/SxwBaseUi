package com.sxw.baseui.view.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.sxw.baseui.R;


/**
 * 右侧显示清除按钮的EditText
 */
public class RightIconClickEditText extends EditText {

    private Drawable mRightClickDrawable;
    private OnRightIconClick mOnRightIconClick;

    public void setOnRightIconClick(OnRightIconClick onRightIconClick) {
        mOnRightIconClick = onRightIconClick;
    }

    public RightIconClickEditText(Context context) {
        this(context, null);
    }

    public RightIconClickEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public RightIconClickEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mRightClickDrawable = getCompoundDrawables()[2];
        if (mRightClickDrawable == null) {
            mRightClickDrawable = getResources().getDrawable(R.drawable.icon_search);
        }
        mRightClickDrawable.setBounds(0, 0, mRightClickDrawable.getIntrinsicWidth(), mRightClickDrawable.getIntrinsicHeight());
        setClearIconVisible();
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRightClickDrawable != null) {
            if (event.getAction() == MotionEvent.ACTION_UP && hasFocus()) {
                if (isTouchInClearRect(event)) {
                    if (mOnRightIconClick != null) {
                        mOnRightIconClick.onClick();
                        return true;
                    }
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
        return event.getX() >= (getWidth() - getPaddingRight() - mRightClickDrawable.getIntrinsicWidth())
                && event.getX() <= getWidth() - getPaddingRight();
    }


    /**
     * 是否显示右侧按钮
     */
    protected void setClearIconVisible() {
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1]
                , mRightClickDrawable
                , getCompoundDrawables()[3]);
    }

    public interface OnRightIconClick {

        void onClick();
    }
}
