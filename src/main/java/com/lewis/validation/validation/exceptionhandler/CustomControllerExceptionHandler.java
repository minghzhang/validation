package com.lewis.validation.validation.exceptionhandler;

import com.lewis.validation.validation.exceptionhandler.exception.ResourceNotFoundException;
import com.lewis.validation.validation.i18n.I18nMessageResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date : 2021/8/11
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private I18nMessageResolver i18nMessageResolver;

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ExceptionDetail> handleBadRequestException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = extractErrorMessage(ex.getCause() != null ? ex.getCause() : ex);
       /* if (ex instanceof BadRequestException) {
            errorMessage = getErrorMessage(ex, errorMessage);
        } else if (ex.getCause() != null && ex.getCause() instanceof BadRequestException) {
            errorMessage = getErrorMessage((Exception) ex.getCause(), errorMessage);
        }
        */
        ExceptionDetail info = ExceptionDetail.consExceptionInfo(HttpStatus.BAD_REQUEST, errorMessage, response);
        HttpStatus retStatus = "true".equals(request.getAttribute("always-respond-ok")) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(retStatus).body(info);
    }

    private String getErrorMessage(Exception ex, String errorMessage) {
        /*BadRequestException badRequestException = BadRequestException.class.cast(ex);
        String[] errMsgArgs = badRequestException.getErrMsgArgs();
        if (ArrayUtils.isNotEmpty(errMsgArgs)) {
            errorMessage = MessageFormat.format(errorMessage, errMsgArgs);
        }*/
        return errorMessage;
    }

    private String extractErrorMessage(Throwable ex) {
        String message = i18nMessageResolver.getMessage(exceptionPropertyName(ex.getMessage()));
        if (StringUtils.isBlank(message)) {
            /*if (ex instanceof BaseException) {
                message = ex.getMessage();
            } else {
                message = "An error was encountered when processing your request";
            }*/
            message = "An error was encountered when processing your request";

        }
        return message;
    }

    private static String exceptionPropertyName(String e) {
        return "exception." + e;
    }


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
