package com.rajbank.loanapp.dao;

public interface SequenceDao {

    /**
     * Atomically increments and returns the next value for the given sequence
     * name, creating the counter (starting at 1) if it does not yet exist.
     */
    long nextValue(String sequenceName);
}
