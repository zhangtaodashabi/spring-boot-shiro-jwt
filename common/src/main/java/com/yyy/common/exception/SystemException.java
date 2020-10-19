package com.yyy.common.exception;

import lombok.Data;

/**
 * 自定义异常
 *
 * @Author: Mr.Liu
 * @date Jan 12, 2019
 */
@Data
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String msg;
    private int code = 500;

    public SystemException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public SystemException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public SystemException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public SystemException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
