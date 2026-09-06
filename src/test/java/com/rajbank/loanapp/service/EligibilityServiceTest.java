package com.rajbank.loanapp.service;

import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.model.EmploymentType;
import com.rajbank.loanapp.model.Gender;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.model.LoanType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EligibilityServiceTest {

    private final EligibilityService eligibilityService = new EligibilityService();

    private Customer customer(int age, BigDecimal annualIncome, EmploymentType employmentType) {
        Customer customer = new Customer();
        customer.setCustomerId("CUST00001");
        customer.setFullName("Test Customer");
        customer.setDateOfBirth(LocalDate.now().minusYears(age));
        customer.setGender(Gender.MALE);
        customer.setEmail("test@example.com");
        customer.setPhone("9876543210");
        customer.setAddress("123 Main St");
        customer.setPanNumber("ABCDE1234F");
        customer.setAadharNumber("123456789012");
        customer.setAnnualIncome(annualIncome);
        customer.setEmploymentType(employmentType);
        return customer;
    }

    private LoanApplication application(BigDecimal loanAmount, int tenureMonths) {
        LoanApplication application = new LoanApplication();
        application.setApplicationNumber("LA00000001");
        application.setLoanType(LoanType.PERSONAL);
        application.setLoanAmount(loanAmount);
        application.setTenureMonths(tenureMonths);
        application.setInterestRate(new BigDecimal("10.5"));
        return application;
    }

    @Test
    void allCriteriaSatisfied_isEligible() {
        Customer customer = customer(30, new BigDecimal("1200000"), EmploymentType.SALARIED);
        LoanApplication application = application(new BigDecimal("300000"), 36);

        EligibilityResult result = eligibilityService.evaluate(customer, application);

        assertTrue(result.isEligible());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void ageBelowMinimum_isNotEligible() {
        Customer customer = customer(19, new BigDecimal("1200000"), EmploymentType.SALARIED);
        LoanApplication application = application(new BigDecimal("100000"), 12);

        EligibilityResult result = eligibilityService.evaluate(customer, application);

        assertFalse(result.isEligible());
    }

    @Test
    void loanAmountExceedsIncomeMultiple_isNotEligible() {
        Customer customer = customer(35, new BigDecimal("500000"), EmploymentType.SALARIED);
        LoanApplication application = application(new BigDecimal("5000000"), 60);

        EligibilityResult result = eligibilityService.evaluate(customer, application);

        assertFalse(result.isEligible());
    }

    @Test
    void unemployedCustomer_isNotEligible() {
        Customer customer = customer(30, new BigDecimal("1200000"), EmploymentType.UNEMPLOYED);
        LoanApplication application = application(new BigDecimal("100000"), 12);

        EligibilityResult result = eligibilityService.evaluate(customer, application);

        assertFalse(result.isEligible());
    }
}
