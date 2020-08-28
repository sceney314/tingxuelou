package com.tingxuelou.www.provider.exceptions;

/**
 * 运行时服务异常
 * <p>
 * Date: 2020-08-03 00:12
 * Copyright (C), 2015-2020
 */
public class ServiceException extends RuntimeException{
    private Integer errorCode;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
