package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.service.LoanApplicationService;
import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Checker landing screen: pending loan applications awaiting a decision. */
@WebServlet("/checker/dashboard")
public class CheckerDashboardServlet extends HttpServlet {

    private final LoanApplicationService loanApplicationService = new LoanApplicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("applications", loanApplicationService.findPending());
        request.getRequestDispatcher(Constants.VIEWS_PATH + "checker_dashboard.jsp").forward(request, response);
    }
}
