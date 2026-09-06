package com.rajbank.loanapp.dao;

import com.rajbank.loanapp.dao.impl.CustomerDaoImpl;
import com.rajbank.loanapp.dao.impl.LoanApplicationDaoImpl;
import com.rajbank.loanapp.dao.impl.SequenceDaoImpl;
import com.rajbank.loanapp.model.ApplicationStatus;
import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.model.EmploymentType;
import com.rajbank.loanapp.model.Gender;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.model.LoanType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercises the Hibernate DAO layer end to end against an in-memory H2
 * database (see src/test/resources/hibernate.cfg.xml), proving out the
 * entity mappings, HQL queries and the pessimistic-lock sequence generator
 * without requiring a real MySQL instance in CI.
 */
class DaoIntegrationTest {

    private Customer newCustomer(String id) {
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setFullName("Jane Doe");
        customer.setDateOfBirth(LocalDate.now().minusYears(30));
        customer.setGender(Gender.FEMALE);
        customer.setEmail("jane@example.com");
        customer.setPhone("9876543210");
        customer.setAddress("1 Bank Street");
        String numericSuffix = id.replaceAll("\\D", "");
        String fourDigits = String.format("%4s", numericSuffix).replace(' ', '0').substring(0, 4);
        customer.setPanNumber("ABCDE" + fourDigits + "F");
        customer.setAadharNumber("12345678" + fourDigits);
        customer.setAnnualIncome(new BigDecimal("1000000"));
        customer.setEmploymentType(EmploymentType.SALARIED);
        return customer;
    }

    @Test
    void sequenceDao_generatesIncrementingValues() {
        SequenceDao sequenceDao = new SequenceDaoImpl();
        long first = sequenceDao.nextValue("TEST_SEQ");
        long second = sequenceDao.nextValue("TEST_SEQ");
        assertEquals(first + 1, second);
    }

    @Test
    void customerDao_savesAndFindsById() {
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = newCustomer("CUSTIT001");
        customerDao.save(customer);

        Optional<Customer> found = customerDao.findById("CUSTIT001");
        assertTrue(found.isPresent());
        assertEquals("Jane Doe", found.get().getFullName());
        assertTrue(customerDao.existsByPanNumber(customer.getPanNumber()));
    }

    @Test
    void loanApplicationDao_fullLifecycle() {
        CustomerDao customerDao = new CustomerDaoImpl();
        Customer customer = newCustomer("CUSTIT002");
        customerDao.save(customer);

        LoanApplicationDao loanApplicationDao = new LoanApplicationDaoImpl();
        LoanApplication application = new LoanApplication();
        application.setApplicationNumber("LAIT0000001");
        application.setCustomer(customer);
        application.setLoanType(LoanType.PERSONAL);
        application.setLoanAmount(new BigDecimal("200000"));
        application.setTenureMonths(24);
        application.setInterestRate(new BigDecimal("9.5"));
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedBy("maker1");
        loanApplicationDao.save(application);

        List<LoanApplication> pending = loanApplicationDao.findByStatus(ApplicationStatus.PENDING);
        assertTrue(pending.stream().anyMatch(a -> a.getApplicationNumber().equals("LAIT0000001")));

        application.setStatus(ApplicationStatus.APPROVED);
        application.setCheckedBy("checker1");
        loanApplicationDao.update(application);

        Optional<LoanApplication> updated = loanApplicationDao.findByApplicationNumber("LAIT0000001");
        assertTrue(updated.isPresent());
        assertEquals(ApplicationStatus.APPROVED, updated.get().getStatus());

        loanApplicationDao.delete(updated.get());
        assertTrue(loanApplicationDao.findByApplicationNumber("LAIT0000001").isEmpty());
    }
}
