package com.rajbank.loanapp.service;

import com.rajbank.loanapp.dao.SequenceDao;
import com.rajbank.loanapp.dao.impl.SequenceDaoImpl;

import static com.rajbank.loanapp.util.Constants.APPLICATION_NUMBER_PREFIX;
import static com.rajbank.loanapp.util.Constants.APPLICATION_NUMBER_SEQUENCE;
import static com.rajbank.loanapp.util.Constants.CUSTOMER_ID_PREFIX;
import static com.rajbank.loanapp.util.Constants.CUSTOMER_ID_SEQUENCE;

public class IdGeneratorService {

    private final SequenceDao sequenceDao;

    public IdGeneratorService() {
        this(new SequenceDaoImpl());
    }

    public IdGeneratorService(SequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    /** Generates the next Customer ID, e.g. CUST00001. */
    public String nextCustomerId() {
        long value = sequenceDao.nextValue(CUSTOMER_ID_SEQUENCE);
        return CUSTOMER_ID_PREFIX + String.format("%05d", value);
    }

    /** Generates the next Loan Application Number, e.g. LA00000001. */
    public String nextApplicationNumber() {
        long value = sequenceDao.nextValue(APPLICATION_NUMBER_SEQUENCE);
        return APPLICATION_NUMBER_PREFIX + String.format("%08d", value);
    }
}
