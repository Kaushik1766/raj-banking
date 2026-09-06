package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.service.CustomerService;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.Constants;
import com.rajbank.loanapp.util.MessageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Screen-4 (entry point): shows the Loan Application Form for the given, already-saved, customer. */
@WebServlet("/maker/loan/new")
public class LoanApplicationNewServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        try {
            Customer customer = customerService.findByIdOrThrow(customerId);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher(Constants.VIEWS_PATH + "loan_application_form.jsp").forward(request, response);
        } catch (ValidationException ex) {
            request.setAttribute("errorMessageKey", ex.getMessage());
            request.setAttribute("errorMessage", MessageUtil.get(ex.getMessage(), request));
            request.getRequestDispatcher(Constants.VIEWS_PATH + "error.jsp").forward(request, response);
        }
    }
}
