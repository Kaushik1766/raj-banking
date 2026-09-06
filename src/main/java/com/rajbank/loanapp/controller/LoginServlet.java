package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.Role;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.service.AuthService;
import com.rajbank.loanapp.util.Constants;
import com.rajbank.loanapp.util.CookieUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.rajbank.loanapp.util.Constants.REMEMBER_ME_COOKIE;
import static com.rajbank.loanapp.util.Constants.REMEMBER_ME_MAX_AGE_SECONDS;
import static com.rajbank.loanapp.util.Constants.SESSION_USER;

/**
 * Common login page (Task 1). Supports the "Keep me signed in" remember-me
 * flow: a returning visitor carrying a valid remember-me cookie is
 * transparently signed back in and redirected straight to their Loan
 * Application screen instead of seeing the login form again.
 */
@WebServlet(urlPatterns = {"/login", ""})
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession existingSession = request.getSession(false);
        User sessionUser = existingSession == null ? null : (User) existingSession.getAttribute(SESSION_USER);
        if (sessionUser != null) {
            redirectToLanding(request, response, sessionUser);
            return;
        }

        Optional<String> rememberMeCookie = CookieUtil.read(request, REMEMBER_ME_COOKIE);
        if (rememberMeCookie.isPresent()) {
            Optional<User> user = authService.resolveRememberMe(rememberMeCookie.get());
            if (user.isPresent()) {
                request.getSession(true).setAttribute(SESSION_USER, user.get());
                LOGGER.info("User [{}] auto-signed-in via remember-me cookie", user.get().getUsername());
                redirectToLanding(request, response, user.get());
                return;
            }
            CookieUtil.clear(response, REMEMBER_ME_COOKIE);
        }

        request.getRequestDispatcher(Constants.VIEWS_PATH + "login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean keepSignedIn = "on".equals(request.getParameter("keepSignedIn"));

        Optional<User> user = authService.authenticate(username, password);
        if (user.isEmpty()) {
            request.setAttribute("errorMessageKey", "error.invalidLogin");
            request.setAttribute("username", username);
            RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.VIEWS_PATH + "login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER, user.get());

        if (keepSignedIn) {
            String cookieValue = authService.createRememberMeToken(user.get());
            CookieUtil.write(response, REMEMBER_ME_COOKIE, cookieValue, REMEMBER_ME_MAX_AGE_SECONDS);
        }

        redirectToLanding(request, response, user.get());
    }

    private void redirectToLanding(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String target = user.getRole() == Role.MAKER ? "/maker/dashboard" : "/checker/dashboard";
        response.sendRedirect(request.getContextPath() + target);
    }
}
