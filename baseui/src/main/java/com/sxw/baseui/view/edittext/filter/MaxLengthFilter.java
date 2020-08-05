package com.sxw.baseui.view.edittext.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 作者：sxw on 2019/3/13 14:55
 * 输入最大长度限制 中文2个  其他一个
 */
public class MaxLengthFilter extends InputFilter.LengthFilter {
    private int maxLength;

    public MaxLengthFilter(int max) {
        super(max);
        maxLength = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        int destLen = getCharacterNum(dest.toString()); //获取字符个数(一个中文算2个字符)
        int sourceLen = getCharacterNum(source.toString());
        if (destLen + sourceLen > maxLength) {
            return "";
        }
        return source;
    }

    /**
     * @param content
     * @return
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    private static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * @param s
     * @return
     * @description 返回字符串里中文字或者全角字符的个数
     */
    private static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

}
