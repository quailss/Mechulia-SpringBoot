package com.quailss.demo.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${FRONTEND_URL}") private String frontendUrl;

    // 로그인 자격 증명이 없는 경우
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Void> handleAuthenticationException(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(frontendUrl + "/login")); // 프론트엔드의 로그인 페이지로 리다이렉트

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER); // 303 See Other
    }

    // 접근 권한이 없는 경우 (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "You do not have permission to access this resource: " + ex.getMessage();
    }

    // EntityNotFoundException과 그 하위 클래스 처리
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}

