package com.sxw.baseui.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sxw.baseui.R;


/**
 * 作者：孙贤武 on 2020/5/25 09:18
 */
public class LoadingImageView extends LoadingLayout {
    private ImageView mImageView;
    private OnContentClickListener mOnContentClickListener;

    public void setOnContentClickListener(OnContentClickListener onContentClickListener) {
        mOnContentClickListener = onContentClickListener;
    }

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.loadingimageview_content_layout, this, true);
        mImageView = findViewById(R.id.iv_loading);

        setLoading(R.layout.loading_layout_simple_loading);
        setError(R.layout.loading_layout_simple_error);
        setEmpty(R.layout.loading_layout_simple_empty);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingImageView);
        int resourceId = ta.getResourceId(R.styleable.LoadingImageView_LoadingImageView_src, R.mipmap.icon_arrow_right);
        mImageView.setImageResource(resourceId);
        ta.recycle();

        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContentClickListener != null) {
                    mOnContentClickListener.onClick(v);
                }
            }
        });
    }

    public void setImageResource(int resourceId) {
        showContent();
        if (mImageView != null) {
            mImageView.setImageResource(resourceId);
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        showContent();
        if (mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        showContent();
        if (mImageView != null) {
            mImageView.setImageDrawable(drawable);
        }
    }

    public interface OnContentClickListener {
        void onClick(View view);
    }
}
