package com.sxw.baseui.view.draglayout;

import android.support.v4.widget.ViewDragHelper;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 作者：sxw on 2019/8/2 17:20
 */
public class DragTimeUtils {
    private View mView;
    private float mMaxVelocity;
    private float mMinVelocity;
    private VelocityTracker mVelocityTracker;
    private ViewDragHelper.Callback mCallback;

    public DragTimeUtils(View view, ViewDragHelper.Callback mCallback) {
        this.mView = view;
        this.mCallback = mCallback;

        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        this.mMaxVelocity = (float) vc.getScaledMaximumFlingVelocity();
        this.mMinVelocity = (float) vc.getScaledMinimumFlingVelocity();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    public int computeSettleDuration(View child, int dx, int dy, int pointId) {
        return computeSettleDuration(child, dx, dy
                , (int) mVelocityTracker.getXVelocity(pointId)
                , (int) mVelocityTracker.getYVelocity(pointId));
    }

    private int computeSettleDuration(View child, int dx, int dy, int xvel, int yvel) {
        xvel = this.clampMag(xvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        yvel = this.clampMag(yvel, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int absDx = Math.abs(dx);
        int absDy = Math.abs(dy);
        int absXVel = Math.abs(xvel);
        int absYVel = Math.abs(yvel);
        int addedVel = absXVel + absYVel;
        int addedDistance = absDx + absDy;
        float xweight = xvel != 0 ? (float) absXVel / (float) addedVel : (float) absDx / (float) addedDistance;
        float yweight = yvel != 0 ? (float) absYVel / (float) addedVel : (float) absDy / (float) addedDistance;
        int xduration = this.computeAxisDuration(dx, xvel, this.mCallback.getViewHorizontalDragRange(child));
        int yduration = this.computeAxisDuration(dy, yvel, this.mCallback.getViewVerticalDragRange(child));
        return (int) ((float) xduration * xweight + (float) yduration * yweight);
    }

    private int clampMag(int value, int absMin, int absMax) {
        int absValue = Math.abs(value);
        if (absValue < absMin) {
            return 0;
        } else if (absValue > absMax) {
            return value > 0 ? absMax : -absMax;
        } else {
            return value;
        }
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        } else {
            int width = this.mView.getWidth();
            int halfWidth = width / 2;
            float distanceRatio = Math.min(1.0F, (float) Math.abs(delta) / (float) width);
            float distance = (float) halfWidth + (float) halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
            velocity = Math.abs(velocity);
            int duration;
            if (velocity > 0) {
                duration = 4 * Math.round(1000.0F * Math.abs(distance / (float) velocity));
            } else {
                float range = (float) Math.abs(delta) / (float) motionRange;
                duration = (int) ((range + 1.0F) * 256.0F);
            }

            return Math.min(duration, 600);
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f *= 0.47123894F;
        return (float) Math.sin((double) f);
    }
}
