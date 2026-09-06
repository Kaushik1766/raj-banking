package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.LoanApplicationDao;
import com.rajbank.loanapp.dao.impl.LoanApplicationDaoImpl;
import com.rajbank.loanapp.model.ApplicationStatus;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.service.exception.ValidationException;
import com.rajbank.loanapp.util.ValidatorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LoanApplicationService {

    private static final Logger LOGGER = LogManager.getLogger(LoanApplicationService.class);

    private final LoanApplicationDao loanApplicationDao;
    private final IdGeneratorService idGeneratorService;

    public LoanApplicationService() {
        this(new LoanApplicationDaoImpl(), new IdGeneratorService());
    }

    public LoanApplicationService(LoanApplicationDao loanApplicationDao, IdGeneratorService idGeneratorService) {
        this.loanApplicationDao = loanApplicationDao;
        this.idGeneratorService = idGeneratorService;
    }

    public LoanApplication createApplication(LoanApplication application, String createdBy) {
        Map<String, String> errors = ValidatorUtil.validate(application);
        if (!errors.isEmpty()) {
            throw new ValidationException("error.validationFailed", errors);
        }
        application.setApplicationNumber(idGeneratorService.nextApplicationNumber());
        application.setStatus(ApplicationStatus.PENDING);
        application.setCreatedBy(createdBy);
        application.setCreatedDate(LocalDateTime.now());
        LoanApplication saved = loanApplicationDao.save(application);
        LOGGER.info("Loan application [{}] created by [{}]", saved.getApplicationNumber(), createdBy);
        return saved;
    }

    /**
     * Looks up an application by number, per the public status-check screen and
     * the Checker's validation scenarios.
     *
     * @throws ValidationException if no application number was supplied, or it does not exist
     */
    public LoanApplication findByApplicationNumberOrThrow(String applicationNumber) {
        if (applicationNumber == null || applicationNumber.isBlank()) {
            throw new ValidationException("error.applicationIdMandatory");
        }
        Optional<LoanApplication> application = loanApplicationDao.findByApplicationNumber(applicationNumber.trim());
        if (application.isEmpty()) {
            throw new ValidationException("error.applicationNumberInvalid");
        }
        return application.get();
    }

    public List<LoanApplication> findAll() {
        return loanApplicationDao.findAll();
    }

    public List<LoanApplication> findByCreatedBy(String createdBy) {
        return loanApplicationDao.findByCreatedBy(createdBy);
    }

    public List<LoanApplication> findPending() {
        return loanApplicationDao.findByStatus(ApplicationStatus.PENDING);
    }

    public void deleteApplication(String applicationNumber) {
        LoanApplication application = findByApplicationNumberOrThrow(applicationNumber);
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ValidationException("Only pending applications can be deleted");
        }
        loanApplicationDao.delete(application);
        LOGGER.info("Loan application [{}] deleted", applicationNumber);
    }

    public LoanApplication approve(String applicationNumber, String checkedBy, String remarks) {
        return decide(applicationNumber, ApplicationStatus.APPROVED, checkedBy, remarks);
    }

    public LoanApplication reject(String applicationNumber, String checkedBy, String remarks) {
        return decide(applicationNumber, ApplicationStatus.REJECTED, checkedBy, remarks);
    }

    private LoanApplication decide(String applicationNumber, ApplicationStatus status, String checkedBy, String remarks) {
        LoanApplication application = findByApplicationNumberOrThrow(applicationNumber);
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ValidationException("This application has already been " + application.getStatus());
        }
        application.setStatus(status);
        application.setCheckedBy(checkedBy);
        application.setCheckedDate(LocalDateTime.now());
        application.setRemarks(remarks);
        LoanApplication updated = loanApplicationDao.update(application);
        LOGGER.info("Loan application [{}] {} by [{}]", applicationNumber, status, checkedBy);
        return updated;
    }
}
