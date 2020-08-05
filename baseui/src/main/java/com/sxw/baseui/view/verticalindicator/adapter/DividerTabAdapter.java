package com.sxw.baseui.view.verticalindicator.adapter;


import com.sxw.baseui.R;
import com.sxw.baseui.utils.BaseUtils;
import com.sxw.baseui.view.verticalindicator.DividerBean;

/**
 * 作者：sxw on 2019/7/4 13:44
 */
public class DividerTabAdapter extends SimpleTabAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public DividerBean getDividerBean(int position) {
        return new DividerBean(BaseUtils.getApp().getResources().getDimensionPixelOffset(R.dimen.space_1), R.color.line_color);
    }
}
