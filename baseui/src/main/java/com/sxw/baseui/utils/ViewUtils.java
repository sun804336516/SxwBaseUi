package com.sxw.baseui.utils;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：sxw on 2019/8/7/007 13:53
 * 邮箱：sun91985415@163.com
 */
public class ViewUtils {

    public static String getString(int id) {
        return BaseUtils.getApp().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return BaseUtils.getApp().getString(id, formatArgs);
    }

    public static int getColor(int colorId) {
        return ContextCompat.getColor(BaseUtils.getApp(), colorId);
    }

    public static Drawable getDrawable(int drawableId) {
        return ContextCompat.getDrawable(BaseUtils.getApp(), drawableId);
    }

    public static int getDimenPixInt(int dimenId) {
        return BaseUtils.getApp().getResources().getDimensionPixelOffset(dimenId);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = BaseUtils.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        final float fontScale = BaseUtils.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        final float scale = BaseUtils.getApp().getResources().getDisplayMetrics().density;
        return pxValue / scale;
    }

    /**
     * 设置EditText的hint字体的大小,适用于hint跟text字体大小不一致的情况
     */
    public static void setEditTextHintSize(EditText editText, String hintText, int size) {
        SpannableString ss = new SpannableString(hintText);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss));
    }

    /**
     * 找到屏幕上RecyclerView开始跟结束的position
     *
     * @param recyclerView
     * @return
     */
    public static Point getRecyclerFirstLastPosition(@NonNull RecyclerView recyclerView) {
        if (recyclerView == null) {
            return new Point();
        } else {
            int firstPosition = 0, lastPosition = 0;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                firstPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                lastPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
                int[] firstVisiableCount = new int[manager.getSpanCount()];
                manager.findFirstCompletelyVisibleItemPositions(firstVisiableCount);
                int[] lastVisiableCount = new int[manager.getSpanCount()];
                manager.findLastCompletelyVisibleItemPositions(lastVisiableCount);
                if (firstVisiableCount.length > 0) {
                    firstPosition = firstVisiableCount[0];
                }
                if (lastVisiableCount.length > 0) {
                    lastPosition = lastVisiableCount[lastVisiableCount.length - 1];
                }
            }
            return new Point(firstPosition, lastPosition);
        }
    }

    /**
     * 获取带用id的子view
     *
     * @param view
     */
    public static List<View> getChildViewWithId(View view) {
        List<View> list = new ArrayList<>();
        if (null != view && view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<>();
            queue.add(viewGroup);
            while (!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                for (int i = 0; i < current.getChildCount(); i++) {
                    View childView = current.getChildAt(i);
                    if (childView instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    }
                    if (childView.getId() != View.NO_ID) {
                        list.add(childView);
                    }
                }
            }
        }
        return list;
    }
}
