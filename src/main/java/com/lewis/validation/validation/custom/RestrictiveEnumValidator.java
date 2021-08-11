package com.lewis.validation.validation.custom;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @date : 2021/8/11
 */
@Slf4j
public class RestrictiveEnumValidator implements ConstraintValidator<RestrictiveInteger, Integer> {

    private RestrictiveInteger integerEnum;

    private static List<String> valueNameList = new ArrayList();

    static {
        valueNameList.add("code");
        valueNameList.add("value");
    }

    @Override
    public void initialize(RestrictiveInteger constraintAnnotation) {
        this.integerEnum = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        Class<?> aClass = integerEnum.applyEnum();
        List<Field> allFieldsList = FieldUtils.getAllFieldsList(aClass);
        List<Field> validFields = allFieldsList.stream().filter(field -> valueNameList.contains(field.getName())).collect(Collectors.toList());
        for (Field field : allFieldsList) {
            if (!Objects.equals(field.getType(), aClass)) {
                continue;
            }
            try {
                Object o = field.get(null);
                for (Field validField : validFields) {
                    validField.setAccessible(true);
                    Object fieldValue = validField.get(o);
                    if (Objects.equals(fieldValue, value)) {
                        return true;
                    }
                }

            } catch (IllegalAccessException e) {
                log.error("RestrictiveEnumValidator exception:", e);
            }
        }

        return false;
    }
}
