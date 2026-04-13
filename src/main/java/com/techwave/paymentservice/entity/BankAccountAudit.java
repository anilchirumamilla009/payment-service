package com.techwave.paymentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bank_account_audits")
public class BankAccountAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resource;
    
    @Column(name = "version", nullable = false)
    private Long version;
    
    @Column(name = "beneficiary", length = 255)
    private String beneficiary;
    
    @Column(name = "beneficiary_address", columnDefinition = "TEXT")
    private String beneficiaryAddress;
    
    @Column(name = "nickname", length = 255)
    private String nickname;
    
    @Column(name = "iban", length = 34)
    private String iban;
    
    @Column(name = "bic", length = 11)
    private String bic;
    
    @Column(name = "account_number", length = 255)
    private String accountNumber;
    
    @Column(name = "national_bank_code", length = 50)
    private String nationalBankCode;
    
    @Column(name = "national_branch_code", length = 50)
    private String nationalBranchCode;
    
    @Column(name = "national_clearing_code", length = 50)
    private String nationalClearingCode;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "country", length = 2)
    private String country;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public BankAccountAudit() {
    }
    
    public BankAccountAudit(UUID resource, Long version, String beneficiary, String beneficiaryAddress,
                           String nickname, String iban, String bic, String accountNumber,
                           String nationalBankCode, String nationalBranchCode, String nationalClearingCode,
                           String currency, String country) {
        this.resource = resource;
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
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UUID getResource() {
        return resource;
    }
    
    public void setResource(UUID resource) {
        this.resource = resource;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
