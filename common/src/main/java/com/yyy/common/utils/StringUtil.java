package com.yyy.common.utils;


import com.yyy.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liugh
 * @since on 2018/5/8.
 */
@Slf4j
public class StringUtil {

//    public static String pin(String chinese) throws Exception {
//        String pinyin = "";
//        HanyuPinyinOutputFormat pinyinOutputFormat = new HanyuPinyinOutputFormat();
//        pinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//        pinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//        String[] pinyinArray = null;
//        for(char ch : chinese.toCharArray()){
//            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch,pinyinOutputFormat);
//            pinyin += ComUtil.isEmpty(pinyinArray) ? ch : pinyinArray[0];
//        }
//        return pinyin;
//    }

    /**
     * 获取方法中指定注解的value值返回
     * @param method 方法名
     * @param validationParamValue 注解的类名
     * @return
     */
    public static String getMethodAnnotationOne(Method method, String validationParamValue) {
        String retParam =null;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                String str = parameterAnnotations[i][j].toString();
                if(str.indexOf(validationParamValue) >=0){
                    retParam = str.substring(str.indexOf("=")+1,str.indexOf(")"));
                }
            }
        }
        return retParam;
    }

    public static boolean isValidURLAddress(String url) {
        String pattern = "([h]|[H])([t]|[T])([t]|[T])([p]|[P])([s]|[S]){0,1}://([^:/]+)(:([0-9]+))?(/\\S*)*";
        return url.matches(pattern);
    }
    /**
     * 将utf-8编码的汉字转为中文
     * @author zhaoqiang
     * @param str
     * @return
     */
    public static String utf8Decoding(String str){
        String result = str;
        try
        {
            result = URLDecoder.decode(str, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            log.info("utf8Decoding错误信息1：{}",e.getMessage());
        }
        return result;
    }

    public static boolean checkEmail(String email) {
        if (ComUtil.isEmpty(email)) {
            return false;
        }
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            log.info("checkEmail错误信息2：{}",e.getMessage());
            flag = false;
        }
        return flag;
    }
    /**
     * 验证手机号码，11位数字，1开通，第二位数必须是3456789这些数字之一 *
     * @param mobileNumber
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        boolean flag = false;
        try {
            // Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Pattern regex = Pattern.compile(Constant.PHONE_CHECK);
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            log.info("checkMobileNumber错误信息3：{}",e.getMessage());
            flag = false;
        }
        return flag;
    }

    public static boolean checkWorkTimeAndBirthday(String workTime,String birthday){
        try {
           Integer workTimeInteger =   Integer.parseInt(workTime);
           Integer birthdayInteger = Integer.parseInt(birthday);
           return birthdayInteger<workTimeInteger;
        }catch (Exception e){
            return false;
        }
    }
    //获取两个String集合1多出2的
    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<>();
        if(CollectionUtils.isEmpty(list1)){
            return null;
        }
        if(CollectionUtils.isEmpty(list2)){
            return list1;
        }
        a:for(int i =0;i<list1.size();i++){
            for (int j=0;j<list2.size();j++){
                if(list1.get(i).equals(list2.get(j))){
                    continue a;
                }
            }
            list.add(list1.get(i));
        }
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
    }
}
