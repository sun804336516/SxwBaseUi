/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sxw.baseui.view.draglayout;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by cgspine on 2018/3/22.
 */

public class LiveDragLinearLayout extends LinearLayout {
    ViewDragHelper viewDragHelper;
    private Point mPoint = new Point();
    /*拖动的时候禁止layout  否则会出现View错位*/
    private boolean isDraging = false;
    private OnPlayCallback mOnPlayCallback;
    private DragTimeUtils mDragTimeUtils;
    private View mReleasedView;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mReleasedView.setTranslationZ(0);
            }
            mReleasedView.setClickable(true);
            isDraging = false;
        }
    };

    public void setOnPlayCallback(OnPlayCallback onPlayCallback) {
        mOnPlayCallback = onPlayCallback;
    }

    public LiveDragLinearLayout(Context context) {
        this(context, null);
    }

    public LiveDragLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, mCallback);
        mDragTimeUtils = new DragTimeUtils(this, mCallback);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isDraging) {
            super.onLayout(changed, l, t, r, b);
        }
        Log.d("Drag", "onLayout");
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

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (!child.isEnabled()) {
                return false;
            }
//                return false;
//                return child.getId() == R.id.iv_courseware
//                        || child.getId() == R.id.iv_teacher
//                        || child.getId() == R.id.iv_cause
//                        || child.getId() == R.id.iv_student;
            return !(child instanceof TextView);
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
            /*正在拖动的view置于顶层*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                capturedChild.setTranslationZ(10);
            }
            capturedChild.setClickable(false);
            mPoint = new Point(capturedChild.getLeft(), capturedChild.getTop());
            isDraging = true;
        }

        @Override
        public void onViewReleased(@NonNull final View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            mReleasedView = releasedChild;
            if (mOnPlayCallback != null) {
                mOnPlayCallback.canPlay(releasedChild);
            }

            viewDragHelper.settleCapturedViewAt(mPoint.x, mPoint.y);
            invalidate();

            int dx = mPoint.x - releasedChild.getLeft();
            int dy = mPoint.y - releasedChild.getTop();
            int duration = mDragTimeUtils.computeSettleDuration(releasedChild, dx, dy, viewDragHelper.getActivePointerId());

            mHandler.sendEmptyMessageDelayed(1, duration);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(1);
    }

    public interface OnPlayCallback {
        void canPlay(View dragView);
    }

}
