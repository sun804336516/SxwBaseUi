package com.sxw.baseui.view.recyclerview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * sxw on 2019/6/12 16:54
 */
public class BaseRecyclerView extends RecyclerView {
    private static final String TAG = "BaseRecyclerView";

    private LoadmoreListener mLoadmoreListener;
    private boolean isLoadMoreing = false;
    private Handler mHandler = new Handler();

    public void setLoadmoreListener(LoadmoreListener loadmoreListener) {
        mLoadmoreListener = loadmoreListener;
    }

    public void setLoadMoreing(boolean loadMoreing) {
        isLoadMoreing = loadMoreing;
    }

    public boolean isLoadMoreing() {
        return isLoadMoreing;
    }

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAnimationDuration(200);
        setOverScrollMode(OVER_SCROLL_NEVER);


        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean canScrollVertically = canScrollVertically(1);
                if (!canScrollVertically && !isLoadMoreing && mLoadmoreListener != null) {
                    isLoadMoreing = true;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLoadmoreListener.onLoadMore();
                        }
                    });
                }
            }
        });

    }

    public void setAnimationDuration(long duration) {
        RecyclerViewItemAnimator myItemAnimator = new RecyclerViewItemAnimator();
        myItemAnimator.setChangeDuration(duration);
        myItemAnimator.setAddDuration(duration);
        myItemAnimator.setMoveDuration(duration);
        myItemAnimator.setRemoveDuration(duration);
        myItemAnimator.setSupportsChangeAnimations(duration == 0 ? false : true);
        setItemAnimator(myItemAnimator);
    }

    public void setAnimationDuration(long duration, int type) {
        RecyclerViewItemAnimator myItemAnimator = new RecyclerViewItemAnimator();
        myItemAnimator.setChangeDuration(duration);
        myItemAnimator.setAddDuration(duration);
        myItemAnimator.setMoveDuration(duration);
        myItemAnimator.setRemoveDuration(duration);
        myItemAnimator.setSupportsChangeAnimations(duration == 0 ? false : true);
        setItemAnimator(myItemAnimator);
    }

    @Override
    protected void onDetachedFromWindow() {
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.onDetachedFromRecyclerView(this);
        }
        super.onDetachedFromWindow();
    }

    public interface LoadmoreListener {
        void onLoadMore();
    }

}
