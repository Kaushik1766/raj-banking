package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.service.AuthService;
import com.rajbank.loanapp.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.rajbank.loanapp.util.Constants.REMEMBER_ME_COOKIE;
import static com.rajbank.loanapp.util.Constants.SESSION_USER;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute(SESSION_USER);
            if (user != null) {
                authService.invalidateRememberMe(user.getUsername());
            }
            session.invalidate();
        }
        CookieUtil.clear(response, REMEMBER_ME_COOKIE);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
