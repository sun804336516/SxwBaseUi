package com.sxw.baseui.view.shadow;

import android.text.TextUtils;

public class ShadowKey {

    private final String name;
    private final int width;
    private final int height;

    public ShadowKey(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShadowKey shadowKey = (ShadowKey) o;
        if (width != shadowKey.width) {
            return false;
        }
        if (height != shadowKey.height) {
            return false;
        }
        return TextUtils.equals(name, shadowKey.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
}