package com.yyy.common.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.yyy.common.annotation.AccessLimit;
import com.yyy.common.constants.Constant;
import com.yyy.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
* @Description: 限流切面
* @Author: Mr.Liu
* @Date: 2020/8/12
*/
@Slf4j
public class AccessLimitAspect extends AbstractAspectManager{

    public AccessLimitAspect(AspectApi aspectApi){
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method)throws Throwable {
        super.doHandlerAspect(pjp,method);
        execute(pjp,method);
        return null;
    }

//    //添加速率.保证是单例的
//    private static RateLimiter rateLimiter = RateLimiter.create(1000);
//     //使用url做为key,存放令牌桶 防止每次重新创建令牌桶
//    private static  Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Override
    public Object execute(ProceedingJoinPoint pjp,Method method) throws Throwable{
        AccessLimit lxRateLimit = method.getAnnotation(AccessLimit.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 或者url(存在map集合的key)
        RateLimiter limiter;
        String url = request.getRequestURI();
        if (!Constant.getLimiterMap().containsKey(url)) {
            // 创建令牌桶
            Constant.setRateLimiter(RateLimiter.create(lxRateLimit.perSecond()));
            Constant.getLimiterMap().put(url, Constant.getRateLimiter());
            log.info("<<=================  请求{},创建令牌桶,容量{} 成功!!!",url,lxRateLimit.perSecond());
        }
        limiter = Constant.getLimiterMap().get(url);
        //获取令牌
        if (!limiter.tryAcquire(lxRateLimit.timeOut(), lxRateLimit.timeOutUnit())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("Error ---时间:{},获取令牌失败.", sdf.format(new Date()));
            throw new SystemException("服务器繁忙，请稍后再试。");
        }
        log.info("获取令牌成功,速率是:{}",limiter.getRate());
        return null;
    }
}
