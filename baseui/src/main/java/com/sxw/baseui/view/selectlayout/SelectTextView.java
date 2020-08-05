package com.sxw.baseui.view.selectlayout;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 作者：孙贤武 on 2019/10/30 15:58
 */
public class SelectTextView extends AppCompatTextView implements Selected {
    public SelectTextView(Context context) {
        this(context, null);
    }

    public SelectTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Selected) {
            if (this.getThisId() != NO_ID && ((Selected) obj).getThisId() != NO_ID) {
                return this.getThisId() == ((Selected) obj).getThisId();
            }
        }
        return super.equals(obj);
    }

    @Override
    public int getThisId() {
        return getId();
    }

    @Override
    public void onSelected(boolean selected) {
        setSelected(selected);
    }
}
