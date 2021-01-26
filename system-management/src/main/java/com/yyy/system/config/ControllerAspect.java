package com.yyy.system.config;


import com.yyy.common.annotation.*;
import com.yyy.common.aspect.*;
import com.yyy.common.exception.SystemException;
import com.yyy.common.utils.BeanValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;


import java.lang.reflect.Method;

/**
* @Description:
* @Author: Mr.Liu
* @Date: 2020/8/12
*/
@Aspect
@Configuration
@Slf4j
public class ControllerAspect {

    /**
     * 获取切点，不需要实现具体功能
     * */
    @Pointcut("execution(* com.yyy.system.controller..*(..))")
    public void aspect(){
        //获取切点的方法
    }

    @Pointcut("@annotation(com.yyy.common.annotation.ParamsCheck))")
    public void valid(){
        //获取切点的方法
    }

    @Around(value = "aspect()")
    public Object validationPoint(ProceedingJoinPoint pjp)throws Throwable{
        Method method = currentMethod(pjp,pjp.getSignature().getName());
        //创建被装饰者
        AspectApiImpl aspectApi = new AspectApiImpl();
        //是否需要验证参数
        if(method!=null){
            //是否需要限流
            if (Boolean.TRUE.equals(method.isAnnotationPresent(AccessLimit.class))) {
                new AccessLimitAspect(aspectApi).doHandlerAspect(pjp,method);
            }
            //是否需要拦截xss攻击
            if(Boolean.TRUE.equals(method.isAnnotationPresent(ParamXssPass.class ))){
                new ParamXssPassAspect(aspectApi).doHandlerAspect(pjp,method);
            }
            if(Boolean.TRUE.equals(method.isAnnotationPresent(RepeatSubmit.class))){
                new RepeatSubmitAspect(aspectApi).doHandlerAspect(pjp,method);
            }
            //是否需要记录日志
            if(Boolean.TRUE.equals(method.isAnnotationPresent(Log.class))){
                new RecordLogAspect(aspectApi).doHandlerAspect(pjp,method);
            }
            //是否要加密
            if(Boolean.TRUE.equals(method.isAnnotationPresent(Secret.class))){
                return new SecretAspect(aspectApi).doHandlerAspect(pjp,method);
            }
        }
        return  pjp.proceed(pjp.getArgs());
    }

    /**
     * 获取目标类的所有方法，找到当前要执行的方法
     */
    private Method currentMethod ( ProceedingJoinPoint joinPoint , String methodName ) {
        Method[] methods      = joinPoint.getTarget().getClass().getMethods();
        Method   resultMethod = null;
        for ( Method method : methods ) {
            if ( method.getName().equals( methodName ) ) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }

    @Around(value = "valid()")
    public Object validParam(ProceedingJoinPoint joinPoint) throws Throwable {
        // 判断是否需要校验
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ParamsCheck paramsCheckAnnotation = method.getAnnotation(ParamsCheck.class);
        if (paramsCheckAnnotation != null && paramsCheckAnnotation.ignore()) {
            return joinPoint.proceed();
        }
        /**
         * 参数校验
         */
        Object[] objects = joinPoint.getArgs();
        for (Object arg : objects) {
            if (arg == null) {
                break;
            }

            // 参数校验
            String validResult = BeanValidateUtil.validate(arg);
            if (validResult != null && validResult.length() > 0) {
                throw new SystemException(validResult);
            }
        }
        return joinPoint.proceed();
    }

}
