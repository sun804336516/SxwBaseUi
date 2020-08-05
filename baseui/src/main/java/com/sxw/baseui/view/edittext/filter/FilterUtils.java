package com.sxw.baseui.view.edittext.filter;

/**
 * 作者：sxw on 2019/6/27 09:57
 */
public class FilterUtils {

    /**
     * 输入值只能是数字&字母&中文
     * [^]非
     */
    public static final String InputTxtFilter = "[^a-zA-Z0-9\\u4E00-\\u9FA5_]";
    /**
     * 文件名字规则
     * //名称不可以以空格“ ”或句点“.”字符结尾。
     * //名称不可以包含以下特殊字符：\ / : * ? " < > |
     * ^[]开头
     */
    public static final String MaopanFileNameFilter = "([/:*?<>|\\\\\"]{1,255})|([.]{1,255}$)|(^[.]{1,255})";
}
