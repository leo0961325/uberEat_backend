package com.tgfc.tw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.tw.entity.model.response.advice.CommonResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException {
        CommonResponse customResponse = new CommonResponse();
        customResponse.setSuccess(false);
        customResponse.setData("請登入後再使用");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), customResponse);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
