package com.techwave.paymentservice.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Audit representation for a bank account entity.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankAccountAudit {

    private UUID resource;
    private String resourceType;
    private Integer version;
    private String beneficiary;
    private String beneficiaryAddress;
    private String nickname;
    private String iban;
    private String bic;
    private String accountNumber;
    private String nationalBankCode;
    private String nationalBranchCode;
    private String nationalClearingCode;
    private String currency;
    private String country;

    public UUID getResource() {
        return resource;
    }

    public void setResource(UUID resource) {
        this.resource = resource;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

