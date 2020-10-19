package com.yyy.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author GKQ
 * @Classname RegexUtils
 * @Description 正则校验工具类
 * @Date 2020/4/26
 */
public class RegexUtils {

    /**
     * 手机号
     */
    public static final String REGEX_MOBILE = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";

    /**
     * 身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    /**
     * 校验手机号
     */
    public static boolean checkMobile(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_MOBILE);
        return pattern.matcher(mobile).find();
    }

    /**
     * 校验身份证
     */
    public static boolean checkIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX_ID_CARD);
        return pattern.matcher(idCard).find();
    }


    public static boolean checkInfo(String info,String regex){
        if(StringUtils.isEmpty(info)){
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(info).find();
    }
}
