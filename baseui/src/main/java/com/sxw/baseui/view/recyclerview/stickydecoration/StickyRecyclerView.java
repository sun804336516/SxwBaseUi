package com.sxw.baseui.view.recyclerview.stickydecoration;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gavin
 * date 2018/8/26
 */
public class StickyRecyclerView extends RecyclerView {

    private BaseDecoration mDecoration;

    public StickyRecyclerView(Context context) {
        super(context);
    }

    public StickyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (decor != null && decor instanceof BaseDecoration) {
            mDecoration = (BaseDecoration) decor;
        }
        super.addItemDecoration(decor);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mDecoration != null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDecoration.onEventDown(e);
                    break;
                case MotionEvent.ACTION_UP:
                    if (mDecoration.onEventUp(e)) {
                        return true;
                    }
                    break;
                default:
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}

