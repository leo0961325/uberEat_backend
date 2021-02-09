package com.tgfc.tw.controller.exception.enums;

import com.tgfc.tw.controller.exception.ErrorCode;

public enum AuthenticationErrorCode implements ErrorCode {

    MUST_LOGIN("AUTH001"),
    PERMISSION_DENIED("AUTH002"),
    LOGIN_FAILED("AUTH003");

    private String code;

    AuthenticationErrorCode(String code){
        this.code=code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
