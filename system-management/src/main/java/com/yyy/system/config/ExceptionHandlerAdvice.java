package com.yyy.system.config;


import com.yyy.api.http.HttpResult;
import com.yyy.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public HttpResult<String> errorHandler(Exception ex) {
        log.error("接口出现严重异常：{}" + ex.getMessage(), ex);
        return HttpResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 捕捉 HkpiException
     *
     * @return
     */
    @ExceptionHandler({SystemException.class})
    private HttpResult<String> catchMedicaCareException(SystemException e) {
        return HttpResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 捕捉UnauthorizedException
     *
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public HttpResult<String> handleUnauthorized() {
        return HttpResult.error(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    /**
     * 捕捉shiro的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public HttpResult<String> handleShiroException(ShiroException e) {
        log.error("接口出现严重异常：{}" + e.getMessage(), e);
        return HttpResult.error(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public HttpResult<String> handleMethodArgumentNotValidException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        log.error("参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return HttpResult.error(fieldError.getDefaultMessage());
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult<String> validExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        log.error("参数校验异常:{}({})", fieldError.getDefaultMessage(), fieldError.getField());
        return HttpResult.error(fieldError.getDefaultMessage());
    }


    @ExceptionHandler
    @ResponseBody
    public HttpResult<String> handle(ConstraintViolationException exception) {
        log.error(String.valueOf(exception));
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation violation : violations) {
            builder.append(violation.getMessage());
            break;
        }
        return HttpResult.error("参数校验错误");
    }

}
