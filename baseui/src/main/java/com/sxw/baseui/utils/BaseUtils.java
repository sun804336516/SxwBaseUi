package com.sxw.baseui.utils;

import android.app.Application;

/**
 * 作者：孙贤武 on 2019/10/24 13:43
 */
public class BaseUtils {
    private static Application mApplication;

    public static void init(Application application) {
        mApplication = application;
    }

    public static Application getApp() {
        if (mApplication == null) {
            throw new IllegalArgumentException("need init in your application first!");
        }
        return mApplication;
    }
}
