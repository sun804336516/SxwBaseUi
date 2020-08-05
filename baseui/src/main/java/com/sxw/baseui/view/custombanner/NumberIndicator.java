package com.sxw.baseui.view.custombanner;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.sxw.baseui.R;
import com.sxw.baseui.utils.ViewUtils;

public class NumberIndicator extends TextView {

    public NumberIndicator(Context context) {
        super(context);
        setTextColor(Color.WHITE);
        setTextSize(14);
        setBackgroundResource(R.drawable.text_indicator_bg);
        int padding = ViewUtils.dp2px(5);
        setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证TextIndicator的宽高一致(正方形)
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
