package com.techwave.paymentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BankAccountRequest {
    
    @Size(max = 255, message = "Beneficiary must not exceed 255 characters")
    private String beneficiary;
    
    @Size(max = 2000, message = "Beneficiary address must not exceed 2000 characters")
    private String beneficiaryAddress;
    
    @Size(max = 255, message = "Nickname must not exceed 255 characters")
    private String nickname;
    
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]*$", message = "Invalid IBAN format")
    private String iban;
    
    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "Invalid BIC format")
    private String bic;
    
    @Size(max = 255, message = "Account number must not exceed 255 characters")
    private String accountNumber;
    
    @Size(max = 50, message = "National bank code must not exceed 50 characters")
    private String nationalBankCode;
    
    @Size(max = 50, message = "National branch code must not exceed 50 characters")
    private String nationalBranchCode;
    
    @Size(max = 50, message = "National clearing code must not exceed 50 characters")
    private String nationalClearingCode;
    
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code (must be 3 uppercase letters)")
    private String currency;
    
    @NotNull(message = "Country is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Invalid country code (must be 2 uppercase letters)")
    private String country;
    
    public BankAccountRequest() {
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
