package com.tgfc.tw.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

@Component
@Aspect
public class VoidToJsonHandler {

    private static Logger logger = LoggerFactory.getLogger(VoidToJsonHandler.class);

    @Around("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object deleteReq(ProceedingJoinPoint pjp) throws Throwable {
            return around(pjp);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public Object putReq(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp);
    }

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Signature signature = pjp.getSignature();
        Class returnType = ((MethodSignature) signature).getReturnType();
        logger.info("test:" + returnType);
        Object obj = pjp.proceed();
        if (returnType == Void.TYPE) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            response.setContentType("application/json");

            response.getWriter().print("{}");

            return new Object();
        }
        return obj;


    }

}
