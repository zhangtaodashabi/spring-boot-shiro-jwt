package com.yyy.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yyy.common.constants.Constant;
import com.yyy.common.pojo.entity.OperationLogDO;
import com.yyy.common.service.IOperationLogService;
import com.yyy.common.utils.ComUtil;
import com.yyy.common.utils.JWTUtil;
import com.yyy.common.annotation.Log;
import com.yyy.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description:记录日志切面
 * @Author: Mr.Liu
 * @Date: 2020/8/12
 */
@Slf4j
public class RecordLogAspect extends AbstractAspectManager {


    public RecordLogAspect(AspectApi aspectApi){
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        super.doHandlerAspect(pjp,method);
        return execute(pjp,method);
    }

    @Override
    @Async
    protected Object execute(ProceedingJoinPoint pjp, Method method) throws Throwable{
        Log log  = method.getAnnotation( Log.class );
        // 异常日志信息
        String actionLog = null;
        StackTraceElement[] stackTrace =null;
        // 是否执行异常
        boolean isException = false;
        try {
            return null;
        } catch ( Throwable throwable ) {
            isException = true;
            actionLog = throwable.getMessage();
            stackTrace = throwable.getStackTrace();
            throw throwable;
        } finally {
            // 日志处理
            logHandle( pjp , method , log , actionLog , isException,stackTrace );
        }
    }
    private void isEmptyOperationLogDO(String authorization, OperationLogDO operationLog){
        if(!ComUtil.isEmpty(authorization)){
            String username = JWTUtil.getUsername(authorization);
            operationLog.setUserName(username);
        }
    }

    private void isException(OperationLogDO operationLog,boolean isException,String actionLog,StackTraceElement[] stackTrace){
        if(isException){
            StringBuilder sb = new StringBuilder();
            sb.append(actionLog+" &#10; ");
            for (int i = 0; i < stackTrace.length; i++) {
                sb.append(stackTrace[i]+" &#10; ");
            }
            operationLog.setMessage(sb.toString());
        }
    }
    private boolean determineDatatype(OperationLogDO operationLog,Object object,boolean isJoint){
        if(object instanceof JSONObject){
            JSONObject parse = (JSONObject) JSONObject.parse(object.toString());
            operationLog.setActionArgs(parse.toString());
        }else if(object instanceof String
                || object instanceof Long
                || object instanceof Integer
                || object instanceof Double
                || object instanceof Float
                || object instanceof Byte
                || object instanceof Short
                || object instanceof Character){
            isJoint=true;
        }
        else if(object instanceof String []
                || object instanceof Long []
                || object instanceof Integer []
                || object instanceof Double []
                || object instanceof Float []
                || object instanceof Byte []
                || object instanceof Short []
                || object instanceof Character []){
            Object[] strs = (Object[])object;
            StringBuilder sbArray  =new StringBuilder();
            sbArray.append("[");
            for (Object str:strs) {
                sbArray.append(str.toString()+",");
            }
            sbArray.deleteCharAt(sbArray.length()-1);
            sbArray.append("]");
            operationLog.setActionArgs(sbArray.toString());
        }
        return isJoint;
    }

    private void logHandle (ProceedingJoinPoint joinPoint ,
                            Method method ,
                            Log log1 ,
                            String actionLog ,
                            boolean isException,
                            StackTraceElement[] stackTrace) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        IOperationLogService operationLogService = SpringContextUtils.getBean(IOperationLogService.class);
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String authorization = request.getHeader("token");
        OperationLogDO operationLog = new OperationLogDO();
        isEmptyOperationLogDO(authorization,operationLog);
        if(!ComUtil.isEmpty(authorization)){
            String username = JWTUtil.getUsername(authorization);
            operationLog.setUserName(username);
        }
        operationLog.setIp(getIpAddress(request));
        operationLog.setClassName(joinPoint.getTarget().getClass().getName() );
        operationLog.setLogDescription(log1.description());
        operationLog.setModelName(log1.modelName());
        operationLog.setAction(log1.action());
        isException(operationLog,isException,actionLog,stackTrace);
        operationLog.setMethodName(method.getName());
        operationLog.setSucceed(isException ? 2:1);
        Object[] args = joinPoint.getArgs();
        operationLog.setActionArgs(JSON.toJSONString(args));
//        StringBuilder sb = new StringBuilder();
//        boolean isJoint = false;
//        for (int i = 0; i < args.length; i++) {
//            isJoint = determineDatatype(operationLog,args[i],isJoint);
//        }
//        if(isJoint){
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            for (String key:parameterMap.keySet()) {
//                String[] strings = parameterMap.get(key);
//                for (String str:strings) {
//                    sb.append(key+"="+str+"&");
//                }
//            }
//            log.info("接口参数：{}",sb.toString());
//            operationLog.setActionArgs(sb.deleteCharAt(sb.length()-1).toString());
//        }
        log.info("日志对象，{}", JSON.toJSONString(operationLog));
        operationLogService.save(operationLog);
    }


    private  String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip+":"+request.getRemotePort();
    }
}
