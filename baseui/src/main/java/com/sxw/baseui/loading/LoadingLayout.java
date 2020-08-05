package com.sxw.baseui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxw.baseui.R;

import java.util.HashMap;
import java.util.Map;


public class LoadingLayout extends FrameLayout {
    public interface OnInflateListener {
        void onInflate(View inflated);
    }

    int mEmptyImage;
    CharSequence mEmptyText;

    int mErrorImage;
    CharSequence mErrorText;
    OnClickListener mRetryClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRetryListener != null) {
                mRetryListener.onClick(v);
            }
        }
    };

    LayoutInflater mInflater;

    OnClickListener mRetryListener;

    OnInflateListener mOnEmptyInflateListener;
    OnInflateListener mOnErrorInflateListener;

    int mEmptyResId = NO_ID, mLoadingResId = NO_ID, mErrorResId = NO_ID;
    int mContentId = NO_ID;

    int currentId = -1000;

    Map<Integer, View> mLayouts = new HashMap<>();


    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayoutStyle);
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, NO_ID);
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);

        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, NO_ID);
        mErrorText = a.getString(R.styleable.LoadingLayout_llRetryText);

        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout.loading_layout_vertical_empty);
        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout.loading_layout_loading);
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout.loading_layout_vertical_error);
        a.recycle();
    }

    int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        if (isInEditMode()) {
            //编辑器视图实时预览
            showContent();
        } else {
            showLoading();
        }
    }

    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }

    public LoadingLayout setLoading(@LayoutRes int id) {
        if (mLoadingResId != id) {
            remove(mLoadingResId);
            mLoadingResId = id;
        }
        return this;
    }

    public LoadingLayout setEmpty(@LayoutRes int id) {
        if (mEmptyResId != id) {
            remove(mEmptyResId);
            mEmptyResId = id;
        }
        return this;
    }

    public LoadingLayout setError(@LayoutRes int id) {
        if (mErrorResId != id) {
            remove(mErrorResId);
            mErrorResId = id;
        }
        return this;
    }

    public LoadingLayout setOnEmptyInflateListener(OnInflateListener listener) {
        mOnEmptyInflateListener = listener;
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts.get(mEmptyResId));
        }
        return this;
    }

    public LoadingLayout setOnErrorInflateListener(OnInflateListener listener) {
        mOnErrorInflateListener = listener;
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts.get(mErrorResId));
        }
        return this;
    }

    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        image(mEmptyResId, R.id.empty_image, mEmptyImage);
        return this;
    }

    public LoadingLayout setEmptyText(String value) {
        mEmptyText = value;
        text(mEmptyResId, R.id.empty_text, mEmptyText);
        return this;
    }

    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        image(mErrorResId, R.id.error_image, mErrorImage);
        return this;
    }

    public LoadingLayout setErrorText(String value) {
        mErrorText = value;
        text(mErrorResId, R.id.error_text, mErrorText);
        return this;
    }

    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }

//    public LoadingLayout setTextColor(@ColorInt int color) {
//        mTextColor = color;
//        return this;
//    }
//    public LoadingLayout setTextSize(@ColorInt int dp) {
//        mTextColor = dp2px(dp);
//        return this;
//    }
//    public LoadingLayout setButtonTextColor(@ColorInt int color) {
//        mButtonTextColor = color;
//        return this;
//    }
//    public LoadingLayout setButtonTextSize(@ColorInt int dp) {
//        mButtonTextColor = dp2px(dp);
//        return this;
//    }
//    public LoadingLayout setButtonBackground(Drawable drawable) {
//        mButtonBackground = drawable;
//        return this;
//    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(mContentId);
    }

    private void show(int layoutId) {
        if (currentId == layoutId) {
            return;
        }
        for (View view : mLayouts.values()) {
            view.setVisibility(INVISIBLE);
        }
        layout(layoutId).setVisibility(VISIBLE);
        currentId = layoutId;
    }

    private void remove(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            View vg = mLayouts.remove(layoutId);
            removeView(vg);
        }
    }

    private View layout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, this, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);

        if (layoutId == mEmptyResId) {
            ImageView img = (ImageView) layout.findViewById(R.id.empty_image);
            if (img != null && mEmptyImage != NO_ID) {
                img.setImageResource(mEmptyImage);
            }
            TextView view = (TextView) layout.findViewById(R.id.empty_text);
            if (view != null) {
                view.setText(mEmptyText);
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(layout);
            }
        } else if (layoutId == mErrorResId) {
            ImageView img = (ImageView) layout.findViewById(R.id.error_image);
            if (img != null) {
                if (mErrorImage != NO_ID) {
                    img.setImageResource(mErrorImage);
                }
            }
            layout.setOnClickListener(mRetryClickListener);

            TextView txt = (TextView) layout.findViewById(R.id.error_text);
            if (txt != null) {
                txt.setText(mErrorText);
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(layout);
            }
        }
        return layout;
    }

    private void text(int layoutId, int ctrlId, CharSequence value) {
        if (mLayouts.containsKey(layoutId)) {
            TextView view = (TextView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setText(value);
            }
        }
    }

    private void image(int layoutId, int ctrlId, int resId) {
        if (mLayouts.containsKey(layoutId)) {
            ImageView view = (ImageView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }

    public Map<Integer, View> getLayouts() {
        return mLayouts;
    }
}
