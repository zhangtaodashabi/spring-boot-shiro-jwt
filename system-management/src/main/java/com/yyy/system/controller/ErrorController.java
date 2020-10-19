package com.yyy.system.controller;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.yyy.api.http.HttpResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * @program: hospitalKpi
 * @description: 用于捕获filter中的异常
 * @author: Mr.Liu
 * @create: 2020-08-12 10:29
 **/
@RestController
@Api(value = "filter错误处理", description = "filter错误处理")
@Slf4j
public class ErrorController extends BasicErrorController {

    /**
     * 必须实现的一个构造方法
     **/
    public ErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
     * produces 设置返回的数据类型：application/json
     *
     * @param request 请求
     * @return 自定义的返回实体类
     */
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Override
    public ResponseEntity error(HttpServletRequest request) {
        Map<String, Object> body =
                getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        log.info(JSONUtil.toJsonPrettyStr(body));
        // 错误信息
        String message = body.get("message").toString();
        if (message.contains("用户未登录")) {
            return new ResponseEntity<>(JSON.toJSON(HttpResult.error(HttpStatus.UNAUTHORIZED.value(),"未授权的访问")), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(body);
    }

}