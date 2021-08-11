package com.lewis.validation.validation.custom;


import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @date : 2021/8/11
 */
public class CustomEmailValidator implements ConstraintValidator<Email, String> {

    private static final String REGEX_EMAIL = "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || PATTERN_EMAIL.matcher(value).matches();
    }
}
