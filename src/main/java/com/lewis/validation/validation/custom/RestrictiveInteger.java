package com.lewis.validation.validation.custom;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date : 2021/8/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = RestrictiveEnumValidator.class)
public @interface RestrictiveInteger {

    Class<?> applyEnum();

    String message() default "invalid param";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
