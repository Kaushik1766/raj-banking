package com.rajbank.loanapp.service;

import java.util.List;

public class EligibilityResult {

    private final boolean eligible;
    private final List<String> reasons;

    public EligibilityResult(boolean eligible, List<String> reasons) {
        this.eligible = eligible;
        this.reasons = reasons;
    }

    public boolean isEligible() {
        return eligible;
    }

    public List<String> getReasons() {
        return reasons;
    }
}
