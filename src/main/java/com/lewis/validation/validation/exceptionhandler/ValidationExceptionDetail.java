package com.lewis.validation.validation.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ValidationExceptionDetail {
    private Integer status;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ObjectError> objectErrors;

    public ValidationExceptionDetail(String message) {
        this.message = message;
    }

    public void addFieldError(String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldError(field, message));
    }

    public void addObjectError(String objectName, String message) {
        if (objectErrors == null) {
            objectErrors = new ArrayList<>();
        }
        objectErrors.add(new ObjectError(objectName, message));
    }

    @Data
    private static class FieldError {
        private String field;
        private String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    @Data
    private static class ObjectError {
        private String objectName;
        private String message;

        public ObjectError(String objectName, String message) {
            this.objectName = objectName;
            this.message = message;
        }
    }

}
