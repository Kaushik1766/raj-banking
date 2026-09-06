package com.rajbank.loanapp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Central i18n lookup used by controllers/services for messages that are
 * generated server-side (validation errors, flash messages). JSP views use
 * the equivalent JSTL {@code <fmt:message>} tag against the same "messages"
 * bundle, keeping a single source of truth for translated text.
 */
public final class MessageUtil {

    private static final String BASE_NAME = "messages";

    private MessageUtil() {
    }

    public static Locale resolveLocale(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Constants.LOCALE_COOKIE.equals(cookie.getName())) {
                    return Locale.forLanguageTag(cookie.getValue());
                }
            }
        }
        return request.getLocale();
    }

    public static String get(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        try {
            return bundle.getString(key);
        } catch (java.util.MissingResourceException ex) {
            // Not every exception message is a bundle key (e.g. ad-hoc business rule
            // violations) - fall back to the raw text rather than failing the request.
            return key;
        }
    }

    public static String get(String key, Locale locale, Object... args) {
        String pattern = get(key, locale);
        return MessageFormat.format(pattern, args);
    }

    public static String get(String key, HttpServletRequest request) {
        return get(key, resolveLocale(request));
    }
}
