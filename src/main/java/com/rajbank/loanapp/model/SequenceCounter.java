package com.rajbank.loanapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Backs generation of human-readable, sequential business identifiers
 * (Customer IDs, Loan Application Numbers). Rows are updated under a
 * pessimistic lock so concurrent Makers never collide on the same number.
 */
@Entity
@Table(name = "sequence_counters")
public class SequenceCounter {

    @Id
    @Column(name = "seq_name", length = 50)
    private String seqName;

    @Column(name = "next_value", nullable = false)
    private long nextValue;

    public SequenceCounter() {
    }

    public SequenceCounter(String seqName, long nextValue) {
        this.seqName = seqName;
        this.nextValue = nextValue;
    }

    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    public long getNextValue() {
        return nextValue;
    }

    public void setNextValue(long nextValue) {
        this.nextValue = nextValue;
    }
}
