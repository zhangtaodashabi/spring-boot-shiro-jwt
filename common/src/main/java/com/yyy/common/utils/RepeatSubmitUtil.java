package com.yyy.common.utils;

import com.alibaba.fastjson.JSON;
import com.yyy.common.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: hip-dcp
 * @description: 重复提交工具类
 * @author: Mr.Liu
 * @create: 2020-11-06 16:40
 **/
@Slf4j
public class RepeatSubmitUtil {

    public static final String REPEAT_PARAMS = "repeatParams";

    public static final String REPEAT_TIME = "repeatTime";


    /**
     * 间隔时间，单位:秒 默认10秒
     *
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    private static  int intervalTime = 10;


    public static boolean isRepeatSubmit(HttpServletRequest request, RedisCache redisCache, ProceedingJoinPoint pjp, Method method) {
        Object[] args = pjp.getArgs();
        String nowParams = JSON.toJSONString(args);
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());
        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        // 唯一值（没有消息头则使用请求地址）
        String token = request.getHeader("token");
        if (ToolUtil.isEmpty(token)) {
            token = url;
        }
        // 唯一标识（指定key + 消息头）
        String cache_repeat_key = Constant.REPEAT_SUBMIT_KEY + token;

        Object sessionObj = redisCache.getCacheObject(cache_repeat_key);
        if (sessionObj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (RepeatSubmitUtil.compareParams(nowDataMap, preDataMap) && RepeatSubmitUtil.compareTime(nowDataMap, preDataMap)) {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put(url, nowDataMap);
        redisCache.setCacheObject(cache_repeat_key, cacheMap, RepeatSubmitUtil.intervalTime, TimeUnit.SECONDS);
        return false;
    }

    /**
     * 判断参数是否相同
     */
    private static boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private static boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1 - time2) < (RepeatSubmitUtil.intervalTime * 1000)) {
            return true;
        }
        return false;
    }
}
