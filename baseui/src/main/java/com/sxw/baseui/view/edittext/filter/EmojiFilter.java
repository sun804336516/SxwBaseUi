package com.sxw.baseui.view.edittext.filter;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 作者：sxw on 2019/6/13 14:51
 * 过滤emoji表情符号
 */
public class EmojiFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (containsEmoji(source.toString())) {
            return "";
        }
        return null;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * @param codePoint 比较的单个字符
     * @return
     */
    private boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }
}
