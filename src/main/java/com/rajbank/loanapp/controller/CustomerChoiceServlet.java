package com.rajbank.loanapp.controller;

import com.rajbank.loanapp.util.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Screen-1: does the customer applying for the loan already exist, or are they new? */
@WebServlet("/maker/customer/choice")
public class CustomerChoiceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Constants.VIEWS_PATH + "customer_choice.jsp").forward(request, response);
    }
}
