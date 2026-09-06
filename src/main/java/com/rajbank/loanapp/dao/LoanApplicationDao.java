package com.rajbank.loanapp.dao;

import com.rajbank.loanapp.model.ApplicationStatus;
import com.rajbank.loanapp.model.LoanApplication;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationDao {

    Optional<LoanApplication> findByApplicationNumber(String applicationNumber);

    LoanApplication save(LoanApplication application);

    LoanApplication update(LoanApplication application);

    void delete(LoanApplication application);

    List<LoanApplication> findAll();

    List<LoanApplication> findByCreatedBy(String createdBy);

    List<LoanApplication> findByStatus(ApplicationStatus status);
}
