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
@Constraint(validatedBy = CustomEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    String message() default "invalid email";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
