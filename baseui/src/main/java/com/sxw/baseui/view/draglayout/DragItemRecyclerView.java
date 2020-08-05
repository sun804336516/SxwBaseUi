package com.sxw.baseui.view.draglayout;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sxw.baseui.view.recyclerview.BaseRecyclerView;


/**
 * 作者：sxw on 2019/7/18 09:14
 * Recyclerview Item可以拖动
 */
public class DragItemRecyclerView extends BaseRecyclerView {

    private ViewDragHelper viewDragHelper;
    private Point mPoint;
    private OnDragReleaseListener mOnDragReleaseListener;

    public void setOnDragReleaseListener(OnDragReleaseListener onDragReleaseListener) {
        mOnDragReleaseListener = onDragReleaseListener;
    }

    public DragItemRecyclerView(Context context) {
        this(context, null);
    }

    public DragItemRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragItemRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (!child.isEnabled()) {
                    return false;
                }
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return child.getMeasuredHeight();
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return child.getMeasuredWidth();
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                /*正在拖动的view置于顶层,参考ItemHelper*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    capturedChild.setTranslationZ(10);
                }
                mPoint = new Point(capturedChild.getLeft(), capturedChild.getTop());
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d("Drag1", left + "," + top);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    releasedChild.setTranslationZ(0);
                }
                if (mPoint != null) {
                    viewDragHelper.settleCapturedViewAt(mPoint.x, mPoint.y);
                    invalidate();
                }
                Rect rect = new Rect();
                releasedChild.getGlobalVisibleRect(rect);
                if (mOnDragReleaseListener != null) {
                    mOnDragReleaseListener.onDragRelease(rect);
                }
            }
        });
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return viewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public interface OnDragReleaseListener {
        void onDragRelease(Rect rect);
    }
}
