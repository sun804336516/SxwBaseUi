package com.sxw.baseui.view.edittext.filter;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：sxw on 2019/3/13 15:02
 * 输入用户名过滤   待添加  一些特殊字符
 */
public class NameFilter implements InputFilter {
    public static Pattern pattern = Pattern.compile(FilterUtils.InputTxtFilter);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            Log.e("NameFilter", "只能输入汉字,英文，数字");
            return "";
        }
        return null;
    }

}
