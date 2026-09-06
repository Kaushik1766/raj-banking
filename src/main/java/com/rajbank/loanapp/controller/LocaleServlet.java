package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.rajbank.loanapp.util.Constants.LOCALE_COOKIE;
import static com.rajbank.loanapp.util.Constants.LOCALE_COOKIE_MAX_AGE_SECONDS;

/** Lets the user switch the UI language (Internationalization support). */
@WebServlet("/locale")
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String lang = request.getParameter("lang");
        if (lang != null && (lang.equals("en") || lang.equals("fr"))) {
            CookieUtil.write(response, LOCALE_COOKIE, lang, LOCALE_COOKIE_MAX_AGE_SECONDS);
        }
        String referer = request.getHeader("Referer");
        String appOrigin = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        boolean sameOrigin = referer != null && referer.startsWith(appOrigin);
        response.sendRedirect(sameOrigin ? referer : request.getContextPath() + "/login");
    }
}
