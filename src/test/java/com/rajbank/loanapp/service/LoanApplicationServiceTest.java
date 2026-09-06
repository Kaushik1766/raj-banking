package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.LoanApplicationDao;
import com.rajbank.loanapp.model.LoanApplication;
import com.rajbank.loanapp.service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class LoanApplicationServiceTest {

    private LoanApplicationDao loanApplicationDao;
    private LoanApplicationService loanApplicationService;

    @BeforeEach
    void setUp() {
        loanApplicationDao = Mockito.mock(LoanApplicationDao.class);
        loanApplicationService = new LoanApplicationService(loanApplicationDao, new IdGeneratorService(name -> 1L));
    }

    @Test
    void blankApplicationNumber_throwsMandatoryValidationException() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> loanApplicationService.findByApplicationNumberOrThrow(""));
        assertEquals("error.applicationIdMandatory", ex.getMessage());
    }

    @Test
    void unknownApplicationNumber_throwsInvalidValidationException() {
        when(loanApplicationDao.findByApplicationNumber("LA99999999")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> loanApplicationService.findByApplicationNumberOrThrow("LA99999999"));
        assertEquals("error.applicationNumberInvalid", ex.getMessage());
    }

    @Test
    void knownApplicationNumber_returnsApplication() {
        LoanApplication application = new LoanApplication();
        application.setApplicationNumber("LA00000001");
        when(loanApplicationDao.findByApplicationNumber("LA00000001")).thenReturn(Optional.of(application));

        LoanApplication found = loanApplicationService.findByApplicationNumberOrThrow("LA00000001");

        assertEquals("LA00000001", found.getApplicationNumber());
    }
}
