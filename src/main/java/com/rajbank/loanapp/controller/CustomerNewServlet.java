package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** New-customer flow: presents a blank Customer Form for the Maker to register the customer. */
@WebServlet("/maker/customer/new")
public class CustomerNewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "NEW");
        request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_form.jsp").forward(request, response);
    }
}
