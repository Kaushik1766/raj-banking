package com.rajbank.loanapp.service;

import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.model.EmploymentType;
import com.rajbank.loanapp.model.LoanApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates a loan application against the bank's eligibility criteria. Used
 * by the Checker screen (Screen-5) to decide whether "Loan is Approved" or
 * "Loan is Rejected".
 */
public class EligibilityService {

    private static final Logger LOGGER = LogManager.getLogger(EligibilityService.class);

    private static final int MIN_AGE = 21;
    private static final int MAX_AGE = 60;
    private static final BigDecimal MAX_EMI_TO_MONTHLY_INCOME_RATIO = new BigDecimal("0.40");
    private static final BigDecimal MAX_LOAN_TO_ANNUAL_INCOME_RATIO = new BigDecimal("5");

    public EligibilityResult evaluate(Customer customer, LoanApplication application) {
        List<String> reasons = new ArrayList<>();

        int age = customer.getAge();
        if (age < MIN_AGE || age > MAX_AGE) {
            reasons.add("Customer age (" + age + ") is outside the eligible range of "
                    + MIN_AGE + "-" + MAX_AGE + " years");
        }

        if (customer.getEmploymentType() == EmploymentType.UNEMPLOYED) {
            reasons.add("Customer does not have a verifiable source of income");
        }

        BigDecimal annualIncome = customer.getAnnualIncome();
        BigDecimal loanAmount = application.getLoanAmount();
        int tenureMonths = application.getTenureMonths();

        if (annualIncome != null && annualIncome.signum() > 0 && loanAmount != null && tenureMonths > 0) {
            BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            BigDecimal emi = loanAmount.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
            BigDecimal maxAllowedEmi = monthlyIncome.multiply(MAX_EMI_TO_MONTHLY_INCOME_RATIO);
            if (emi.compareTo(maxAllowedEmi) > 0) {
                reasons.add("Estimated EMI (" + emi + ") exceeds " + MAX_EMI_TO_MONTHLY_INCOME_RATIO
                        + " of monthly income (" + monthlyIncome + ")");
            }

            BigDecimal maxLoanAmount = annualIncome.multiply(MAX_LOAN_TO_ANNUAL_INCOME_RATIO);
            if (loanAmount.compareTo(maxLoanAmount) > 0) {
                reasons.add("Loan amount (" + loanAmount + ") exceeds " + MAX_LOAN_TO_ANNUAL_INCOME_RATIO
                        + "x annual income (" + annualIncome + ")");
            }
        } else {
            reasons.add("Insufficient income or loan detail information to assess repayment capacity");
        }

        boolean eligible = reasons.isEmpty();
        LOGGER.info("Eligibility evaluated for application [{}]: eligible={}, reasons={}",
                application.getApplicationNumber(), eligible, reasons);
        return new EligibilityResult(eligible, reasons);
    }
}
