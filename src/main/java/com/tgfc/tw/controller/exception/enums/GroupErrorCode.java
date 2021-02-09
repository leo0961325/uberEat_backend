package com.tgfc.tw.controller.exception.enums;

import com.tgfc.tw.controller.exception.ErrorCode;

public enum GroupErrorCode implements ErrorCode {
    GROUP_NOT_FOUND("GROUPD0001");

    private String code;
    GroupErrorCode(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
