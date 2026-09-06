package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.service.LoanApplicationService;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.Constants;
import com.rajbank.loanapp.util.MessageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Public loan application status check (no login required). Any user can
 * enter a Loan Application ID at any time and see its current status.
 */
@WebServlet("/status")
public class StatusServlet extends HttpServlet {

    private final LoanApplicationService loanApplicationService = new LoanApplicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Constants.VIEWS_PATH + "status_check.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String applicationNumber = request.getParameter("applicationNumber");
        try {
            LoanApplication application = loanApplicationService.findByApplicationNumberOrThrow(applicationNumber);
            request.setAttribute("application", application);
        } catch (ValidationException ex) {
            request.setAttribute("errorMessage", MessageUtil.get(ex.getMessage(), request));
            request.setAttribute("applicationNumber", applicationNumber);
        }
        request.getRequestDispatcher(Constants.VIEWS_PATH + "status_check.jsp").forward(request, response);
    }
}
