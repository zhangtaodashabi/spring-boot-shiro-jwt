package com.yyy.common.utils.pagination;

import java.util.regex.Pattern;

/**
 * @Author: Mr.Liu
 * @Description:
 * @Date: 2019/5/29 10:20
 */
public class WafKit {
    private WafKit(){
        //私有构造
    }

	private static final Pattern SCRIPTPATTERN1 = Pattern.compile("<script>(.*?)</script>", 2);
	private static final Pattern SCRIPTPATTERN2 = Pattern.compile("</script>", 2);
	private static final Pattern SCRIPTPATTERN3 = Pattern.compile("<script(.*?)>", 42);
	private static final Pattern SCRIPTPATTERN4 = Pattern.compile("eval\\((.*?)\\)", 42);
	private static final Pattern SCRIPTPATTERN5 = Pattern.compile("<script>(.*?)</script>", 2);
	private static final Pattern SCRIPTPATTERN6 = Pattern.compile("javascript:", 2);
	private static final Pattern SCRIPTPATTERN7 = Pattern.compile("vbscript:", 2);
	private static final Pattern SCRIPTPATTERN8 = Pattern.compile("onload(.*?)=", 42);
    public static String stripXSS(String value) {
        String rlt = null;
        if (null != value) {

	        rlt = value.replaceAll("", "");
            rlt = SCRIPTPATTERN1.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN2.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN3.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN4.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN5.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN6.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN7.matcher(rlt).replaceAll("");
            rlt = SCRIPTPATTERN8.matcher(rlt).replaceAll("");
        }

        return rlt;
    }

    public static String stripSqlInjection(String value) {
        return null == value ? null : value.replaceAll("('.+--)|(--)|(%7C)", "");
    }

    public static String stripSqlXSS(String value) {
        return stripXSS(stripSqlInjection(value));
    }
}
