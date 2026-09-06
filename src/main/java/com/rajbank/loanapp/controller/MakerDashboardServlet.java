package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.service.LoanApplicationService;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Screen-5 for the Maker: existing loan applications in tabular form, with
 * actions to create a new application or delete an existing one.
 */
@WebServlet("/maker/dashboard")
public class MakerDashboardServlet extends HttpServlet {

    private final LoanApplicationService loanApplicationService = new LoanApplicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("applications", loanApplicationService.findAll());
        request.getRequestDispatcher(Constants.VIEWS_PATH + "maker_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String applicationNumber = request.getParameter("applicationNumber");
        try {
            loanApplicationService.deleteApplication(applicationNumber);
            request.getSession().setAttribute("flashMessageKey", "msg.applicationDeleted");
        } catch (ValidationException ex) {
            request.getSession().setAttribute("flashError", ex.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/maker/dashboard");
    }
}
