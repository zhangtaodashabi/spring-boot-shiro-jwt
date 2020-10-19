package com.yyy.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;


/**
 * @program: hkpi
 * @description: 装饰器模式
 * @author: Mr.Liu
 * @create: 2020-07-21 16:20
 **/
public interface AspectApi {

     Object doHandlerAspect(ProceedingJoinPoint pjp, Method method)throws Throwable;

}
