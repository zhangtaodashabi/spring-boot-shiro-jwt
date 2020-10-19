package com.yyy.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
* @Description:基本被装饰类,做一些公共处理
* @Author: Mr.Liu
* @Date: 2020/8/12
*/
public class AspectApiImpl implements AspectApi {

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable {
        return null;
    }
}
