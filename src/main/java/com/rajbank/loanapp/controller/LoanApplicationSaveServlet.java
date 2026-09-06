package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.model.LoanType;
import com.rajbank.loanapp.model.User;
import com.rajbank.loanapp.service.CustomerService;
import com.rajbank.loanapp.service.LoanApplicationService;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import static com.rajbank.loanapp.util.Constants.SESSION_USER;

/** Screen-4 submission: creates the loan application and generates its Application Number. */
@WebServlet("/maker/loan/save")
public class LoanApplicationSaveServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(LoanApplicationSaveServlet.class);

    private final CustomerService customerService = new CustomerService();
    private final LoanApplicationService loanApplicationService = new LoanApplicationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        Customer customer;
        try {
            customer = customerService.findByIdOrThrow(customerId);
        } catch (ValidationException ex) {
            request.setAttribute("errorMessageKey", ex.getMessage());
            request.getRequestDispatcher(Constants.VIEWS_PATH + "error.jsp").forward(request, response);
            return;
        }

        LoanApplication application = bindFromRequest(request, customer);
        User maker = (User) request.getSession().getAttribute(SESSION_USER);

        try {
            LoanApplication saved = loanApplicationService.createApplication(application, maker.getUsername());
            request.setAttribute("applicationNumber", saved.getApplicationNumber());
            request.getRequestDispatcher(Constants.VIEWS_PATH + "application_created.jsp").forward(request, response);
        } catch (ValidationException ex) {
            LOGGER.warn("Loan application validation failed: {}", ex.getFieldErrors());
            request.setAttribute("customer", customer);
            request.setAttribute("application", application);
            request.setAttribute("fieldErrors", ex.getFieldErrors());
            request.getRequestDispatcher(Constants.VIEWS_PATH + "loan_application_form.jsp").forward(request, response);
        }
    }

    private LoanApplication bindFromRequest(HttpServletRequest request, Customer customer) {
        LoanApplication application = new LoanApplication();
        application.setCustomer(customer);
        application.setLoanType(parseEnum(LoanType.class, request.getParameter("loanType")));
        application.setLoanAmount(parseDecimal(request.getParameter("loanAmount")));
        application.setTenureMonths(parseInt(request.getParameter("tenureMonths")));
        application.setInterestRate(parseDecimal(request.getParameter("interestRate")));
        application.setPurpose(request.getParameter("purpose"));
        return application;
    }

    private BigDecimal parseDecimal(String value) {
        try {
            return value == null || value.isBlank() ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return value == null || value.isBlank() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        try {
            return value == null || value.isBlank() ? null : Enum.valueOf(type, value.trim());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
