package com.tgfc.tw.controller.exception;

/**
 * Created by tecoli on 2016/11/21.
 */
public class ErrorCodeException extends Exception {

    private ErrorCode errorCode;
    private Object data;

    public ErrorCodeException(ErrorCode errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;

    }

    public ErrorCodeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;

    }
    public ErrorCodeException(ErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;

    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
