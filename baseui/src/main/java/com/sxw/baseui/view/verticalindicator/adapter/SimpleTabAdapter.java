package com.sxw.baseui.view.verticalindicator.adapter;


import com.sxw.baseui.view.verticalindicator.DividerBean;
import com.sxw.baseui.view.verticalindicator.widget.TabView;

/**
 * Created by chqiu on 2017/1/20.
 */

public abstract class SimpleTabAdapter implements TabAdapter {
    @Override
    public abstract int getCount();

    @Override
    public TabView.TabBadge getBadge(int position) {
        return null;
    }

    @Override
    public TabView.TabIcon getIcon(int position) {
        return null;
    }

    @Override
    public TabView.TabTitle getTitle(int position) {
        return null;
    }

    @Override
    public int getBackground(int position) {
        return 0;
    }

    @Override
    public DividerBean getDividerBean(int position) {
        return null;
    }
}
