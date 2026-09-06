package com.rajbank.loanapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @Column(name = "application_number", length = 20)
    private String applicationNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false, length = 20)
    private LoanType loanType;

    @NotNull
    @Positive
    @Column(name = "loan_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @NotNull
    @Min(1)
    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Size(max = 255)
    @Column(name = "purpose", length = 255)
    private String purpose;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "checked_by", length = 50)
    private String checkedBy;

    @Column(name = "checked_date")
    private LocalDateTime checkedDate;

    @Column(name = "remarks", length = 500)
    private String remarks;

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(Integer tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public LocalDateTime getCheckedDate() {
        return checkedDate;
    }

    public void setCheckedDate(LocalDateTime checkedDate) {
        this.checkedDate = checkedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
