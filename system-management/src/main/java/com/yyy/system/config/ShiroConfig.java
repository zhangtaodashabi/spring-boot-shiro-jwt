package com.yyy.system.config;

/**
 * @program: hkpi
 * @description: shiro 配置文件
 * @author: Mr.Liu
 * @create: 2020-07-14 16:20
 **/

import com.yyy.common.annotation.Pass;
import com.yyy.common.constants.Constant;
import com.yyy.common.filter.JwtFilter;
import com.yyy.common.utils.ComUtil;
import com.yyy.common.utils.SpringContextUtils;
import com.yyy.common.utils.StringUtil;
import com.yyy.system.shiro.CustomRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.reflections.Reflections;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Filter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Mr.Liu
 * @description Shiro配置类
 */
@Configuration
@Slf4j
public class ShiroConfig {
    /**
     * 先走 filter ，然后 filter 如果检测到请求头存在 token，则用 token 去 login，走 Realm 去验证
     */
    @Bean
    public ShiroFilterFactoryBean factory(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        // 访问401和404页面不通过我们的Filter
        //通过http://127.0.0.1:9527/druid/index.html 访问
        filterRuleMap.put("/druid/**", "anon");
        //放行webSocket
        filterRuleMap.put("/websocket/*", "anon");
        //放行swagger
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/swagger-resources", "anon");
        filterRuleMap.put("/v2/api-docs", "anon");
        filterRuleMap.put("/webjars/springfox-swagger-ui/**", "anon");
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
        filterRuleMap.putAll(getUrlAndMethodSet());
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }


    private Map<String, String> getUrlAndMethodSet(){
        String scanPackage ="com.wanwei.system.controller";
        String contextPath ="/system";
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Controller.class);
        classesList.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        Map<String, String> filterRuleMap = new LinkedHashMap<>();
        for (Class<?> clazz :classesList) {
            String[] classUrl ={};
            if(!ComUtil.isEmpty(clazz.getAnnotation(RequestMapping.class))){
                classUrl=clazz.getAnnotation(RequestMapping.class).value();
            }
            Method[] methods = clazz.getMethods();
            for (Method method:methods) {
                getHavePassUrl(method, classUrl,contextPath,filterRuleMap);
            }
        }
        Constant.getMethodUrlSet().addAll(filterRuleMap.keySet());
        log.info("@Pass:"+ filterRuleMap.keySet());
        return filterRuleMap;
    }

    private void getHavePassUrl(Method method, String[] classUrl, String contextPath, Map<String, String> filterRuleMap){
            if(method.isAnnotationPresent(Pass.class)){
                StringBuilder sb  =new StringBuilder();
                String baseUrl="";
                if(!ComUtil.isEmpty(method.getAnnotation(PostMapping.class))){
                    baseUrl =this.getBaseUrl(1,method,classUrl,contextPath,sb);
                }else if(!ComUtil.isEmpty(method.getAnnotation(GetMapping.class))){
                    baseUrl =this.getBaseUrl(2,method,classUrl,contextPath,sb);
                }else if(!ComUtil.isEmpty(method.getAnnotation(DeleteMapping.class))){
                    baseUrl =this.getBaseUrl(3,method,classUrl,contextPath,sb);
                }else if(!ComUtil.isEmpty(method.getAnnotation(PutMapping.class))){
                    baseUrl =this.getBaseUrl(4,method,classUrl,contextPath,sb);
                }else {
                    baseUrl =this.getBaseUrl(0,method,classUrl,contextPath,sb);
                }
                if(!ComUtil.isEmpty(baseUrl)){
                    filterRuleMap.put(baseUrl,"anon");
                }
            }

    }
    private String getBaseUrl(int requestType,Method method,String[] classUrl, String contextPath,StringBuilder sb){
        String[] url;
        switch (requestType){
            case 1:{
                url=method.getAnnotation(PostMapping.class).value();
                if(ComUtil.isEmpty(url)){
                    url = method.getAnnotation(PostMapping.class).path();
                }
                break;
            }
            case 2:{
                url=method.getAnnotation(GetMapping.class).value();
                if(ComUtil.isEmpty(url)){
                    url = method.getAnnotation(GetMapping.class).path();
                }
                break;
            }
            case 3:{
                url=method.getAnnotation(DeleteMapping.class).value();
                if(ComUtil.isEmpty(url)){
                    url = method.getAnnotation(DeleteMapping.class).path();
                }
                break;
            }
            case 4:{
                url=method.getAnnotation(PutMapping.class).value();
                if(ComUtil.isEmpty(url)){
                    url=method.getAnnotation(PutMapping.class).path();
                }
                break;
            }
            default:{
                url=method.getAnnotation(RequestMapping.class).value();
            }
        }
        return getRequestUrl(classUrl, url, sb,contextPath);
    }

    private String  getRequestUrl(String[] classUrl, String[] methodUrl, StringBuilder sb,String contextPath) {
        if(!ComUtil.isEmpty(contextPath)){
            sb.append(contextPath);
        }
        if(!ComUtil.isEmpty(classUrl)){
            for (String url:classUrl) {
                sb.append(url+"/");
            }
        }
        for (String url:methodUrl) {
            sb.append(url);
        }
        if(sb.toString().endsWith("/")){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString().replaceAll("/+", "/");
    }


    /**
     * 注入 securityManager
     */
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置自定义 realm.

//        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        securityManager.setRealm(customRealm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 添加注解支持
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用MD5算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 散列的次数，比如散列两次，相当于md5(md5(""))
        hashedCredentialsMatcher.setHashIterations(1);
        // 表示是否存储散列后的密码为16进制，需要和生成密码时的一样，默认是base64
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }
}

