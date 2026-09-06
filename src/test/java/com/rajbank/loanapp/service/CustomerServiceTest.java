package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.CustomerDao;
import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private CustomerDao customerDao;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerDao = Mockito.mock(CustomerDao.class);
        customerService = new CustomerService(customerDao, new IdGeneratorService(name -> 1L));
    }

    @Test
    void blankCustomerId_throwsMandatoryValidationException() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> customerService.findByIdOrThrow("  "));
        assertEquals("error.customerIdMandatory", ex.getMessage());
    }

    @Test
    void unknownCustomerId_throwsInvalidValidationException() {
        when(customerDao.findById("CUST99999")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> customerService.findByIdOrThrow("CUST99999"));
        assertEquals("error.customerIdInvalid", ex.getMessage());
    }

    @Test
    void knownCustomerId_returnsCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId("CUST00001");
        when(customerDao.findById("CUST00001")).thenReturn(Optional.of(customer));

        Customer found = customerService.findByIdOrThrow("CUST00001");

        assertEquals("CUST00001", found.getCustomerId());
    }
}
