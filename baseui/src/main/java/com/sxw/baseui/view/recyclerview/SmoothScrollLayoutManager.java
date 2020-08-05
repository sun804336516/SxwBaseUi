package com.sxw.baseui.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

public class SmoothScrollLayoutManager extends LinearLayoutManager {
    private float calculateSpeedPerPixel;

    /**
     * 返回：滑过1px时经历的时间(ms)。
     *
     * @param context
     * @param calculateSpeedPerPixel
     */
    public SmoothScrollLayoutManager(Context context, float calculateSpeedPerPixel) {
        super(context);
        this.calculateSpeedPerPixel = calculateSpeedPerPixel;
    }

    public SmoothScrollLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    /**
                     * 返回：滑过1px时经历的时间(ms)
                     * @param displayMetrics
                     * @return
                     */
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        if (calculateSpeedPerPixel != 0) {
                            return calculateSpeedPerPixel;
                        }
                        return 150f / displayMetrics.densityDpi;
                    }
                };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}