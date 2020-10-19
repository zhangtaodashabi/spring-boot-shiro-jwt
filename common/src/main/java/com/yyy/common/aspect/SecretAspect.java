package com.yyy.common.aspect;

import com.alibaba.fastjson.JSON;
import com.yyy.api.http.HttpResult;
import com.yyy.common.annotation.Secret;
import com.yyy.common.utils.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @program: hospitalKpi
 * @description: Http传输加密
 * @author: Mr.Liu
 * @create: 2020-09-18 17:28
 **/
@Slf4j
public class SecretAspect extends AbstractAspectManager{


    public SecretAspect(AspectApi aspectApi) {
        super(aspectApi);
    }

    @Override
    public Object doHandlerAspect(ProceedingJoinPoint pjp, Method method) throws Throwable{
        super.doHandlerAspect(pjp,method);
        return execute(pjp,method);
    }

    @Override
    protected Object execute(ProceedingJoinPoint point, Method method) throws Throwable {
        HttpResult<String> result = null;

        // 获取被代理方法参数
        Object[] args = point.getArgs();

        // 获取被代理对象
        Object target = point.getTarget();
        

        try {
//            // 获取被代理方法
//            Method pointMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
            // 获取被代理方法上面的注解@Secret
            Secret secret = method.getAnnotation(Secret.class);
            // 被代理方法上没有，则说明@Secret注解在被代理类上
            if(secret==null){
                secret = target.getClass().getAnnotation(Secret.class);
            }

            if(secret!=null){
                // 获取注解上声明的加解密类
                Class<T> clazz = secret.value();
                // 获取注解上声明的加密参数名
                String encryptStrName = secret.encryptStrName();

                for (int i = 0; i < args.length; i++) {
                    // 如果是clazz类型则说明使用了加密字符串encryptStr传递的加密参数
                    if(clazz.isInstance(args[i])&&secret.isDecrypt()){
                        Object cast = clazz.cast(args[i]);      //将args[i]转换为clazz表示的类对象
                        // 通过反射，执行getEncryptStr()方法，获取加密数据
                        Method voMethod = clazz.getMethod(getMethedName(encryptStrName));
                        // 执行方法，获取加密数据
                        String encryptStr = (String) voMethod.invoke(cast);
                        // 加密字符串是否为空
                        if(StringUtils.isNotBlank(encryptStr)){
                            // 解密
                            String json = AESUtils.decrypt(encryptStr);
                            // 转换vo
                            args[i] = JSON.parseObject(json, (Type) args[i].getClass());
                        }
                    }
                    // 其他类型，比如基本数据类型、包装类型就不使用加密解密了
                }
            }

            // 执行请求
            result = (HttpResult) point.proceed(args);
            log.info("返回信息{}", JSON.toJSONString(result));
            if(HttpStatus.OK.value()==result.getCode()&&secret.isEncrypt()){
                // 获取返回值json字符串
                String jsonString = JSON.toJSONString(result.getData());
                // 加密data
                String s = AESUtils.encrypt(jsonString);
                result.setData(s);
                //加密message
                String message  = JSON.toJSONString(result.getMsg());
                result.setMsg(AESUtils.encrypt(message));
            }
        } catch (NoSuchMethodException e) {
            log.error("@Secret注解指定的类没有字段:encryptStr,或encryptStrName参数字段不存在");
        }
        return result;
    }

    // 转化方法名
    private String getMethedName(String name){
        String first = name.substring(0,1);
        String last = name.substring(1);
        first = StringUtils.upperCase(first);
        return "get" + first + last;
    }

}
