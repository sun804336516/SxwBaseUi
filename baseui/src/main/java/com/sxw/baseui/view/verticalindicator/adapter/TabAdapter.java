package com.sxw.baseui.view.verticalindicator.adapter;


import com.sxw.baseui.view.verticalindicator.DividerBean;
import com.sxw.baseui.view.verticalindicator.widget.TabView;

/**
 * @author chqiu
 * Email:qstumn@163.com
 */
public interface TabAdapter {
    int getCount();

    TabView.TabBadge getBadge(int position);

    TabView.TabIcon getIcon(int position);

    TabView.TabTitle getTitle(int position);

    int getBackground(int position);

    DividerBean getDividerBean(int position);
}
