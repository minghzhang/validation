package com.lewis.validation.validation.i18n;

import java.util.Locale;

/**
 * @date : 2021/8/11
 */
public interface I18nMessageResolver {

    String getMessage(String key, Object... args);

    String getMessage(String key, Object[] args, Locale locale);
}
