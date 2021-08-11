package com.lewis.validation.validation.exceptionhandler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @date : 2021/8/11
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionDetail info = new ValidationExceptionDetail("invalid_param");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            info.addFieldError(error.getField(), error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            info.addObjectError(error.getObjectName(), error.getDefaultMessage());
        }
        info.setStatus(status.value());

        return handleExceptionInternal(ex, info, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionDetail info = new ValidationExceptionDetail("invalid_request");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            info.addFieldError(error.getField(), error.getDefaultMessage());
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            info.addObjectError(error.getObjectName(), error.getDefaultMessage());
        }
        info.setStatus(status.value());

        return handleExceptionInternal(ex, info, headers, status, request);
    }
}
