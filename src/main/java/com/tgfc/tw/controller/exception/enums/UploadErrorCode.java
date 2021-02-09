package com.tgfc.tw.controller.exception.enums;

import com.tgfc.tw.controller.exception.ErrorCode;

public enum UploadErrorCode implements ErrorCode {
    FILE_NOT_FOUND("UPLOAD0001"),
    IMAGE_FILE_NOT_FOUND("IMAGE0001"),
    FILE_NAME_INCORRECT("UPLOAD0002");

    private String code;
    UploadErrorCode(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
