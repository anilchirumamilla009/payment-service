package com.techwave.paymentservice.dto.response;

import java.util.UUID;

public class BankAccountAuditResponse {
    
    private UUID resource;
    private String resourceType;
    private Long version;
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
    
    public BankAccountAuditResponse() {
    }
    
    public BankAccountAuditResponse(UUID resource, Long version, String beneficiary, String beneficiaryAddress,
                                   String nickname, String iban, String bic, String accountNumber,
                                   String nationalBankCode, String nationalBranchCode,
                                   String nationalClearingCode, String currency, String country) {
        this.resource = resource;
        this.resourceType = "bank-account-audits";
        this.version = version;
        this.beneficiary = beneficiary;
        this.beneficiaryAddress = beneficiaryAddress;
        this.nickname = nickname;
        this.iban = iban;
        this.bic = bic;
        this.accountNumber = accountNumber;
        this.nationalBankCode = nationalBankCode;
        this.nationalBranchCode = nationalBranchCode;
        this.nationalClearingCode = nationalClearingCode;
        this.currency = currency;
        this.country = country;
    }
    
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
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
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
