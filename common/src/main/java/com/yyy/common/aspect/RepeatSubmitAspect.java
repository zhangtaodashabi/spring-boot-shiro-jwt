package com.yyy.common.aspect;



import com.yyy.common.exception.SystemException;
import com.yyy.common.utils.RepeatSubmitUtil;
import com.yyy.common.utils.SpringContextUtils;
import com.yyy.common.utils.pagination.HttpKit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.redis.cache.RedisCache;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @program: hip-dcp
 * @description: 防止表单重复提交注解（如果使用传输加密最好使用类似hash加密方式，不然此注解无效）
 * @author: Mr.Liu
 * @create: 2020-11-06 16:26
 **/
public class RepeatSubmitAspect extends AbstractAspectManager{
    public RepeatSubmitAspect(AspectApi aspectApi) {
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        super.doHandlerAspect(pjp,method);
        return execute(pjp,method);
    }

    @Override
    protected Object execute(ProceedingJoinPoint pjp, Method method) throws Throwable {
        HttpServletRequest request =  HttpKit.getRequest();
        RedisCache redisCache = SpringContextUtils.getBean(RedisCache.class);
        if (RepeatSubmitUtil.isRepeatSubmit(request,redisCache,pjp,method)) {
           throw new SystemException("请勿重复提交");
        }
        return null;
    }
}
