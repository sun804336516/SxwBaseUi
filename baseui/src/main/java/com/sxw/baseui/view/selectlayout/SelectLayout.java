package com.sxw.baseui.view.selectlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sxw.baseui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：孙贤武 on 2019/10/30 15:45
 */
public class SelectLayout extends LinearLayout {
    private int bgResId = -1;
    private List<Selected> mSelectedList;
    private SelectChangeListener mSelectChangeListener;

    public void setSelectChangeListener(SelectChangeListener selectChangeListener) {
        mSelectChangeListener = selectChangeListener;
    }

    public SelectLayout(Context context) {
        this(context, null);
    }

    public SelectLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectLayout);
        bgResId = ta.getResourceId(R.styleable.SelectLayout_child_background, -1);
        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        mSelectedList = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View view = getChildAt(i);
            if (view instanceof Selected) {
                mSelectedList.add((Selected) view);
                if (bgResId != -1) {
                    view.setBackgroundResource(bgResId);
                }
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = mSelectedList.indexOf(view);
                        setSelected(index);
                        if (mSelectChangeListener != null) {
                            mSelectChangeListener.onSelectChanged(SelectLayout.this, view, index);
                        }
                    }
                });
            }
        }
    }

    public void setSelected(int index) {
        if (index < 0 || index >= mSelectedList.size()) {
            return;
        }
        for (int i = 0, len = mSelectedList.size(); i < len; i++) {
            setSelected(mSelectedList.get(i), i == index);
        }
    }

    private void setSelected(Selected selected, boolean onSelected) {
        selected.onSelected(onSelected);
    }

    public interface SelectChangeListener {
        void onSelectChanged(SelectLayout selectLayout, View view, int position);
    }
}
