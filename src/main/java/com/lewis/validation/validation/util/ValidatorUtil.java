package com.lewis.validation.validation.util;


import com.lewis.validation.validation.custom.EachConstraint;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ValidatorUtil {

    public static <A extends Annotation> Annotation getEachConstraint(A constraintAnnotation) {
        Class<? extends Annotation> eachAType = constraintAnnotation.annotationType();

        if (eachAType.isAnnotationPresent(EachConstraint.class)) {
            Class constraintClass = eachAType.getAnnotation(EachConstraint.class).validateAs();

            Map<String, Object> attributes = readAllAttributes(constraintAnnotation);


            AnnotationDescriptor descriptor = new AnnotationDescriptor(constraintClass, attributes);
            //AnnotationDescriptor descriptor = new AnnotationDescriptor(constraintClass);

            return AnnotationFactory.create(descriptor);

        } else {
            throw new IllegalArgumentException(String.format(
                    "%s is not annotated with @EachConstraint and doesn't declare 'value' of type Annotation[] either.",
                    eachAType.getName()));
        }
    }

    public static Map<String, Object> readAllAttributes(Annotation annotation) {
        Map<String, Object> attributes = new HashMap<>();

        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                Object value = method.invoke(annotation);
                attributes.put(method.getName(), value);

            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return attributes;
    }

}
