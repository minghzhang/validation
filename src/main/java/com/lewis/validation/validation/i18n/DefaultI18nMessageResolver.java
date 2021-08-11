package com.lewis.validation.validation.i18n;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author : landon
 * @date : 2021/8/11
 */
@Slf4j
@Component
public class DefaultI18nMessageResolver implements I18nMessageResolver {

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.CHINA);
    }

    @Override
    public String getMessage(String key, Object[] args, Locale locale) {
        return messageSource.getMessage(key, args, locale);
    }
}
