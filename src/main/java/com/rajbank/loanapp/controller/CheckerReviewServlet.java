package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.service.EligibilityResult;
import com.rajbank.loanapp.service.EligibilityService;
import com.rajbank.loanapp.service.LoanApplicationService;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.Constants;
import com.rajbank.loanapp.util.MessageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.rajbank.loanapp.util.Constants.SESSION_USER;

/**
 * Checker's review screen for a single pending application: runs the
 * eligibility check (Scenarios 6/7) and lets the Checker approve or reject.
 */
@WebServlet("/checker/review")
public class CheckerReviewServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(CheckerReviewServlet.class);

    private final LoanApplicationService loanApplicationService = new LoanApplicationService();
    private final EligibilityService eligibilityService = new EligibilityService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String applicationNumber = request.getParameter("applicationNumber");
        try {
            LoanApplication application = loanApplicationService.findByApplicationNumberOrThrow(applicationNumber);
            EligibilityResult eligibility = eligibilityService.evaluate(application.getCustomer(), application);
            request.setAttribute("application", application);
            request.setAttribute("eligibility", eligibility);
            request.setAttribute("eligibilityMessageKey", eligibility.isEligible() ? "loan.approved" : "loan.rejected");
            request.getRequestDispatcher(Constants.VIEWS_PATH + "checker_review.jsp").forward(request, response);
        } catch (ValidationException ex) {
            request.setAttribute("errorMessageKey", ex.getMessage());
            request.setAttribute("errorMessage", MessageUtil.get(ex.getMessage(), request));
            request.getRequestDispatcher(Constants.VIEWS_PATH + "error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String applicationNumber = request.getParameter("applicationNumber");
        String action = request.getParameter("action");
        String remarks = request.getParameter("remarks");
        User checker = (User) request.getSession().getAttribute(SESSION_USER);

        try {
            if ("approve".equals(action)) {
                loanApplicationService.approve(applicationNumber, checker.getUsername(), remarks);
            } else if ("reject".equals(action)) {
                loanApplicationService.reject(applicationNumber, checker.getUsername(), remarks);
            }
            request.getSession().setAttribute("flashMessageKey", "msg.statusUpdated");
            response.sendRedirect(request.getContextPath() + "/checker/dashboard");
        } catch (ValidationException ex) {
            LOGGER.warn("Unable to record decision for [{}]: {}", applicationNumber, ex.getMessage());
            request.setAttribute("errorMessageKey", ex.getMessage());
            request.setAttribute("errorMessage", MessageUtil.get(ex.getMessage(), request));
            request.getRequestDispatcher(Constants.VIEWS_PATH + "error.jsp").forward(request, response);
        }
    }
}
