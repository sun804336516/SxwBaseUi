package com.sxw.baseui.interf;

/**
 * 作者：sxw on 2019/6/26 15:22
 */
public interface DialogPopWindowCallBack<T> {
    void onCancel();

    void onSure(T t);
}
