package com.yyy.common.utils;

import com.yyy.common.constants.Constant;
import com.yyy.common.exception.SystemException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author GKQ
 * @Classname NumberUtils
 * @Description 数字工具类
 * @Date 2020/4/14
 */
public class NumberUtils {

    /**
     * 验证字符串是否为数字
     *
     * @param str
     * @return boolean
     */
    public static boolean isInteger(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile(Constant.REGX_STRING_OF_NUMBER);
        return pattern.matcher(str).matches();
    }
    /** 
    * @Description: 判断是不是double 
    * @Param:  
    * @return:  
    * @Author: Mr.Liu 
    * @Date: 2020/8/7
    */
    public static boolean isNumeric(Object str){
        if (ToolUtil.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile(Constant.REGX_STRING_OF_DOUBLE_NUMBER);
        String result = String.valueOf(str);
        return pattern.matcher(result).matches();
    }


    /**
     * double类型的树相除保留2位
     *
     * @param numerator   分子
     * @param denominator 分母
     * @return 结果保留2位
     */
    public static BigDecimal divide(double numerator, double denominator) {
        if (Objects.equals(denominator, 0.0)) {
            throw new SystemException("分母不能为0（ / by zero）");
        }
        BigDecimal num = BigDecimal.valueOf(numerator);
        BigDecimal den = BigDecimal.valueOf(denominator);
        return num.divide(den, 2, RoundingMode.HALF_UP);
    }


    /**
     * double类型的树相加保留2位
     *
     * @param item 需要相加的项
     * @return 结果保留2位
     */
    public static BigDecimal add(double... item) {
        return BigDecimal.valueOf(Arrays.stream(item).sum()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * @author: liulinchuan
     * @description: 设置默认值
     * @date: 2020/8/17 14:45
     * @param i:
     * @return: java.lang.Integer
     */
    public static Integer defaultValue(Integer i, int defaultValue) {
        return i == null ? Integer.valueOf(defaultValue) : i;
    }

    /**
     * @author: liulinchuan
     * @description: 设置默认值
     * @date: 2020/8/17 14:47
     * @param d:
     * @return: java.lang.Double
     */
    public static Double defaultValue(Double d, double defaultValue) {
        return d == null ? Double.valueOf(defaultValue) : d;
    }



}
