package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.model.EmploymentType;
import com.rajbank.loanapp.model.Gender;
import com.rajbank.loanapp.service.CustomerService;
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
import java.time.LocalDate;

/**
 * Handles submission of the Customer Form (Screen-3) for both the new-customer
 * registration flow and the existing-customer edit flow. On success the Maker
 * proceeds to the Loan Application Form (Screen-4).
 */
@WebServlet("/maker/customer/save")
public class CustomerSaveServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(CustomerSaveServlet.class);

    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode");
        Customer customer = bindFromRequest(request);

        try {
            Customer saved = "EDIT".equals(mode)
                    ? customerService.updateExistingCustomer(customer)
                    : customerService.createNewCustomer(customer);
            response.sendRedirect(request.getContextPath() + "/maker/loan/new?customerId=" + saved.getCustomerId());
        } catch (ValidationException ex) {
            LOGGER.warn("Customer form validation failed: {}", ex.getFieldErrors());
            request.setAttribute("customer", customer);
            request.setAttribute("mode", mode);
            request.setAttribute("fieldErrors", ex.getFieldErrors());
            request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_form.jsp").forward(request, response);
        }
    }

    private Customer bindFromRequest(HttpServletRequest request) {
        Customer customer = new Customer();
        String customerId = request.getParameter("customerId");
        if (customerId != null && !customerId.isBlank()) {
            customer.setCustomerId(customerId);
        }
        customer.setFullName(trimmed(request.getParameter("fullName")));
        customer.setDateOfBirth(parseDate(request.getParameter("dateOfBirth")));
        customer.setGender(parseEnum(Gender.class, request.getParameter("gender")));
        customer.setEmail(trimmed(request.getParameter("email")));
        customer.setPhone(trimmed(request.getParameter("phone")));
        customer.setAddress(trimmed(request.getParameter("address")));
        customer.setPanNumber(upper(request.getParameter("panNumber")));
        customer.setAadharNumber(trimmed(request.getParameter("aadharNumber")));
        customer.setAnnualIncome(parseDecimal(request.getParameter("annualIncome")));
        customer.setEmploymentType(parseEnum(EmploymentType.class, request.getParameter("employmentType")));
        return customer;
    }

    private String trimmed(String value) {
        return value == null ? null : value.trim();
    }

    private String upper(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    private LocalDate parseDate(String value) {
        try {
            return value == null || value.isBlank() ? null : LocalDate.parse(value);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private BigDecimal parseDecimal(String value) {
        try {
            return value == null || value.isBlank() ? null : new BigDecimal(value.trim());
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
