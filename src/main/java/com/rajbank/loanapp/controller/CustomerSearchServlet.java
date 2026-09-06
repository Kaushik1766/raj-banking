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

/**
 * Screen-2 / Screen-3: the Maker searches for an existing customer by
 * Customer ID; on a match the Customer Form is shown prefilled, with the
 * identity fields (Customer ID, PAN, Aadhar, DOB) locked from editing.
 */
@WebServlet("/maker/customer/search")
public class CustomerSearchServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String customerId = request.getParameter("customerId");
        try {
            Customer customer = customerService.findByIdOrThrow(customerId);
            request.setAttribute("customer", customer);
            request.setAttribute("mode", "EDIT");
            request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_form.jsp").forward(request, response);
        } catch (ValidationException ex) {
            request.setAttribute("errorMessage", MessageUtil.get(ex.getMessage(), request));
            request.setAttribute("customerId", customerId);
            request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_search.jsp").forward(request, response);
        }
    }
}
