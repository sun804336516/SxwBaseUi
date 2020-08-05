package com.sxw.baseui.view.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;


/**
 * RecyclerView垂直方向分割线
 * <p>
 * getItemViewType==0 才绘制
 */
public class BaseVerticalDecoration extends RecyclerView.ItemDecoration {
    private Drawable drawable;
    private float leftMargin;
    private float rightMargin;

    public BaseVerticalDecoration() {
        this.leftMargin = 0;
        this.rightMargin = 0;
        drawable = ViewUtils.getDrawable(R.drawable.list_decoration);

    }

    public BaseVerticalDecoration(Drawable drawable, float leftMargin, float rightMargin) {
        this.drawable = drawable;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
    }

    /**
     * itemView绘制之后绘制，这部分UI盖在itemView上面
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            drawVertical((LinearLayoutManager) layoutManager, adapter, c, parent);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    private void drawVertical(LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, Canvas c, RecyclerView parent) {
        int left = (int) (parent.getPaddingLeft() + leftMargin);
        int right = (int) (parent.getWidth() - parent.getPaddingRight() - rightMargin);
        int childCount = parent.getChildCount();
        int end = childCount;
        for (int i = 0; i < end; i++) {
            View child = parent.getChildAt(i);
            if (canDrawDecoration(parent, child, (LinearLayoutManager) layoutManager, adapter)) {
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + lp.bottomMargin;
                int bottom = top + drawable.getIntrinsicHeight();
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(c);
            }
        }
    }

    /**
     * 设置itemView上下左右的间距
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter != null && layoutManager != null && layoutManager instanceof LinearLayoutManager) {
            if (canDrawDecoration(parent, view, (LinearLayoutManager) layoutManager, adapter)) {
                outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
            }
        }
    }

    /**
     * 能绘制Decoration的条件
     *
     * @param layoutManager
     * @param adapter
     * @param parent
     * @param child
     * @return
     */
    protected boolean canDrawDecoration(RecyclerView parent, View child, LinearLayoutManager layoutManager, RecyclerView.Adapter adapter) {
        try {
            int position = layoutManager.getPosition(child);
            return position < adapter.getItemCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
