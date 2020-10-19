package com.yyy.common.utils.pagination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Mr.Liu
 * @Description:
 * @Date: 2019/5/29 10:26
 */
public class NamingStrategyUtils {

    /**
     * 匹配下划线
     */
    private static final Pattern PATTERN_UNDERLINE = Pattern.compile("_(\\w)");

    /**
     * 匹配大写
     */
    private static final Pattern PATTERN_UPPERCASE = Pattern.compile("[A-Z]");


    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String toCamel(String str) {
        Matcher matcher = PATTERN_UNDERLINE.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return toCamel(sb.toString());
    }


    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String toUnderline(String str) {
        Matcher matcher = PATTERN_UPPERCASE.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return toUnderline(sb.toString());
    }

}
