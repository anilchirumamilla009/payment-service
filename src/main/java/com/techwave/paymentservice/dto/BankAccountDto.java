package com.techwave.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * DTO representing a bank account,
 * matching the OpenAPI BankAccount schema.
 */
public class BankAccountDto {

    private UUID id;
    private String resourceType = "bank-accounts";

    @NotBlank(message = "Beneficiary is required")
    @Size(max = 255, message = "Beneficiary must not exceed 255 characters")
    private String beneficiary;

    @Size(max = 500, message = "Beneficiary address must not exceed 500 characters")
    private String beneficiaryAddress;

    @Size(max = 255, message = "Nickname must not exceed 255 characters")
    private String nickname;

    @Size(max = 34, message = "IBAN must not exceed 34 characters")
    private String iban;

    @Size(max = 11, message = "BIC must not exceed 11 characters")
    private String bic;

    @Size(max = 50, message = "Account number must not exceed 50 characters")
    private String accountNumber;

    @Size(max = 50, message = "National bank code must not exceed 50 characters")
    private String nationalBankCode;

    @Size(max = 50, message = "National branch code must not exceed 50 characters")
    private String nationalBranchCode;

    @Size(max = 50, message = "National clearing code must not exceed 50 characters")
    private String nationalClearingCode;

    @Size(max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @Size(max = 2, message = "Country must be a 2-letter ISO code")
    private String country;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNationalBankCode() {
        return nationalBankCode;
    }

    public void setNationalBankCode(String nationalBankCode) {
        this.nationalBankCode = nationalBankCode;
    }

    public String getNationalBranchCode() {
        return nationalBranchCode;
    }

    public void setNationalBranchCode(String nationalBranchCode) {
        this.nationalBranchCode = nationalBranchCode;
    }

    public String getNationalClearingCode() {
        return nationalClearingCode;
    }

    public void setNationalClearingCode(String nationalClearingCode) {
        this.nationalClearingCode = nationalClearingCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
