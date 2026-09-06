package com.rajbank.loanapp.util;

public final class Constants {

    private Constants() {
    }

    public static final String SESSION_USER = "loggedInUser";

    public static final String REMEMBER_ME_COOKIE = "rememberMe";
    public static final int REMEMBER_ME_MAX_AGE_SECONDS = 30 * 24 * 60 * 60; // 30 days

    public static final String LOCALE_COOKIE = "preferredLocale";
    public static final int LOCALE_COOKIE_MAX_AGE_SECONDS = 365 * 24 * 60 * 60;

    public static final String CUSTOMER_ID_SEQUENCE = "CUSTOMER_ID";
    public static final String APPLICATION_NUMBER_SEQUENCE = "APPLICATION_NUMBER";

    public static final String CUSTOMER_ID_PREFIX = "CUST";
    public static final String APPLICATION_NUMBER_PREFIX = "LA";

    public static final String VIEWS_PATH = "/WEB-INF/views/";
}
