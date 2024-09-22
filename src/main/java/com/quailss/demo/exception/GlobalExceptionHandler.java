package com.quailss.demo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${FRONEND_URL}") private String frontendUrl;

    // 로그인 자격 증명이 없는 경우
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ModelAndView handleAuthenticationException(HttpServletRequest request) {
        // 로그인 페이지로 리다이렉트
        return new ModelAndView("redirect:"+frontendUrl+"/login");
    }

    // EntityNotFoundException과 그 하위 클래스 처리
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}

