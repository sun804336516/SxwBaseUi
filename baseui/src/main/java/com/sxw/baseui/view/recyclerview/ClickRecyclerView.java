package com.sxw.baseui.view.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * sxw on 2019/6/12 15:40
 * 参考ListView实现点击事件的RecyclerView
 */
public class ClickRecyclerView extends BaseRecyclerView {
    private GestureDetector mGestureDetector;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Rect mTouchFrame;

    public ClickRecyclerView(Context context) {
        this(context, null);
    }

    public ClickRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (mOnItemClickListener != null) {
                    View view = point2View(e.getX(), e.getY());
                    if (view != null) {
                        mOnItemClickListener.onItemClick(ClickRecyclerView.this, getChildLayoutPosition(view));
                    }
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (mOnItemLongClickListener != null) {
                    View view = point2View(e.getX(), e.getY());
                    if (view != null) {
                        mOnItemLongClickListener.onItemLongClick(ClickRecyclerView.this, getChildLayoutPosition(view));
                    }
                }
            }
        });
    }

    /**
     * 设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置长点击事件
     *
     * @param onItemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    /**
     * 通过触摸点的位置找到当前的view
     *
     * @param x
     * @param y
     * @return
     */
    private View point2View(float x, float y) {
        if (mTouchFrame == null) {
            mTouchFrame = new Rect();
        }
        int count = getChildCount();
        for (int i = count - 1; i > -1; i--) {
            View view = getChildAt(i);
            if (view != null && view.getVisibility() == View.VISIBLE) {
                view.getHitRect(mTouchFrame);
                if (mTouchFrame.contains((int) x, (int) y)) {
                    return view;
                }
            }
        }
        return null;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    public interface OnItemLongClickListener {

        boolean onItemLongClick(View view, int position);

    }
}
