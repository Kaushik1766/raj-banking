package com.rajbank.loanapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * customerId, panNumber, aadharNumber and dateOfBirth are identity-establishing
 * fields. Once a customer record exists they are treated as mandatory/immutable
 * by the Maker UI (Screen-3) - every other field remains editable.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_id", length = 20)
    private String customerId;

    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @NotNull
    @Past
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    @Column(name = "phone", nullable = false, length = 10)
    private String phone;

    @NotBlank
    @Size(max = 255)
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotBlank
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "PAN must match the format AAAAA9999A")
    @Column(name = "pan_number", nullable = false, unique = true, length = 10)
    private String panNumber;

    @NotBlank
    @Pattern(regexp = "\\d{12}", message = "Aadhar number must be 12 digits")
    @Column(name = "aadhar_number", nullable = false, unique = true, length = 12)
    private String aadharNumber;

    @NotNull
    @PositiveOrZero
    @Column(name = "annual_income", nullable = false, precision = 15, scale = 2)
    private BigDecimal annualIncome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 20)
    private EmploymentType employmentType;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
