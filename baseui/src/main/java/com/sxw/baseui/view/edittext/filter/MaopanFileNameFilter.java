package com.sxw.baseui.view.edittext.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：sxw on 2019/6/18 10:57
 * 名字规则
 */
public class MaopanFileNameFilter implements InputFilter {

    private Pattern mPattern = Pattern.compile(FilterUtils.MaopanFileNameFilter);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(source);
        if (matcher.find()) {
            Log.e("MaopanFileNameFilter", "不能输入这些符号");
            return "";
        }
        return null;

    }
}
