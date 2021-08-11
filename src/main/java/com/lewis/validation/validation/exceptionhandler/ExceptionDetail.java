package com.lewis.validation.validation.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ExceptionDetail {
    // http status
    private Integer status;
    // error code
    private String error;
    // error message
    private String message;
    private String tid;
    private String newTid;
    private String sid;
    private Long timestamp;
    // optional, some extension info
    private Object extension;

    private static final String TRACE_ID_NAME = "X-B3-TraceId";
    private static final String SPAN_ID_NAME = "X-B3-SpanId";

    public static ExceptionDetail consExceptionInfo(HttpStatus status, String message, HttpServletResponse response) {
        ExceptionDetail info = new ExceptionDetail();
        info.setStatus(status.value());
        info.setError(status.getReasonPhrase());
        info.setMessage(message);
        info.setTid(response.getHeader(TRACE_ID_NAME));
        info.setNewTid(response.getHeader("traceId"));
        info.setSid(response.getHeader(SPAN_ID_NAME));
        info.setTimestamp(new Date().getTime());
        return info;
    }

    public static ExceptionDetail consExceptionInfo(HttpStatus status, String message) {
        ExceptionDetail info = new ExceptionDetail();
        info.setStatus(status.value());
        info.setError(status.getReasonPhrase());
        info.setMessage(message);
        info.setTimestamp(new Date().getTime());
        return info;
    }
}
