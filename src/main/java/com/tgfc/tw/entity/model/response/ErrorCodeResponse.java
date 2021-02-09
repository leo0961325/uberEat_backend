package com.tgfc.tw.entity.model.response;

/**
 * Created by tecoli on 2016/11/21.
 */
public class ErrorCodeResponse {
    private String errorCode;
    private String message;
    private Object data;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
