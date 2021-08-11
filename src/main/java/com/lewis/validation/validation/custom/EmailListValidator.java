package com.lewis.validation.validation.custom;

import com.lewis.validation.validation.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;

/**
 * @date : 2021/8/11
 */
@Slf4j
public class EmailListValidator implements ConstraintValidator<EmailList, Collection<String>> {

    private Annotation constraint;

    @Override
    public void initialize(EmailList constraintAnnotation) {
        this.constraint = ValidatorUtil.getEachConstraint(constraintAnnotation);
    }

    @Override
    public boolean isValid(Collection<String> collection, ConstraintValidatorContext context) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }

        int index = 0;
        for (Iterator<String> it = collection.iterator(); it.hasNext(); index++) {
            String element = it.next();

            if (StringUtils.isBlank(element)) {
                return false;
            }

            CustomEmailValidator validator = new CustomEmailValidator();

            validator.initialize((Email) constraint);

            if (!validator.isValid(element, context)) {
                log.error("Element [{}] is invalid according to: {}", index, validator.getClass().getName());
                return false;
            }
        }
        return true;
    }
}
