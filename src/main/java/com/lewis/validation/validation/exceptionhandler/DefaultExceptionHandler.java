package com.lewis.validation.validation.exceptionhandler;

import com.lewis.validation.validation.i18n.I18nMessageResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date : 2021/8/11
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private I18nMessageResolver i18nMessageResolver;

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ExceptionDetail> exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("ex:{},message:{}", ex.getClass().getSimpleName(), ex.getMessage());
        }
        HttpStatus status = resolveResponseStatus(ex);
        logException(status, ex);
        String message = extractErrorMessage(ex);
        return exceptionResponse(status, message, ex, request, response);
    }

    private String extractErrorMessage(Exception ex) {
        String[] errMsgArgs = null;
        /*if (ex instanceof BaseException) {
            errMsgArgs = ((BaseException) ex).getErrMsgArgs();
        } else {
            errMsgArgs = null;
        }*/
        String message = i18nMessageResolver.getMessage(exceptionPropertyName(ex.getMessage()), (Object[]) errMsgArgs);
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

    private ResponseEntity<ExceptionDetail> exceptionResponse(HttpStatus status, String message,
                                                              Exception exception,
                                                              HttpServletRequest request, HttpServletResponse response) {
        ExceptionDetail e = ExceptionDetail.consExceptionInfo(status, message, response);
        if (isAlwaysRespondOk(request.getAttribute("always-respond-ok"))) {
            response.setHeader("X-Code", Integer.toString(status.value()));
            return new ResponseEntity<>(e, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(e, status);
        }
    }

    private static HttpStatus resolveResponseStatus(Exception ex) {
        ResponseStatus rs = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
        if (rs != null) {
            return rs.code();
        } else if (ex instanceof HttpMessageNotReadableException || ex instanceof MethodArgumentNotValidException) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        logException(status, ex);
        ExceptionDetail e = ExceptionDetail.consExceptionInfo(status, status.getReasonPhrase());
        if (isAlwaysRespondOk(request.getAttribute("always-respond-ok", RequestAttributes.SCOPE_REQUEST))) {
            headers.add("X-Code", Integer.toString(status.value()));
            e.setStatus(status.value());
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(e, headers, status);
    }


    private void logException(HttpStatus status, Exception ex) {
        if (status == null || status.is5xxServerError()) {
            log.error("exception handled by DefaultExceptionHandler: {}", ex.getMessage(), ex);
        } else {
            log.info("exception: {}", ex.getMessage());
        }
    }

    private boolean isAlwaysRespondOk(Object alwaysRespondOk) {
        return alwaysRespondOk != null && "true".equalsIgnoreCase(alwaysRespondOk.toString());
    }

}
