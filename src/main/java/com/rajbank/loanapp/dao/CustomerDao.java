package com.rajbank.loanapp.dao;

import com.rajbank.loanapp.model.Customer;

import java.util.Optional;

public interface CustomerDao {

    Optional<Customer> findById(String customerId);

    Customer save(Customer customer);

    Customer update(Customer customer);

    boolean existsByPanNumber(String panNumber);

    boolean existsByAadharNumber(String aadharNumber);
}
