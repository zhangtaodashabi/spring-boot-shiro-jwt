package com.yyy.common.filter;


import com.alibaba.fastjson.JSONObject;
import com.yyy.api.http.HttpResult;
import com.yyy.common.base.CodeEnum;
import com.yyy.common.constants.Constant;
import com.yyy.common.jwt.JWTToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;

/**
 * @program: hkpi
 * @description: JWT过滤器
 * @author: Mr.Liu
 * @create: 2020-07-14 16:23
 **/
@Slf4j
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 如果带有 token，则对 token 进行检查，否则直接通过
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        if(Constant.getMethodUrlSet().contains(requestURI)||requestURI.contains("swagger")){
            return true;
        }
        //如果请求头不存在token,则可能是执行登陆操作或是游客状态访问,直接返回true
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                return true;
            } catch (AuthenticationException e) {
                log.info(e.getMessage());
                responseError(request, response, CodeEnum.IDENTIFICATION_ERROR.getMsg());
            }
        }
         log.info("请求的url:{}",requestURI);
         String ip = request.getRemoteAddr();
         ip = determineType(ip,(HttpServletRequest) request);
         log.info("请求ip:{}",ip);
//         throw new HkpiException("用户未登录");
         responseError(request, response,CodeEnum.UNAUTHORIZED.getMsg());
         return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        // 从 http 请求头中取出 token
        String token = req.getHeader("token");
        return token != null;
    }


    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("token");
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
//        setUserBean(request, response, token);
        return true;
    }

    /**
     * 对跨域提供支持
     */
//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return super.preHandle(request, response);
//    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendRedirect("/unauthorized/" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String determineType(String ip,HttpServletRequest httpServletRequest){
        if (ip == null || ip.length() == 0 || Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 ||  Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 ||  Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 ||  Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 ||  Constant.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        return ip;
    }

//    private void setUserBean(ServletRequest request, ServletResponse response, JWTToken token) {
//        Object principal = SecurityUtils.getSubject().getPrincipal();
//        if(principal instanceof UserDO){
//            UserDO userBean =(UserDO)principal;
//            request.setAttribute("currentUser", userBean);
//        }
//    }

    /**
     * 非法url返回身份错误信息
     */
    private void responseError(ServletRequest request, ServletResponse response,String message) {
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), "utf-8"); Writer out = new BufferedWriter(outputStreamWriter)) {
            response.setContentType("application/json; charset=utf-8");
            out.write(JSONObject.toJSONString(HttpResult.error(401,message)));
            out.flush();
        } catch (IOException e) {
            log.info("错误信息1：{}", e.getMessage());
        }
    }
}
