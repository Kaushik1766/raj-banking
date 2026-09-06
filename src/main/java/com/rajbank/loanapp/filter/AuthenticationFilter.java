package com.rajbank.loanapp.filter;

import com.rajbank.loanapp.model.Role;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Guards the role-specific areas of the application: only an authenticated
 * MAKER may reach {@code /maker/*} and only an authenticated CHECKER may
 * reach {@code /checker/*}. Everything else (login, public status check,
 * static assets) is reachable without a session.
 */
@WebFilter(urlPatterns = {"/maker/*", "/checker/*"})
public class AuthenticationFilter extends HttpFilter {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constants.SESSION_USER);

        if (user == null) {
            LOGGER.warn("Unauthenticated access attempt to [{}]", request.getRequestURI());
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        boolean makerAreaOk = path.startsWith("/maker") && user.getRole() == Role.MAKER;
        boolean checkerAreaOk = path.startsWith("/checker") && user.getRole() == Role.CHECKER;

        if (!makerAreaOk && !checkerAreaOk) {
            LOGGER.warn("User [{}] with role [{}] denied access to [{}]", user.getUsername(), user.getRole(), path);
            request.setAttribute("errorMessageKey", "error.unauthorized");
            request.getRequestDispatcher(Constants.VIEWS_PATH + "error.jsp").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
