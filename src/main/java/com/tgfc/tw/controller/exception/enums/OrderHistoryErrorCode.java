package com.tgfc.tw.controller.exception.enums;

import com.tgfc.tw.controller.exception.ErrorCode;

public enum OrderHistoryErrorCode implements ErrorCode {
    FLOOR_ID_NOT_SETTING("ORDERH00001");

    private String code;
    OrderHistoryErrorCode(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
