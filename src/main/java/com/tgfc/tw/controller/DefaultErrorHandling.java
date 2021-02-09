package com.tgfc.tw.controller;



import com.tgfc.tw.controller.exception.ErrorCodeException;
import com.tgfc.tw.entity.model.response.ErrorCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DefaultErrorHandling {
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandling.class);
    @Autowired
    private MessageSource messageSource;
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorCodeResponse unknowExceptionHandler(Exception exception) {
        logger.error(exception.getMessage(), exception);
        ErrorCodeResponse errorResponse = new ErrorCodeResponse();
        errorResponse.setErrorCode("774");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(ErrorCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorCodeResponse errorCodeExceptionHandler(ErrorCodeException exception) {
        logger.error(exception.getMessage(), exception);
        ErrorCodeResponse errorResponse = new ErrorCodeResponse();
        errorResponse.setErrorCode(exception.getErrorCode().getCode());
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setData(exception.getData());
        return errorResponse;
    }

}
