package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.CustomerDao;
import com.rajbank.loanapp.dao.impl.CustomerDaoImpl;
import com.rajbank.loanapp.model.Customer;
import com.rajbank.loanapp.service.exception.NotFoundException;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.ValidatorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);

    private final CustomerDao customerDao;
    private final IdGeneratorService idGeneratorService;

    public CustomerService() {
        this(new CustomerDaoImpl(), new IdGeneratorService());
    }

    public CustomerService(CustomerDao customerDao, IdGeneratorService idGeneratorService) {
        this.customerDao = customerDao;
        this.idGeneratorService = idGeneratorService;
    }

    /**
     * Looks up an existing customer by id, per Screen-2/Screen-3 of the Maker's
     * "existing customer" flow.
     *
     * @throws ValidationException if no customer id was supplied
     * @throws NotFoundException   if the customer id does not resolve to a record
     */
    public Customer findByIdOrThrow(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new ValidationException("error.customerIdMandatory");
        }
        Optional<Customer> customer = customerDao.findById(customerId.trim());
        if (customer.isEmpty()) {
            throw new ValidationException("error.customerIdInvalid");
        }
        return customer.get();
    }

    public Customer createNewCustomer(Customer customer) {
        Map<String, String> errors = ValidatorUtil.validate(customer);
        if (!errors.isEmpty()) {
            throw new ValidationException("error.validationFailed", errors);
        }
        if (customerDao.existsByPanNumber(customer.getPanNumber())) {
            throw new ValidationException("PAN number already registered to another customer",
                    Map.of("panNumber", "PAN number already registered to another customer"));
        }
        if (customerDao.existsByAadharNumber(customer.getAadharNumber())) {
            throw new ValidationException("Aadhar number already registered to another customer",
                    Map.of("aadharNumber", "Aadhar number already registered to another customer"));
        }
        customer.setCustomerId(idGeneratorService.nextCustomerId());
        Customer saved = customerDao.save(customer);
        LOGGER.info("Created new customer [{}]", saved.getCustomerId());
        return saved;
    }

    public Customer updateExistingCustomer(Customer customer) {
        Map<String, String> errors = ValidatorUtil.validate(customer);
        if (!errors.isEmpty()) {
            throw new ValidationException("error.validationFailed", errors);
        }
        Customer updated = customerDao.update(customer);
        LOGGER.info("Updated customer [{}]", updated.getCustomerId());
        return updated;
    }
}
