package com.sxw.baseui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;

/**
 * 作者：孙贤武 on 2020/7/7 10:06
 */
public class IndicatorLayout extends LinearLayout {
    private Drawable mIndicatorSelectDrawable;
    private Drawable mIndicatorUnSelectDrawable;
    private int mIndicatorInterval;
    private OnItemClickListener mOnItemClickListener;

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorLayout);

        mIndicatorInterval = mTypedArray.getDimensionPixelOffset(
                R.styleable.IndicatorLayout_indicator_Interval, ViewUtils.dp2px(5));
        int indicatorSelectRes = mTypedArray.getResourceId(
                R.styleable.IndicatorLayout_indicator_SelectRes, R.drawable.bg_indicator_select);
        int indicatorUnSelectRes = mTypedArray.getResourceId(
                R.styleable.IndicatorLayout_indicator_UnSelectRes, R.drawable.bg_indicator_unselect);
        mIndicatorSelectDrawable = context.getResources().getDrawable(indicatorSelectRes);
        mIndicatorUnSelectDrawable = context.getResources().getDrawable(indicatorUnSelectRes);
        mTypedArray.recycle();

        setGravity(Gravity.CENTER);
        setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        setDividerDrawable(getDividerDrawable(mIndicatorInterval));

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void initIndicator(int count) {
        removeAllViews();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                addView(imageView, lp);

                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(v, finalI);
                        }
                    }
                });
            }
        }

    }

    public void updateIndicator(int currentItem) {
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                ImageView view = (ImageView) getChildAt(i);
                if (i == currentItem) {
                    view.setImageDrawable(mIndicatorSelectDrawable);
                } else {
                    view.setImageDrawable(mIndicatorUnSelectDrawable);
                }
            }
        }
    }

    private Drawable getDividerDrawable(int interval) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(Color.TRANSPARENT);
        drawable.setIntrinsicWidth(interval);
        return drawable;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
